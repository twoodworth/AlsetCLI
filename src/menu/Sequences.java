package menu;

import connection.ConnectionManager;
import constants.Constants;
import database.DatabaseManager;
import main.IOManager;
import user.User;
import user.UserManager;

import java.sql.Connection;

/**
 * Contains all the sequence functions in the program.
 * <p>
 * A sequence is a set of prompts and user input requests allows
 * the user to interact with the program outside of just through
 * menus.
 */
public class Sequences {

    /**
     * Runs the alset login sequence
     */
    static void alsetLoginSequence() {
        String email = IOManager.getStringInput("Enter your Alset email:");
        String password = IOManager.getPasswordInput("Enter your Alset password:");
        boolean success = UserManager.login(email, password);

        if (success) {
            User current = UserManager.getCurrent();
            StringBuilder sb = new StringBuilder("Welcome back, ");
            sb.append(current.getFirst()).append(" ");
            String mid = current.getMiddle();
            if (mid != null && !mid.isEmpty()) sb.append(mid.charAt(0)).append(". ");
            sb.append(current.getLast()).append("!");

            IOManager.println(sb.toString());
            MenuManager.showMenu(Constants.ALSET_MAIN_MENU_KEY);
        } else IOManager.println("Unable to login. Please try again.");
    }

    static void edgar1LoginSequence() {
        String id = IOManager.getStringInput("Enter your Oracle id for edgar1:");
        String pwd = IOManager.getPasswordInput("Enter your Oracle password for edgar1:");
        IOManager.println("Connecting to database...");
        Connection conn = ConnectionManager.createEdgar1Connection(id, pwd);
        if (conn == null)
            IOManager.println("Invalid id/password (make sure are you connected to Lehigh wifi or using the Lehigh VPN)");
        else {
            IOManager.println("Connected successfully.");
            MenuManager.showMenu(Constants.ALSET_LOGIN_MENU_KEY);
        }
    }

    static void alsetLogoutSequence() {
        IOManager.println("Logging out of " + UserManager.getCurrent().getEmail() + "...");
        if (!UserManager.logout()) {
            IOManager.println("Error: already logged out.");
        }
        MenuManager.showMenu(Constants.ALSET_LOGIN_MENU_KEY);
    }

    /**
     * Exits the program.
     */
    static void exitSequence() {
        Connection current = ConnectionManager.getCurrentConnection();
        if (current != null) {
            IOManager.println("Closing connection...");
            boolean closed = ConnectionManager.closeConnection();
            if (!closed) IOManager.println("Error while closing connection.");
        }
        IOManager.println("Exiting...");
        System.exit(0);
    }

    static void forgotPwdSequence() {
        String email = IOManager.getStringInput("Enter your Alset email:");
        boolean exists = DatabaseManager.emailExists(email);
        if (exists) {
            String pass = User.getRandomPassword();
            DatabaseManager.updatePassword(email, pass);
            IOManager.println("A new random password has been sent to " + email);
            IOManager.println("Email from security@alset.com: Your new random password is '" + pass + "'.");
        } else {
            IOManager.print("Unrecognised email, please try again.");
        }
    }

    static void createAcctSequence() {//todo add code
    }

    static void adminLoginSequence() {//todo add code
    }

    static void endConnectionSequence() {//todo add code
    }
}
