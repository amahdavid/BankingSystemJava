import java.util.UUID;

public class User {
    private String userID;
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.userID = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login (String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

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
