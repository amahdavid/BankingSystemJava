public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 500.00;

    public CheckingAccount(String userID, String accountType) {
        super(userID, accountType);
    }
    public CheckingAccount(String accountID, double balance, String userID) {
        super(accountID, balance, userID, "Checking");
    }

    @Override
    public void withdraw(double amount) throws ExceptionHandler {
        if (getBalance() - amount < -OVERDRAFT_LIMIT) {
            throw new ExceptionHandler("Overdraft limit exceeded.");
        }
        super.withdraw(amount);
    }
}
