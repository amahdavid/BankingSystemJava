public class SavingsAccount extends Account {
    public SavingsAccount(String userID, String accountType) {
        super(userID, accountType);
    }

    @Override
    public void transfer(Account recipient, double amount) throws Exception {
        System.out.println("Not Implemented");
    }
}
