package group16.classes.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    public boolean connectionStatus;
    public boolean loginSuccess = false;

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users(username, password, email, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionZ.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

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

        try (Connection connection = DatabaseConnectionZ.getConnection();) {
            var statement = connection.createStatement();
            var result = statement.executeQuery(sql);

            connectionStatus = true;

            
            while (result.next()) {
                if (result.getString("username").equals(username) 
                && BCrypt.checkpw(password, result.getString("password")) == true) {
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


    public int updateUser(User user) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public int deleteUser(User user) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        List<User> userList = new ArrayList<User>();
        
        try (Connection connection = DatabaseConnectionZ.getConnection();) {
            var statement = connection.createStatement();
            var result = statement.executeQuery(sql);

            connectionStatus = true;

            //creates list and adds all users to it as User class
            User listUser = new User();
            while (result.next()) {
                listUser = new User(result.getString("username"),
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
}
