package user;

import connection.ConnectionManager;
import database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages data related to the program user
 */
public class UserManager {

    /**
     * User currently logged into the program
     */
    public static User current = null;

    /**
     * Returns the user currently logged into the program
     *
     * @return user currently logged in
     */
    public static User getCurrent() {
        return current;
    }

    /**
     * Private constructor of UserManager
     */
    private UserManager() {

    }

    /**
     * Logs a new user into the program using a given email and password.
     * <p>
     * Returns true if the user is successfully logged in, otherwise returns false.
     * <p>
     * A login will fail in any of the following circumstances: the email is invalid,
     * the password is invalid, or there is an issue with the connection to the database.
     *
     * @param email:    Email of user logging in
     * @param password: Password of user logging in
     * @return true if login successful, false otherwise
     */
    public static boolean login(String email, String password) {
        if (DatabaseManager.validLoginData(email, password)) {
            try {
                boolean success;
                PreparedStatement s = ConnectionManager.getCurrentConnection().prepareStatement("SELECT first, middle, last FROM customer_name WHERE email=?");
                s.setString(1, email);
                s.execute();
                ResultSet rs = s.executeQuery();
                if (rs.next()) {
                    String first = rs.getString("first");
                    String middle = rs.getString("middle");
                    String last = rs.getString("last");
                    current = new User(first, middle, last, email, password);
                    success = true;
                } else {
                    success = false;
                }
                rs.close();
                return success;
            } catch (SQLException e) {
                return false;
            }
        } else {
            return false;
        }
    }

}
