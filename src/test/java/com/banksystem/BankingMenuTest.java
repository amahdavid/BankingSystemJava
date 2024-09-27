package test.java.com.banksystem;

import main.java.com.banksystem.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

import static org.junit.Assert.*;

public class BankingMenuTest {
    private BankingMenu bankingMenu;
    private TestUtils testUtils;
    private final String testUserId = UUID.randomUUID().toString();
    private final String testEmail = "testuser@example.com";
    private final String testPassword = "password123";
    private final String testRole = "Customer";
    private final String testAccountId = UUID.randomUUID().toString();
    private final String testAccountType = "Savings";
    private final double testAccountBalance = 100.89;

    @Before
    public void initialization() throws Exception {
        Scanner scanner = ScannerSingleton.getInstance();
        Connection connection = MyDatabase.getConnection();
        bankingMenu = new BankingMenu(scanner);
        testUtils = new TestUtils(connection);
        testUtils.cleanupDatabase();
    }

    @After
    public void tearDown() throws Exception {
        testUtils.cleanupDatabase();
    }

    @Test
    public void testLogin_ValidCredentials_Success() throws ExceptionHandler, SQLException {
        testUtils.insertTestUser(testUserId, testEmail, testPassword, testRole);
        String simulatedInput = "1\n" + testEmail + "\n" + testPassword + "\n0\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        Scanner testScanner = new Scanner(System.in);
        bankingMenu = new BankingMenu(testScanner);
        bankingMenu.displayMenu();

        assertEquals(testEmail, bankingMenu.getLoggedInUser().getEmail());
        System.setIn(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testLogin_InvalidCredentials_Exception() throws ExceptionHandler {
        String invalidUser = "invalid@gmail.com";
        String simulatedInput = "1\n" + invalidUser + "\n" + testPassword + "\n0\n";

        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Scanner testScanner = new Scanner(System.in);
        bankingMenu = new BankingMenu(testScanner);
        bankingMenu.displayMenu();

        String expectedOutput = "No account found with the email: " + invalidUser + ". Please create an account";

        assertTrue(outContent.toString().contains(expectedOutput));
        assertNull(bankingMenu.getLoggedInUser());

        System.setIn(System.in);
        System.setOut(originalOut);
    }

    @Test
    public void testCreateUser_ValidCredentials_Success() throws ExceptionHandler {
        String simulatedInput = "2\n" + testEmail + "\n" + testPassword + "\n\n" + "1\n" + testEmail + "\n" + testPassword + "\n0\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        Scanner testScanner = new Scanner(System.in);
        bankingMenu = new BankingMenu(testScanner);
        bankingMenu.displayMenu();

        assertEquals(testEmail, bankingMenu.getLoggedInUser().getEmail());
        System.setIn(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testCreateUser_InvalidCredentials_Exception() throws ExceptionHandler {
        String simulatedInput = "2\n\n\n0\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Scanner testScanner = new Scanner(System.in);
        bankingMenu = new BankingMenu(testScanner);
        bankingMenu.displayMenu();

        String output = outputStream.toString();
        assertTrue("The output should indicate failure due to empty credentials.",
                output.contains("Failed to create user. Email and password cannot be empty."));
        System.setIn(new ByteArrayInputStream(new byte[0]));
        System.setOut(originalOut);
    }

    @Test
    public void testCreateAccount_ValidData_Success() throws SQLException, ExceptionHandler {
        testUtils.insertTestUser(testUserId, testEmail, testPassword, testRole);
        String simulatedInput = "1\n" + testEmail + "\n" + testPassword + "\n" + // Login step
                "1\n" + // Account Management
                "1\n" + // Create Account
                testAccountType + "\n" + // Account Type (using provided variable)
                testAccountBalance + "\n" + // Deposit Amount (using provided variable)
                "4\n" + // Return to Main Menu
                "0\n"; // Exit
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        Scanner testScanner = new Scanner(System.in);
        bankingMenu = new BankingMenu(testScanner);
        bankingMenu.displayMenu();

        Account createdAccount = MyDatabase.getAccountIdByUserAndType(bankingMenu.getLoggedInUser(), testAccountType);
        assertNotNull(createdAccount);
        double delta = 0.01;
        assertEquals(testAccountBalance, createdAccount.getBalance(), delta);
        System.setIn(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testCreateAccount_InvalidData_Exception() throws ExceptionHandler, SQLException {
        testUtils.insertTestUser(testUserId, testEmail, testPassword, testRole);
        String simulatedInput = "1\n" + testEmail + "\n" + testPassword + "\n" + // Login step
                "1\n" + // Account Management
                "1\n" + // Create Account
                "null\n" + // Invalid Account Type (simulate null)
                "0.0\n" + // Invalid Balance (simulate 0.0, assuming this is treated as invalid)
                "4\n" + // Return to Main Menu
                "0\n"; // Exit

        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        Scanner testScanner = new Scanner(System.in);
        bankingMenu = new BankingMenu(testScanner);
        bankingMenu.displayMenu();

        Account createdAccount = MyDatabase.getAccountIdByUserAndType(bankingMenu.getLoggedInUser(), "null");
        assertNull(createdAccount);
        System.setIn(new ByteArrayInputStream(new byte[0]));
    }
}