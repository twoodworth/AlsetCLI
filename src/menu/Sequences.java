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

    static void forgotPwdSequence() {

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
            MenuManager.getInstance().showMenu(Constants.START_MENU_KEY);
        }
    }
}
