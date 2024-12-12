package group16.classes.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    public Connection connection;
    public boolean connectionStatus;
    public boolean loginSuccess = false;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users(username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole());
            statement.executeUpdate();
            connectionStatus = true;
        } catch (Exception e) {
            System.err.println("Database connection failed");
            connectionStatus = false;
        }
    }

    public User getUser(String username, String password) throws SQLException {
        User loggedUser = new User();
        String sql = "SELECT * FROM users";
        try (var statement = connection.createStatement();
             var result = statement.executeQuery(sql)) {

            connectionStatus = true;

            while (result.next()) {
                if (result.getString("username").equals(username)
                        && BCrypt.checkpw(password, result.getString("password"))) {
                    loginSuccess = true;
                    loggedUser = new User(result.getString("username"),
                            password,
                            result.getString("email"),
                            result.getString("role"));
                    break;
                }
            }
            return loggedUser;
        } catch (Exception e) {
            System.err.println("Database connection failed");
            connectionStatus = false;
        }
        return loggedUser;
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        List<User> userList = new ArrayList<>();
        try (var statement = connection.createStatement();
             var result = statement.executeQuery(sql)) {

            connectionStatus = true;
            while (result.next()) {
                User listUser = new User(result.getString("username"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("role"));
                userList.add(listUser);
            }
            return userList;
        } catch (Exception e) {
            connectionStatus = false;
        }
        return userList;
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(result.getString("username"),
                            result.getString("password"),
                            result.getString("email"),
                            result.getString("role"));
                }
            }
        }
        return null;
    }
}