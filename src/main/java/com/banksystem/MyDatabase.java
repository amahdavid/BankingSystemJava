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

    public static Account getAccountIdByUserAndType(User user, String accountType) {
        String query = "SELECT account_id, balance, user_id, account_type FROM accounts WHERE user_id = ? AND account_type = ?";

        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getUserID());
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

    public static List<Account> getAccountsByUserId(String userID) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String accountID = rs.getString("account_id");
                String accountType = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                Account account = new Account(accountID, balance, userID, accountType);
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving accounts: " + e.getMessage());
        }
        return accounts;
    }

    public static void updateAccountBalance(Account account, double newBalance) {
        String query = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            account.setBalance(newBalance);
            stmt.setDouble(1, account.getBalance());
            stmt.setString(2, account.getAccountID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating account balance: " + e.getMessage());
        }
    }

    public static boolean insertTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_date, sender_account_id, recipient_account_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getTransactionID());
            statement.setString(2, transaction.getTransactionType());
            statement.setDouble(3, transaction.getAmount());
            statement.setTimestamp(4, new java.sql.Timestamp(transaction.getDate().getTime()));
            statement.setString(5, transaction.getSender());
            statement.setString(6, transaction.getRecipient());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
            return false;
        }
    }

    public static User getUserByAccountId(String accountId) {
        User user = null;
        String sql = "SELECT u.id, u.email, u.password, u.role FROM users u "
                + "JOIN accounts a ON u.id = a.user_id WHERE a.account_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userId = resultSet.getString("id");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");

                    user = new User(userId, email, password, role);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by account ID: " + e.getMessage());
        }
        return user;
    }

    public static List<Transaction> getTransactionHistoryByAccount(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, transaction_type, amount, transaction_date, sender_account_id, recipient_account_id "
                + "FROM transactions WHERE sender_account_id = ? OR recipient_account_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountId);
            statement.setString(2, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String transactionID = resultSet.getString("transaction_id");
                    String transactionType = resultSet.getString("transaction_type");
                    double amount = resultSet.getDouble("amount");
                    Date transactionDate = resultSet.getDate("transaction_date");
                    String senderAccountId = resultSet.getString("sender_account_id");
                    String recipientAccountId = resultSet.getString("recipient_account_id");

                    // Fetch sender and recipient as Users
                    User sender = MyDatabase.getUserByAccountId(senderAccountId);
                    User recipient = (recipientAccountId != null) ? MyDatabase.getUserByAccountId(recipientAccountId) : null;

                    // Create a transaction with User objects for sender and recipient
                    Transaction transaction = new Transaction(
                            transactionID,
                            transactionType,
                            amount,
                            transactionDate,
                            sender,
                            recipient
                    );
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
        return transactions;
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
