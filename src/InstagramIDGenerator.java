/*
 * Copyright (c) 2024 G Varun Tyagarayan
 *
 * This software is licensed under the MIT License.
 * For details, see the LICENSE file in the root directory of this project.
 */

import java.sql.*;

public class InstagramIDGenerator {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/virtual_db1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "use you pass";

    public static void main(String[] args) {
        // User ID to insert
        String userId = "mkdir_070";

        insertUser(userId);

        fetchAndDisplaySeqCounter();

        clearDatabase();
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
            System.err.println("SQL Error: " + e.getMessage());
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

            String selectQuery = "SELECT * FROM seq_counter";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            System.out.println("Current seq_counter table contents:");
            while (rs.next()) {
                int shardId = rs.getInt("shard_id");
                long seqId = rs.getLong("seq_id");
                String userInsertion = rs.getString("userInsertion");

                System.out.printf("Shard ID: %d, Seq ID: %d, User: %s%n", shardId, seqId, userInsertion);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } finally {
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
