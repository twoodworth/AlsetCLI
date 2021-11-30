package user;

import database.DBManager;
import vehicle.Vehicle;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages data related to the program user
 */
public class UserManager {

    /**
     * User currently logged into the program
     */
    private static User current = null;

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
        if (DBManager.validLoginData(email, password)) {
            String[] name = DBManager.getName(email);
            Set<Vehicle> vehicles = DBManager.getVehicles(email);
            if (name == null || vehicles == null) {
                return false;
            } else {
                current = new User(name[0], name[1], name[2], email, password, vehicles);
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Logs the user out.
     * <p>
     * Current gets set to null, and the function
     * returns false if current was already set to null.
     *
     * @return False if the user was already logged out, otherwise true.
     */
    public static boolean logout() {
        if (current == null) return false;
        current = null;
        return true;
    }

    /**
     * Sends a message to the given email address via email
     * @param email: Email to send to
     * @param message: Message to send
     */
    public static void sendEmail(String email, String message) {
        /*
            For the sake of this project, an actual email does not get sent.
            If this program were deployed into a fully-functioning company,
            this method would then send out an actual email in place of this
            comment.
         */
    }
}
