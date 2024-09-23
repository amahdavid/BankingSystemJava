import java.util.Scanner;

public class BankingMenu {
    private Scanner scanner;

    public BankingMenu() {
        this.scanner = ScannerSingleton.getInstance(); // Use singleton scanner
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Banking System Menu ---");
            System.out.println("1. Create User");
            System.out.println("2. Transfer Funds");
            System.out.println("3. Deposit Funds");
            System.out.println("4. Account Management");
            System.out.println("5. Exit");
            System.out.print("Please choose an option: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    transferFunds();
                    break;
                case 3:
                    depositFunds();
                    break;
                case 4:
                    accountManagement();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private void createUser() {
        System.out.println("---- Create a new user ----");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Role (Admin/Customer): ");
        String role = scanner.nextLine();

        User newUser = new User(username, password, role);
        boolean isCreated = MyDatabase.createUser(newUser);
        if (isCreated) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("Failed to create user.");
        }
    }

    private void transferFunds() {
        System.out.println("Transfer Funds functionality is not yet implemented.");
    }

    private void depositFunds() {
        System.out.println("Deposit Funds functionality is not yet implemented.");
    }

    private void accountManagement() {
        System.out.println("Account Management functionality is not yet implemented.");
    }
}
