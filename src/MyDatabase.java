import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
}
