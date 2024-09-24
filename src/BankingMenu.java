import java.util.Scanner;

public class BankingMenu {
    private Scanner scanner;

    public BankingMenu() {
        this.scanner = ScannerSingleton.getInstance(); // Use singleton scanner
    }

    public void displayMenu() {
        int choice;
        do {
            // might make an if condition to check if a user is an admin and will show them a different menu
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
        String role = "Customer";

        User newUser = new User(username, password, role);
        boolean isCreated = MyDatabase.createUser(newUser);
        if (isCreated) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("Failed to create user.");
        }

        System.out.print("Press Enter to return to the main menu...");
        scanner.nextLine();
    }

    private void transferFunds() {
        System.out.println("Transfer Funds functionality is not yet implemented.");
        // will need the recipient email, will be simulating interac
    }

    private void depositFunds() {
        System.out.println("Deposit Funds functionality is not yet implemented.");
    }

    private void createAccount() {
        System.out.println("\n--- Create New Account ---");
        System.out.print("Enter Username for Account: ");
        String username = scanner.nextLine();

        // Check if the user exists in the database
        User user = MyDatabase.findUser(username); // Implement this method to search for a user
        if (user == null) {
            System.out.println("User not found. Please create a user profile first.");
            return;
        }

        System.out.print("Enter Account Type (Savings/Checking/Business): ");
        String accountType = scanner.nextLine();

        // Here, instantiate the appropriate Account type based on the accountType
        Account account = null;
        switch (accountType.toLowerCase()) {
            case "savings":
                account = new SavingsAccount(user.getUserID(), accountType); // Assuming you have a SavingsAccount class
                break;
            case "checking":
                account = new CheckingAccount(user.getUserID(), accountType); // Assuming you have a CheckingAccount class
                break;
            case "business":
                account = new BusinessAccount(user.getUserID(), accountType); // Assuming you have a BusinessAccount class
                break;
            default:
                System.out.println("Invalid account type. Please try again.");
                return;
        }

        // Call the createAccount method in MyDatabase with user ID and account object
        boolean isAccountCreated = MyDatabase.createAccount(user.getUserID(), account);

        if (isAccountCreated) {
            System.out.println("Account created successfully for " + user.getUsername());
        } else {
            System.out.println("Failed to create account. Please try again.");
        }
    }

    private void deleteAccount() {
        System.out.println("\n--- Delete Account ---");
        System.out.print("Enter Username for Account: ");
        String username = scanner.nextLine();

        // Check if the user exists in the database
        User user = MyDatabase.findUser(username); // Implement this method to search for a user
        if (user == null) {
            System.out.println("User not found. Please try again.");
            return;
        }

        System.out.print("Enter Account Type to Delete (Savings/Checking/Business): ");
        String accountType = scanner.nextLine();

        // Retrieve the account ID by user ID and account type
        String accountId = MyDatabase.getAccountIdByUserAndType(user.getUserID(), accountType);

        if (accountId == null) {
            System.out.println("Account not found for user: " + user.getUsername());
            return;
        }

        // Delete the account using the account ID
        boolean isAccountDeleted = MyDatabase.deleteAccount(accountId);

        if (isAccountDeleted) {
            System.out.println("Account deleted successfully for " + user.getUsername());
        } else {
            System.out.println("Failed to delete account. Please try again.");
        }
    }

    private void accountManagement() {
        int choice;
        do {
            System.out.println("\n--- Account Management ---");
            System.out.println("1. Create Account");
            System.out.println("2. Delete Account");
            System.out.println("3. Return to Main Menu");
            System.out.print("Please choose an option: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deleteAccount();
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }
}
