import java.util.UUID;

public class User {
    private String userID;
    private String email;
    private String password;
    private String role;

    public User(String email, String password, String role) {
        this.userID = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String userID, String email, String password, String role) {
        this.userID = userID;  // Use the existing UUID from the database
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // figure out what you want to do with this function
    public boolean login (String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
