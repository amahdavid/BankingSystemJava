public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 100.00;

    public SavingsAccount(String userID, String accountType) {
        super(userID, accountType);
    }

    public SavingsAccount(String accountID, double balance, String userID) {
        super(accountID, balance, userID, "Savings");
    }

    @Override
    public void withdraw(double amount) throws ExceptionHandler {
        if (getBalance() - amount < MINIMUM_BALANCE) {
            throw new ExceptionHandler("Insufficient funds. Savings account must maintain a minimum balance of $" + MINIMUM_BALANCE);
        }
        super.withdraw(amount);
    }
}
