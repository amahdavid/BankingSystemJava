public class BusinessAccount extends Account {
    public BusinessAccount(String userID, String accountType) {
        super(userID, accountType);
    }

    @Override
    public void transfer(Account recipient, double amount) throws Exception {
        System.out.println("Not Implemented yet");
    }
}
