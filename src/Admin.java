public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password, "Admin");
    }

    public void createAccount() {
        // will implement with SQL DB
    }

    public void deleteAccount() {
        // will implement with SQL DB
    }

    // other admin functionalities
}
