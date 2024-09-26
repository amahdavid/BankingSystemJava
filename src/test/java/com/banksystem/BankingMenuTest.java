package test.java.com.banksystem;

import main.java.com.banksystem.BankingMenu;
import main.java.com.banksystem.ExceptionHandler;
import main.java.com.banksystem.MyDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BankingMenuTest {
    private BankingMenu bankingMenu;
    private Connection connection;

    private String testUserId = UUID.randomUUID().toString();
    private String testEmail = "testuser@example.com";
    private String testPassword = "password123"; // Consider using plain text only for testing
    private String testRole = "Customer";

    @Before
    public void initialization() throws Exception {
        bankingMenu = new BankingMenu();
        connection = MyDatabase.getConnection();
        cleanupDatabase();
        insertTestUser();
    }

    @After
    public void tearDown() throws Exception {
        // Clean up database after each test
        cleanupDatabase();
        connection.close();
    }

    @Test
    public void testLogin_ValidCredentials() throws ExceptionHandler {

        // Simulate user input for login
        String simulatedInput = "1" + testEmail + testPassword + "0";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        bankingMenu.displayMenu();
        assertEquals(testEmail, bankingMenu.getLoggedInUser().getEmail());
    }

    private void insertTestUser() throws Exception {
        String sql = "INSERT INTO users (id, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, testUserId);
            preparedStatement.setString(2, testEmail);
            preparedStatement.setString(3, testPassword); // This should ideally be hashed in production
            preparedStatement.setString(4, testRole);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting test user: " + e.getMessage());
        }
    }

    private void cleanupDatabase() throws Exception {
        String cleanupUsersSQL = "DELETE FROM users";
        String cleanupTransactionsSQL = "DELETE FROM transactions";
        String cleanupAccountsSQL = "DELETE FROM accounts";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(cleanupAccountsSQL);
            stmt.executeUpdate(cleanupTransactionsSQL);
            stmt.executeUpdate(cleanupUsersSQL);
        } catch (SQLException e) {
            System.out.println("Error cleaning up database: " + e.getMessage());
        }
    }
}