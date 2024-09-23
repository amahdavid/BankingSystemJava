import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---- Create a new user ----");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Role (Admin/Customer): ");
        String role = scanner.nextLine();

        // Create a new User object
        User newUser = new User(username, password, role);
        // Attempt to insert the user into the database
        boolean isCreated = MyDatabase.createUser(newUser);
        if (isCreated) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("Failed to create user.");
        }

        scanner.close();
    }
}