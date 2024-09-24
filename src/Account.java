import java.util.UUID;

public class Account {
    private String accountID;
    private double balance;
    private String userID;
    private String accountType;

    public Account(String userID, String accountType) {
        this.accountID = UUID.randomUUID().toString();
        this.userID = userID;
        this.accountType = accountType;
        this.balance = 0.0;
    }

    public Account(String accountID, double balance, String userID, String accountType) {
        this.accountID = accountID;
        this.balance = balance;
        this.userID = userID;
        this.accountType = accountType;
    }

    // figure out what you want to do with this function
    public void deposit(double amount) {
        balance += amount;
        setBalance(balance);
    }

    // figure out what you want to do with this function
    public void withdraw(double amount) throws ExceptionHandler {
        if (amount > balance) {
            throw new ExceptionHandler("Insufficient funds");
        }
        balance -= amount;
        System.out.println("Withdrew: " + amount);
    }

    // figure out what you want to do with this function
    public void transfer(Account recipient, double amount) throws Exception {}

    public String getAccountID() {return accountID;}

    public void setAccountID(String accountID) {this.accountID = accountID;}

    public double getBalance() {return balance;}

    public void setBalance(double balance) {this.balance = balance;}

    public String getAccountType() {return accountType;}

    public void setAccountType(String accountType) {this.accountType = accountType;}

    public String getUserID() {return userID;}
}
