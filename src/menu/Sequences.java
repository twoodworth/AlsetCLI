package menu;

import connection.ConnectionManager;
import constants.Constants;
import database.DBManager;
import main.IOManager;
import user.User;
import user.UserManager;
import vehicle.Vehicle;

import java.sql.Connection;
import java.util.HashSet;

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

            // welcome the user back
            StringBuilder sb = new StringBuilder("Welcome back, ");
            sb.append(current.getFirst()).append(" ");
            String mid = current.getMiddle();
            if (mid != null && !mid.isEmpty()) sb.append(mid.charAt(0)).append(". ");
            sb.append(current.getLast()).append("!");
            IOManager.println(sb.toString());

            // Load in the user's vehicles to the vehicle manager menu.
            HashSet<Vehicle> vehicles = current.getVehicles();
            for (Vehicle v : vehicles) {
                String s = v.getYear() + " Model " + v.getModel() + " (SN: " + v.getSerial_num() + ")";
                MenuManager.addOption(Constants.MY_VEHICLES_KEY, new MenuOption(s, () -> {
                        }) //todo add functionality
                );
            }

            // display the main menu
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

        // Remove user's vehicles from 'My Vehicles' Menu
        int size = MenuManager.getSize(Constants.MY_VEHICLES_KEY);
        for (int i = size - 1; i > 0; i--)
            MenuManager.removeOption(Constants.MY_VEHICLES_KEY, i);

        // Log user out + display login menu
        UserManager.logout();
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
        boolean exists = DBManager.emailExists(email);
        if (exists) {
            String pass = User.getRandomPassword();
            DBManager.updatePassword(email, pass);
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

    static void endConnectionSequence() {
        IOManager.println("Closing Connection...");
        ConnectionManager.closeConnection();
        MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
    }
}
