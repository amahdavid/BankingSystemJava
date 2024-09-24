import java.util.List;
import java.util.Scanner;

public class BankingMenu {
    private Scanner scanner;
    private User loggedInUser = null;

    public BankingMenu() {
        this.scanner = ScannerSingleton.getInstance();
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Banking System Menu ---");
            if (loggedInUser == null) {
                System.out.println("1. Login");
                System.out.println("2. Create User");
            } else {
                System.out.println("1. Account Management");
                System.out.println("2. Transfer Funds");
                System.out.println("3. Deposit Funds");
                System.out.println("4. Withdraw Funds");
                System.out.println("5. Logout");
            }
            System.out.println("0. Exit");
            System.out.print("Please choose an option: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            if (loggedInUser == null) {
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        createUser();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                switch (choice) {
                    case 1:
                        accountManagement();
                        break;
                    case 2:
                        transferFunds();
                        break;
                    case 3:
                        depositFunds();
                        break;
                    case 4:
                        withdrawFunds();
                        break;
                    case 5:
                        logout();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } while (choice != 0);
    }

    private void login() {
        System.out.println("\n---- Login ----");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = MyDatabase.findUser(email);
        if (user != null && user.login(email, password)) {
            loggedInUser = user;
            System.out.println("Login successful. Welcome, " + loggedInUser.getEmail());
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private void logout() {
        System.out.println("Logging out...");
        loggedInUser = null;
    }

    private void createUser() {
        System.out.println("\n---- Create a new user ----");
        System.out.print("Email: ");
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
        System.out.println("\nTransfer Funds functionality is not yet implemented.");
        // will need the recipient email, will be simulating interac
    }

    private void depositFunds() {
        System.out.println("\nDeposit Funds functionality is not yet implemented.");
    }

    private void withdrawFunds() {
        System.out.println("\nNot implemented yet");
    }

    private void createAccount() {
        System.out.println("\n--- Create New Account ---");
        System.out.print("Enter Account Type (Savings/Checking/Business): ");
        String accountType = scanner.nextLine();

        Account account;
        switch (accountType.toLowerCase()) {
            case "savings":
                account = new SavingsAccount(loggedInUser.getUserID(), accountType);
                break;
            case "checking":
                account = new CheckingAccount(loggedInUser.getUserID(), accountType);
                break;
            case "business":
                account = new BusinessAccount(loggedInUser.getUserID(), accountType);
                break;
            default:
                System.out.println("Invalid account type. Please try again.");
                return;
        }

        boolean isAccountCreated = MyDatabase.createAccount(loggedInUser.getUserID(), account);

        if (isAccountCreated) {
            System.out.println("Account created successfully for " + loggedInUser.getEmail());
        } else {
            System.out.println("Failed to create account. Please try again.");
        }
    }

    private void deleteAccount() {
        System.out.println("\n--- Delete Account ---");
        System.out.print("Enter Account Type to Delete (Savings/Checking/Business): ");
        String accountType = scanner.nextLine();

        String accountId = MyDatabase.getAccountIdByUserAndType(loggedInUser.getUserID(), accountType);

        if (accountId == null) {
            System.out.println("Account not found for user: " + loggedInUser.getEmail());
            return;
        }

        boolean isAccountDeleted = MyDatabase.deleteAccount(accountId);

        if (isAccountDeleted) {
            System.out.println("Account deleted successfully for " + loggedInUser.getEmail());
        } else {
            System.out.println("Failed to delete account. Please try again.");
        }
    }

    private void displayAccounts() {
        System.out.println("\n--- Display All Accounts ---");
        List<Account> accounts = MyDatabase.getAccountsByUserId(loggedInUser.getUserID());
        if (accounts.isEmpty()) {
            System.out.println("No accounts found for user: " + loggedInUser.getEmail());
        } else {
            System.out.println("Accounts for user " + loggedInUser.getEmail() + ":");
            for (Account account : accounts) {
                System.out.println("Account ID: " + account.getAccountID() + ", Type: "
                        + account.getAccountType() + ", Balance: " + account.getBalance());
            }
        }
    }

    private void accountManagement() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
        int choice;

        do {
            System.out.println("\n--- Account Management ---");
            System.out.println("1. Create Account");
            System.out.println("2. Delete Account");
            System.out.println("3. Display All Accounts");
            System.out.println("4. Return to Main Menu");
            System.out.print("Please choose an option: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deleteAccount();
                    break;
                case 3:
                    displayAccounts();
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
}
