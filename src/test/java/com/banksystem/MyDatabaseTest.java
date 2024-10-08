package test.java.com.banksystem;

import main.java.com.banksystem.Account;
import main.java.com.banksystem.MyDatabase;
import main.java.com.banksystem.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MyDatabaseTest {

    private User validUser;
    private Account validSavingsAccount;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() throws SQLException {
        validUser = new User(UUID.randomUUID().toString(), "test@example.com", "password123", "Customer");
        validSavingsAccount = new Account(validUser.getUserID(), "Savings", 100.0);
        Connection connection = MyDatabase.getConnection();
        testUtils = new TestUtils(connection);
        testUtils.cleanupDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {testUtils.cleanupDatabase();}

    @Test
    void createUser_ValidData_Success()
    {
        boolean result = MyDatabase.createUser(validUser);
        assertTrue(result, "User should be created successfully");
        User createdUser = MyDatabase.findUser(validUser.getEmail());
        assertNotNull(createdUser, "User should exist in the database after creation");
        assertEquals(validUser.getEmail(), createdUser.getEmail(), "Email should match the created user");
    }

    @Test
    void createUser_InvalidData_Exception()
    {
        User invalidTestUser = new User("testUserIdInvalid", null, "password123", "Customer");
        boolean result = MyDatabase.createUser(invalidTestUser);
        assertFalse(result, "User creation should fail with invalid data");
        User createdUser = MyDatabase.findUser(invalidTestUser.getEmail());
        assertNull(createdUser, "User should not exist in the database with invalid data");
    }

    @Test
    void findUser_ValidUser_Success()
    {
        MyDatabase.createUser(validUser);
        User foundUser = MyDatabase.findUser(validUser.getEmail());
        assertNotNull(foundUser, "User should be found in the database");
        assertEquals(validUser.getUserID(), foundUser.getUserID(), "User ID should match");
        assertEquals(validUser.getEmail(), foundUser.getEmail(), "Email should match");
        assertEquals(validUser.getPassword(), foundUser.getPassword(), "Password should match");
        assertEquals(validUser.getRole(), foundUser.getRole(), "Role should match");
    }

    @Test
    void findUser_UserDoesNotExist_Exception()
    {
        User foundUser = MyDatabase.findUser("nonexistent@example.com");
        assertNull(foundUser, "User should not be found in the database");
    }

    @Test
    void createAccount_ValidData_Success()
    {
        MyDatabase.createUser(validUser);
        boolean result = MyDatabase.createAccount(validUser, validSavingsAccount);
        assertTrue(result, "Account should be created successfully");
        Account foundAccount = MyDatabase.getAccountIdByUserAndType(validUser, validSavingsAccount.getAccountType());
        assertNotNull(foundAccount, "Account should be found in the database");
        assertEquals(validSavingsAccount.getAccountID(), foundAccount.getAccountID(), "Account ID should match");
        assertEquals(validSavingsAccount.getAccountType(), foundAccount.getAccountType(), "Account type should match");
        assertEquals(validSavingsAccount.getBalance(), foundAccount.getBalance(), "Balance should match");
    }

    @Test
    void createAccount_InvalidData_Exception()
    {
        MyDatabase.createUser(validUser);
        Account invalidAccount = new Account(null, null, 0);
        boolean result = MyDatabase.createAccount(validUser, invalidAccount);
        assertFalse(result, "Account creation should fail with invalid data");
    }

    @Test
    void deleteAccount_ValidData_Success()
    {
        MyDatabase.createUser(validUser);
        boolean createResult = MyDatabase.createAccount(validUser, validSavingsAccount);
        assertTrue(createResult, "Account should be created successfully");
        Account foundAccount = MyDatabase.getAccountIdByUserAndType(validUser, validSavingsAccount.getAccountType());
        assertNotNull(foundAccount, "Account should be found in the database");
        assertEquals(validSavingsAccount.getAccountID(), foundAccount.getAccountID(), "Account ID should match");
        assertEquals(validSavingsAccount.getAccountType(), foundAccount.getAccountType(), "Account type should match");
        assertEquals(validSavingsAccount.getBalance(), foundAccount.getBalance(), "Balance should match");

        boolean deleteResult = MyDatabase.deleteAccount(validSavingsAccount.getAccountID());
        Account deletedAccount = MyDatabase.getAccountIdByUserAndType(validUser, validSavingsAccount.getAccountType());
        assertTrue(deleteResult, "Account is deleted");
        assertNull(deletedAccount, "Account does not exist in DB");
    }
    @Test
    void deleteAccount_InvalidData_Exception()
    {
        String nonExistentAccountId = "invalid_account_id";
        boolean deleteResult = MyDatabase.deleteAccount(nonExistentAccountId);
        assertFalse(deleteResult, "Deletion of non-existent account should return false");
    }
    @Test
    void getAccountIdByUserAndType_ValidUserAndType_Success()
    {
        MyDatabase.createUser(validUser);
        MyDatabase.createAccount(validUser, validSavingsAccount);

        Account retrievedAccount = MyDatabase.getAccountIdByUserAndType(validUser, validSavingsAccount.getAccountType());

        assertNotNull(retrievedAccount, "Account should be found in the database");
        assertEquals(validSavingsAccount.getAccountID(), retrievedAccount.getAccountID(), "Account ID should match");
        assertEquals(validSavingsAccount.getAccountType(), retrievedAccount.getAccountType(), "Account type should match");
        assertEquals(validSavingsAccount.getBalance(), retrievedAccount.getBalance(), "Balance should match");
        assertEquals(validUser.getUserID(), retrievedAccount.getUserID(), "User ID should match");
    }
    @Test
    void getAccountIdByUserAndType_ValidUserAndInvalidType_Exception()
    {
        MyDatabase.createUser(validUser);
        MyDatabase.createAccount(validUser, validSavingsAccount);
        Account invalidAccount = new Account(validUser.getUserID(), null, 0);
        Account retrievedAccount = MyDatabase.getAccountIdByUserAndType(validUser, invalidAccount.getAccountType());
        assertNull(retrievedAccount, "No account should be found for the invalid account type");
    }

    @Test
    void getAccountIdByUserAndType_InvalidUserAndValidType_Exception()
    {
        User invalidUser = new User("testUserIdInvalid", null, "password123", "Customer");
        MyDatabase.createUser(validUser);
        MyDatabase.createAccount(validUser, validSavingsAccount);
        Account retrievedAccount = MyDatabase.getAccountIdByUserAndType(invalidUser, validSavingsAccount.getAccountType());
        assertNull(retrievedAccount, "No account should be found for the invalid user ID");
    }

    @Test
    void getAccountsByUserId_ValidUserID_Success()
    {
        MyDatabase.createUser(validUser);
        MyDatabase.createAccount(validUser, validSavingsAccount);

        List<Account> accounts = MyDatabase.getAccountsByUserId(validUser.getUserID());
        assertNotNull(accounts, "The account list should not be null");
        assertEquals(1, accounts.size(), "There should be 3 accounts associated with the user");

        assertEquals(validSavingsAccount.getAccountID(), accounts.getFirst().getAccountID(), "Account ID should match for the first account");
        assertEquals(validSavingsAccount.getAccountType(), accounts.getFirst().getAccountType(), "Account type should match for the first account");
        assertEquals(validSavingsAccount.getBalance(), accounts.getFirst().getBalance(), "Balance should match for the first account");
    }

    @Test
    void getAccountsByUserId_InvalidUserID_Exception()
    {
        User invalidUser = new User("testUserIdInvalid", null, "password123", "Customer");
        List<Account> accounts = MyDatabase.getAccountsByUserId(invalidUser.getUserID());
        assertNotNull(accounts, "The account list should not be null, but should be empty");
        assertEquals(0, accounts.size(), "No accounts should be returned for an invalid user ID");
    }

    @Test
    void updateAccountBalance_ValidAccount_Success()
    {
        MyDatabase.createUser(validUser);
        MyDatabase.createAccount(validUser, validSavingsAccount);
        double newBalance = 150.0;
        MyDatabase.updateAccountBalance(validSavingsAccount, newBalance);
        Account updatedAccount = MyDatabase.getAccountIdByUserAndType(validUser, validSavingsAccount.getAccountType());
    }

    @Test
    void updateAccountBalance_InvalidAccount_Exception()
    {
        Account invalidAccount = new Account(null, null, 0);
        double newBalance = 150.0;
        MyDatabase.updateAccountBalance(invalidAccount, newBalance);
        Account account = MyDatabase.getAccountIdByUserAndType(validUser, validSavingsAccount.getAccountType());
        assertNull(account, "Account should not exist, and no update should have occurred");
    }
}