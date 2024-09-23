public abstract class Account {
    private String accountID;
    private double balance;
    private String accountHolder;
    private String accountType;

    public Account(String accountID, String accountHolder, String accountType) {
        this.accountID = accountID;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        balance += amount;
        setBalance(balance);
    }

    public void withdraw(double amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient funds");
        }
        balance -= amount;
        System.out.println("Withdrew: " + amount);
    }

    public abstract void transfer(Account recipient, double amount) throws Exception;

    public String getAccountID() {return accountID;}

    public void setAccountID(String accountID) {this.accountID = accountID;}

    public double getBalance() {return balance;}

    public void setBalance(double balance) {this.balance = balance;}

    public String getAccountHolder() {return accountHolder;}

    public void setAccountHolder(String accountHolder) {this.accountHolder = accountHolder;}

    public String getAccountType() {return accountType;}

    public void setAccountType(String accountType) {this.accountType = accountType;}
}
