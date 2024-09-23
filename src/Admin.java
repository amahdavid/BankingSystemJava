public class Admin extends User {

    public Admin(String userID, String username, String password) {
        super(userID, username, password, "Admin");
    }

    public void createAccount() {
        // will implement with SQL DB
    }

    public void deleteAccount() {
        // will implement with SQL DB
    }
}
