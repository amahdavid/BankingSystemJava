package main.java.com.banksystem;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BankingMenu {
    private final Scanner scanner;
    private User loggedInUser = null;

    public BankingMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayMenu() throws ExceptionHandler {
        int choice;
        do {
            System.out.println("\n--- Banking System Menu ---");
            printMenuOptions();
            System.out.print("Please choose an option: ");

            choice = getUserChoice();

            if (loggedInUser == null) {
                handleGuestMenuChoice(choice);
            } else {
                handleUserMenuChoice(choice);
            }

        } while (choice != 0);
    }

    private void printMenuOptions() {
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
    }

    private int getUserChoice() {
        int choice = -1;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("InputMismatchException: " + e.getMessage());
            scanner.nextLine();
        }
        return choice;
    }

    private void handleGuestMenuChoice(int choice) {
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
    }

    private void handleUserMenuChoice(int choice) throws ExceptionHandler {
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

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private void login() {
        System.out.println("\n---- Login ----");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = MyDatabase.findUser(email.toLowerCase());

        if (user == null) {
            System.out.println("No account found with the email: " + email + ". Please create an account.");
        } else if (!user.login(email.toLowerCase(), password)) {
            System.out.println("Incorrect password. Please try again.");
        } else {
            loggedInUser = user;
            System.out.println("Login successful. Welcome, " + loggedInUser.getEmail());
        }
    }

    private void logout() { // FUNCTIONAL
        System.out.println("Logging out...");
        loggedInUser = null;
    }

    private void createUser() { // FUNCTIONAL
        System.out.println("\n---- Create a new user ----");
        System.out.print("Email: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String role = "Customer";

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Failed to create user. Email and password cannot be empty.");
            return;
        }

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
            System.out.println("4. View Transaction History");
            System.out.println("5. Return to main menu");
            System.out.print("Please choose an option: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deleteAccount(); // might want to make this to only admins
                    break;
                case 3:
                    displayAccounts();
                    break;
                case 4:
                    displayTransactionHistory();
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    private void createAccount() { // FUNCTIONAL
        System.out.println("\n--- Create New Account ---");
        System.out.print("Enter Account Type (Savings/Checking/Business): ");
        String accountType = scanner.nextLine();
        System.out.print("Enter how much to deposit: ");
        double balance = scanner.nextDouble();

        Account account;
        switch (accountType.toLowerCase()) {
            case "savings":
                account = new SavingsAccount(loggedInUser.getUserID(), accountType, balance);
                break;
            case "checking":
                account = new CheckingAccount(loggedInUser.getUserID(), accountType, balance);
                break;
            case "business":
                account = new BusinessAccount(loggedInUser.getUserID(), accountType, balance);
                break;
            default:
                System.out.println("Invalid account type. Please try again.");
                return;
        }
        boolean isAccountCreated = MyDatabase.createAccount(loggedInUser, account);

        if (isAccountCreated) {
            System.out.println("Account created successfully for " + loggedInUser.getEmail());
            Transaction transaction = new Transaction("Deposit", balance, loggedInUser, null);
            transaction.saveTransaction();
        } else {
            System.out.println("Failed to create account. Please try again.");
        }
    }

    private void transferFunds() { // FUNCTIONAL
        if (loggedInUser == null) {
            System.out.println("Please log in first");
            return;
        }

        System.out.println("\n---- Transfer Funds ----");
        System.out.print("Enter recipient's email: ");
        String recipientEmail = scanner.nextLine();

        User recipient = MyDatabase.findUser(recipientEmail.toLowerCase());
        if (recipient == null) {
            System.out.println("Recipient not found");
            return;
        }
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        Account senderAccount = MyDatabase.getAccountIdByUserAndType(loggedInUser, "Checking");
        Account recipientAccount = MyDatabase.getAccountIdByUserAndType(recipient, "Checking");

        if (senderAccount == null || recipientAccount == null) {
            System.out.println("Account not found.");
            return;
        }
        try {
            senderAccount.transfer(recipientAccount, amount);
            MyDatabase.updateAccountBalance(senderAccount, senderAccount.getBalance());
            MyDatabase.updateAccountBalance(recipientAccount, recipientAccount.getBalance());
            System.out.println("Transfer of " + amount + " to " + recipientEmail + " successful!");
            Transaction transaction = new Transaction("Transfer", amount, loggedInUser, recipient);
            transaction.saveTransaction();
        } catch (ExceptionHandler e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    private void depositFunds() throws ExceptionHandler { // FUNCTIONAL
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.println("\n---- Deposit Funds ----");
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter account to deposit to: ");
        String accountType = scanner.nextLine();

        Account userAccount = MyDatabase.getAccountIdByUserAndType(loggedInUser, accountType);
        if (userAccount == null) {
            System.out.println("Account not found.");
            return;
        }
        userAccount.deposit(amount);
        MyDatabase.updateAccountBalance(userAccount, userAccount.getBalance());
        System.out.println("Deposited " + amount + " successfully!");
        Transaction transaction = new Transaction("Deposit", amount, loggedInUser, null);
        transaction.saveTransaction();
    }

    private void withdrawFunds() { // FUNCTIONAL
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.println("\n---- Withdraw Funds ----");
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter account to withdraw from: ");
        String accountType = scanner.nextLine();

        Account userAccount = MyDatabase.getAccountIdByUserAndType(loggedInUser, accountType);
        if (userAccount == null) {
            System.out.println("Account not found.");
            return;
        }

        try {
            userAccount.withdraw(amount);
            MyDatabase.updateAccountBalance(userAccount, userAccount.getBalance());
            System.out.println("Withdrew " + amount + " successfully!");
            Transaction transaction = new Transaction("Withdrawal", amount, loggedInUser, null);
            transaction.saveTransaction();
        } catch (ExceptionHandler e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    private void deleteAccount() { // FUNCTIONAL
        System.out.println("\n--- Delete Account ---");
        System.out.print("Enter Account Type to Delete (Savings/Checking/Business): ");
        String accountType = scanner.nextLine();

        Account accountToBeDeleted = MyDatabase.getAccountIdByUserAndType(loggedInUser, accountType);

        if (accountToBeDeleted == null) {
            System.out.println("Account not found for user: " + loggedInUser.getEmail());
            return;
        }

        boolean isAccountDeleted = MyDatabase.deleteAccount(accountToBeDeleted.getAccountID());

        if (isAccountDeleted) {
            System.out.println("Account deleted successfully for " + loggedInUser.getEmail());
        } else {
            System.out.println("Failed to delete account. Please try again.");
        }
    }

    private void displayAccounts() { // FUNCTIONAL
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

    private void displayTransactionHistory() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter account type (Savings/Checking/Business): ");
        String accountType = scanner.nextLine();

        Account userAccount = MyDatabase.getAccountIdByUserAndType(loggedInUser, accountType);
        if (userAccount == null) {
            System.out.println("Account not found.");
            return;
        }

        List<Transaction> transactionHistory = MyDatabase.getTransactionHistoryByAccount(userAccount.getAccountID());
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found for this account.");
        } else {
            for (Transaction transaction : transactionHistory) {
                System.out.println("Trans Date: " + transaction.getDate()
                        + ", Trans Type: " + transaction.getTransactionType()
                        + ", Sender: " + transaction.getSender()
                        + ", Recipient: " + transaction.getRecipient());
            }
        }
    }
}
