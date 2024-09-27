package test.java.com.banksystem;

import main.java.com.banksystem.BankingMenu;
import main.java.com.banksystem.ExceptionHandler;
import main.java.com.banksystem.MyDatabase;
import main.java.com.banksystem.ScannerSingleton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
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

    @Before
    public void initialization() throws Exception
    {
        Scanner scanner = ScannerSingleton.getInstance();
        Connection connection = MyDatabase.getConnection();
        bankingMenu = new BankingMenu(scanner);
        testUtils = new TestUtils(connection);
        testUtils.insertTestUser(testUserId, testEmail, testPassword, testRole);
    }

    @After
    public void tearDown() throws Exception {testUtils.cleanupDatabase();}

    @Test
    public void testLogin_ValidCredentials() throws ExceptionHandler
    {
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
    public void testLogin_InvalidCredentials() throws ExceptionHandler
    {
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
    public void testCreateUser_ValidCredentials() {

    }
}