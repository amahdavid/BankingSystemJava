package main.java.com.banksystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/BankManagementProject";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static boolean createUser(User user) {
        String sql = "INSERT INTO users (id, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUserID());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword()); // Consider hashing this
            preparedStatement.setString(4, user.getRole());
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    public static User findUser(String email) {
        String query = "SELECT id, email, password, role FROM users WHERE email = ?";
        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String userEmail = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");

                return new User(id, userEmail, password, role);
            }
        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    public static boolean createAccount(User user, Account account) {
        if (findUser(user.getEmail()) == null) {
            System.out.println("User does not exist");
            return false;
        }
        String query = "INSERT INTO accounts (account_id, user_id, account_type, balance) VALUES (?, ?, ?, ?)";
        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, account.getAccountID());
            stmt.setString(2, user.getUserID());
            stmt.setString(3, account.getAccountType());
            stmt.setDouble(4, account.getBalance());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteAccount(String accountId) {
        String query = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accountId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
        return false;
    }

    public static Account getAccountIdByUserAndType(String userId, String accountType) {
        String query = "SELECT account_id, balance, user_id, account_type FROM accounts WHERE user_id = ? AND account_type = ?";

        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.setString(2, accountType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String accountId = rs.getString("account_id");
                String userID = rs.getString("user_id");
                String accType = rs.getString("account_type");
                double balance = rs.getDouble("balance");

                return new Account(accountId, balance, userID, accType);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account: " + e.getMessage());
        }
        return null;
    }

    public static List<Account> getAccountsByUserId(String userId) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String accountID = rs.getString("account_id");
                String accountType = rs.getString("account_type");

                // figure out if you want a default value or let the user decide
                double balance = rs.getDouble("balance");

                Account account = new Account(accountID, balance, userId, accountType);
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving accounts: " + e.getMessage());
        }
        return accounts;
    }

    public static void updateAccountBalance(String accountID, double newBalance) {
        String query = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating account balance: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
