package test.java.com.banksystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtils {
    private Connection connection;

    public TestUtils(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a test user into the database.
     *
     * @param testUserId The ID of the test user.
     * @param testEmail The email of the test user.
     * @param testPassword The password of the test user.
     * @param testRole The role of the test user.
     * @throws SQLException If an SQL error occurs during the insertion.
     */
    public void insertTestUser(String testUserId, String testEmail, String testPassword, String testRole) throws SQLException {
        String sql = "INSERT INTO users (id, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, testUserId);
            preparedStatement.setString(2, testEmail);
            preparedStatement.setString(3, testPassword); // This should be hashed in production
            preparedStatement.setString(4, testRole);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting test user: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cleans up the database by deleting all entries in the users, transactions, and accounts tables.
     *
     * @throws SQLException If an SQL error occurs during the cleanup.
     */
    public void cleanupDatabase() throws SQLException {
        String cleanupUsersSQL = "DELETE FROM users";
        String cleanupTransactionsSQL = "DELETE FROM transactions";
        String cleanupAccountsSQL = "DELETE FROM accounts";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(cleanupAccountsSQL);
            stmt.executeUpdate(cleanupTransactionsSQL);
            stmt.executeUpdate(cleanupUsersSQL);
        } catch (SQLException e) {
            System.out.println("Error cleaning up database: " + e.getMessage());
            throw e;
        }
    }
}
