package group16.classes.User;

public class User {
    private String username;
    private String password;
    private String email;
    private String role;
    
    //constructors
    public User() {
        username = "Tempname";
        password = "Temppass";
        email = "Tempemail";
        role = "none";
    }

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    //getters and setters

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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        
        return "U:" + username + " P:" + password + " E:" + email + " R:" + role;
    }
}

