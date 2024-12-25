import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InstagramIDGeneratorForDB2 {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/virtual_db1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Tyagi#2004";

    public static void main(String[] args) {
        int userCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(userCount);

        // Submit tasks for concurrent insertion
        for (int i = 1; i <= userCount; i++) {
            String userId = "user_" + i;
            executor.execute(() -> insertUser(userId));
        }

        executor.shutdown();

        // Wait for all threads to complete
        while (!executor.isTerminated()) {
            // Do nothing, just wait
        }

        fetchAndDisplaySeqCounter();

        clearDatabase();

        System.out.println("\nAll operations completed. Database cleared.");
    }

    public static void insertUser(String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Set the session variable for user ID
            String setSessionQuery = "SET @user_id = ?";
            try (PreparedStatement sessionStmt = conn.prepareStatement(setSessionQuery)) {
                sessionStmt.setString(1, userId);
                sessionStmt.executeUpdate();
            }

            // Insert a new row into seq_counter
            String insertQuery = "INSERT INTO seq_counter (userInsertion) VALUES (?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, userId);
            stmt.executeUpdate();

            System.out.println("Insert successful for user: " + userId);

        } catch (SQLException e) {
            System.err.println("SQL Error for user " + userId + ": " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    public static void fetchAndDisplaySeqCounter() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String selectQuery = "SELECT * FROM seq_counter ORDER BY seq_id ASC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            System.out.println("\nCurrent seq_counter table contents (sorted by seq_id):");
            while (rs.next()) {
                int shardId = rs.getInt("shard_id");
                long seqId = rs.getLong("seq_id");
                String userInsertion = rs.getString("userInsertion");

                System.out.printf("Shard ID: %d, Seq ID: %d, User: %s%n", shardId, seqId, userInsertion);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error while fetching data: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    public static void clearDatabase() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String clearQuery = "DELETE FROM seq_counter";
            stmt = conn.createStatement();
            int rowsDeleted = stmt.executeUpdate(clearQuery);

            System.out.println("\nCleared seq_counter table. Rows deleted: " + rowsDeleted);

        } catch (SQLException e) {
            System.err.println("SQL Error while clearing data: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }
}
