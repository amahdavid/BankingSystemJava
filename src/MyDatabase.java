import java.sql.*;

public class MyDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/BankManagementProject";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static boolean createUser(User user) {
        String sql = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";
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

    public static boolean createAccount(String userId, Account account) {
        String query = "INSERT INTO accounts (account_id, user_id, account_type, balance) VALUES (?, ?, ?, ?)";
        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, account.getAccountID());
            stmt.setString(2, userId);
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

    public static String getAccountIdByUserAndType(String userId, String accountType) {
        String accountId = null;
        String query = "SELECT account_id FROM accounts WHERE user_id = ? AND account_type = ?";

        try (Connection connection = MyDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.setString(2, accountType);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                accountId = rs.getString("account_id");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account: " + e.getMessage());
        }

        return accountId;
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
