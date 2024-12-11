package group16.classes.User;

import java.sql.SQLException;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    public UserDAO DAO;

    // constructor
    public UserService(UserDAO DAO) {
        this.DAO = DAO;
    }

    // methods
    public void addUser(User user) throws SQLException {
        if (user == null) {
            System.out.println("User obj null");
            return;
        }

        String hashPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        User newUser = new User(user.getUsername(), hashPassword, user.getEmail(), user.getRole());
        DAO.addUser(newUser);

        if (DAO.connectionStatus) {
            System.out.println("Account registered!");
        }
    }

    public User getUser(String username, String password) throws SQLException {
        if (username == null || password == null) {
            System.out.println("Enter a username and password");
            return null;
        }
        return DAO.getUser(username, password);
    }

    public List<User> getAllUsers() throws SQLException {
        return DAO.getAllUsers();
    }

    public void deleteUser(int userId) throws SQLException {
        DAO.deleteUser(userId);
    }

    public User getUserById(int userId) throws SQLException {
        return DAO.getUserById(userId);
    }
}