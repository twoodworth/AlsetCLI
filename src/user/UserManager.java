package user;

import database.DatabaseManager;

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
        if (DatabaseManager.validLoginData(email, password)) {
            String[] name = DatabaseManager.getName(email);
            if (name == null) {
                return false;
            } else {
                current = new User(name[0], name[1], name[2], email, password);
                return true;
            }
        } else {
            return false;
        }
    }


    public static boolean logout() {
        if (current == null) return false;
        current = null;
        return true;
    }

}
