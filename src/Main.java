import java.util.*;
import java.sql.*;

public class Main {
    public static void loadJDBCDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't load JDBC driver: " + e.getMessage());
        }
    }
    public static void coachStats(Scanner scan, Connection con) {
        try {
            String coachName;
            System.out.print("Enter a coaches name: ");
            coachName = scan.nextLine();
            String query = "SELECT CoachName, CareerWins, count(t.CoachID) AS '# of Teams Trained'" +
                    " FROM TopNFLCoaches c JOIN Trains t ON c.CoachID = t.CoachID" +
                    " WHERE CoachName = ?" +
                    " GROUP BY c.CoachName;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, coachName);
            ResultSet result = stmt.executeQuery();
            System.out.println("Processing Results");

            System.out.println(String.format("%-20s | %-12s | %-18s", "Coach Name", "Career Wins", "Teams Trained"));
            while(result.next()) {
                System.out.println(String.format("%-20s | %-12s | %-18s",
                        result.getString("CoachName"),
                        result.getString("CareerWins"),
                        result.getString("# of Teams Trained")));
            }
        } catch(Exception e) {
            System.out.println("Error occurred" + e.getMessage());
        }
    }
    public static void searchByRank(Scanner scan, Connection con) {
        try {
            int rank;
            System.out.print("Enter a rank to search by: ");
            rank = scan.nextInt();
            scan.nextLine();
            String query = "SELECT Rank, TeamName, SongTitle, ArtistFirstName, ArtistLastName"+
                " FROM Teams t JOIN TopSongs s ON t.SongID = s.SongID JOIN Features f ON f.SongID = s.SongID" +
                " WHERE Rank = ?;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, rank);
            ResultSet result = stmt.executeQuery();
            System.out.println("Processing Results");

            System.out.println(String.format("%-5s | %-20s | %-30s | %-20s | %-20s",
                    "Rank", "Team Name", "Song Title", "Artist First Name", "Artist Last Name"));
            while(result.next()) {
                System.out.println(String.format("%-5s | %-20s | %-30s | %-20s | %-20s",
                        result.getString("Rank"),
                        result.getString("TeamName"),
                        result.getString("SongTitle"),
                        result.getString("ArtistFirstName"),
                        result.getString("ArtistLastName")));
            }
        } catch(Exception e) {
            System.out.println("Error occurred" + e.getMessage());
        }
    }
    public static void presentCoaches(Connection con) {
        try {
            String sql = "{CALL get_presentCoachesWithSongs()}";
            CallableStatement stmt = con.prepareCall(sql);
            ResultSet result = stmt.executeQuery();
            System.out.println("Processing Results");

            System.out.println(String.format("%-20s | %-30s | %-20s | %-20s",
                    "Coach Name", "Song Title", "Artist First Name", "Artist Last Name"));
            while (result.next()) {
                System.out.println(String.format("%-20s | %-30s | %-20s | %-20s",
                        result.getString("CoachName"),
                        result.getString("SongTitle"),
                        result.getString("ArtistFirstName"),
                        result.getString("ArtistLastName")));
            }
        } catch(Exception e) {
            System.out.println("Error occurred" + e.getMessage());
        }
    }
    public static void anthemsFrom1990(Connection con) {
        try {
            String query = "SELECT TeamName, SongTitle, ArtistFirstName, ArtistLastName" +
                    " FROM Teams t JOIN TopSongs s ON t.SongID = s.SongID" +
                    " JOIN Features f ON f.SongID = s.SongID" +
                    " WHERE t.SongID IN" +
                    "    (SELECT SongID" +
                    "       FROM TopSongs" +
                    "       WHERE ReleaseYear = '1990');";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            System.out.println("Processing Results");

            System.out.println(String.format("%-20s | %-30s | %-20s | %-20s",
                    "Team Name", "Song Title", "Artist First Name", "Artist Last Name"));
            while (result.next()) {
                System.out.println(String.format("%-20s | %-30s | %-20s | %-20s",
                        result.getString("TeamName"),
                        result.getString("SongTitle"),
                        result.getString("ArtistFirstName"),
                        result.getString("ArtistLastName")));
            }
        } catch(Exception e) {
            System.out.println("Error occurred" + e.getMessage());
        }
    }
    public static void dallasCowboysAnthems(Connection con) {
        try {
            String query = "SELECT SongTitle, ArtistFirstName, ArtistLastName" +
                    " FROM Teams t Join TopSongs s ON t.SongID = s.SongID" +
                    " JOIN Features f ON f.SongID = s.SongID" +
                    " WHERE TeamName = 'Dallas Cowboys';";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            System.out.println("Processing Results");

            System.out.println(String.format("%-30s | %-20s | %-20s",
                    "Song Title", "Artist First Name", "Artist Last Name"));
            while (result.next()) {
                System.out.println(String.format("%-30s | %-20s | %-20s",
                        result.getString("SongTitle"),
                        result.getString("ArtistFirstName"),
                        result.getString("ArtistLastName")));
            }
        } catch(Exception e) {
            System.out.println("Error occurred" + e.getMessage());
        }
    }
    public static void searchByArtist(Scanner scan, Connection con) {
        try {
            String artistName;
            System.out.print("Enter an artist's first name: ");
            artistName = scan.nextLine();
            String query = "SELECT SongTitle, TeamName" +
                    " FROM Features f JOIN TopSongs s ON f.SongID = s.SongID" +
                    " JOIN Teams t ON t.SongID = s.SongID" +
                    " WHERE ArtistFirstName = ?;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, artistName);
            ResultSet result = stmt.executeQuery();
            System.out.println("Processing Results");

            System.out.println(String.format("%-30s | %-20s", "Song Title", "Team Name"));
            while (result.next()) {
                System.out.println(String.format("%-30s | %-20s",
                        result.getString("SongTitle"),
                        result.getString("TeamName")));
            }
        } catch(Exception e) {
            System.out.println("Error occurred" + e.getMessage());
        }
    }
    public static int menu(Scanner scan) {
        System.out.println("Welcome, choose an option below!");
        System.out.println("Enter a coach's name to show their careers wins and the number of teams they've trained. (Enter 1)");
        System.out.println("Enter a rank to display the stats and anthems of teams. (Enter 2)");
        System.out.println("Show all coaches present in the current season and their favorite song. (Enter 3)");
        System.out.println("Show all teams whose anthem was released in 1990. (Enter 4)");
        System.out.println("Show all previous and current Dallas Cowboys anthems. (Enter 5)");
        System.out.println("Enter an artist's name to view all the teams with an anthem released by them. (Enter 6)");
        System.out.println("Exit (Enter 0)");
        System.out.println();
        System.out.print("Enter Choice Here: ");
        int choice = 0;
        String userInput = scan.nextLine();
        try {
            choice = Integer.parseInt(userInput);
        } catch(Exception e) {
            System.exit(-1);
        }
        return choice;
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        loadJDBCDriver();
        try {
            System.out.println("Starting Connection........");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://161.35.177.175:3306/dbalexj?noAccessToProcedureBodies=true", "alexj", "pwd");
            System.out.println("Connection Established");
            int userInput = menu(scan);
            while (userInput != 0) {
                switch (userInput) {
                    case 1 -> coachStats(scan, con);
                    case 2 -> searchByRank(scan, con);
                    case 3 -> presentCoaches(con);
                    case 4 -> anthemsFrom1990(con);
                    case 5 -> dallasCowboysAnthems(con);
                    case 6 -> searchByArtist(scan, con);
                    default -> System.out.println("Invalid Option Entered - Try again");
                }
                System.out.println();
                userInput = menu(scan);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error establishing connection: " + e.getMessage());
        }
    }
}