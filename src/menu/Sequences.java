package menu;

import connection.ConnectionManager;
import constants.Constants;
import main.InputManager;
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
        String email = InputManager.getStringInput("Enter your Alset email:");
        String password = InputManager.getPasswordInput("Enter your Alset password:");
        boolean success = UserManager.login(email, password);

        if (success) System.out.println("Success");
        else System.out.println("Unable to login. Please try again.");
    }

    static void edgar1LoginSequence() {
        String id = InputManager.getStringInput("Enter your Oracle id for edgar1:");
        String pwd = InputManager.getPasswordInput("Enter your Oracle password for edgar1:");
        System.out.println("Connecting to database...");
        Connection conn = ConnectionManager.createEdgar1Connection(id, pwd);
        if (conn == null)
            System.out.println("Invalid id/password (make sure are you connected to Lehigh wifi or using the Lehigh VPN)");
        else {
            System.out.println("Connected successfully.");
            MenuManager.showMenu(Constants.ALSET_LOGIN_MENU_KEY);
        }
    }


    /**
     * Exits the program.
     */
    public static void exitSequence() {
        Connection current = ConnectionManager.getCurrentConnection();
        if (current != null) {
            System.out.println("Closing connection...");
            boolean closed = ConnectionManager.closeConnection();
            if (!closed) System.out.println("Error while closing connection.");
        }
        System.out.println("Exiting...");
        System.exit(0);
    }

    static void forgotPwdSequence() {
        InputManager.getStringInput("test");
    }

    public static void createAcctSequence() {//todo add code
    }

    public static void adminLoginSequence() {//todo add code
    }

    public static void endConnectionSequence() {//todo add code
    }
}
