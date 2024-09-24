import java.sql.*;

public class MyDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/BankManagementProject";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static boolean createUser(User user) {
        String sql = "INSERT INTO users (userID, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUserID());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword()); // Consider hashing this
            preparedStatement.setString(4, user.getRole());
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0; // Return true if user was inserted
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    public static User findUser(String userIdOrUsername) {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ? OR username = ?";
        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userIdOrUsername); // Bind user_id
            stmt.setString(2, userIdOrUsername); // Bind username

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Extract user details
                String id = rs.getString("id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                // You can expand to retrieve more fields as necessary

                user = new User(id, username, role);
            }
        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return user;
    }

    public static boolean createAccount(String userId, Account account) {
        String query = "INSERT INTO accounts (account_id, user_id, account_type, balance) VALUES (?, ?, ?, ?)";
        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, account.getAccountID());    // Set account UUID
            stmt.setString(2, userId);                    // Set user UUID (foreign key)
            stmt.setString(3, account.getAccountType());  // Set account type (Savings, Checking, etc.)
            stmt.setDouble(4, account.getBalance());      // Set initial balance (usually 0.0)

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

            stmt.setString(1, accountId);  // Set account UUID

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
        return false;
    }

    public static String getAccountIdByUserAndType(String userId, String accountType) {
        String accountId = null;
        String query = "SELECT account_id FROM accounts WHERE user_id = ? AND account_type = ?";

        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userId);      // Bind user ID
            stmt.setString(2, accountType); // Bind account type

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                accountId = rs.getString("account_id"); // Get the account ID
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account: " + e.getMessage());
        }

        return accountId;
    }


    public static void main(String[] args) {
    // Check the database connection
    Connection connection = getConnection();
    if (connection != null) {
        // If the connection is successful, close it
        try {
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
}
