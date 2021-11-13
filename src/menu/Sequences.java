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
     * <p>
     * This login sequence logs the user into Alset.
     * <p>
     * This sequence interacts with the database in order to
     * verify the account exists, and to fetch user data upon logging
     * in successfully.
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
                String num = v.getSerialNum();
                String s = v.getYear() + " Model " + v.getModel() + " (SN: " + num + ")";

                // Create menu for vehicle
                MenuManager.createMenu(num, s,
                        new MenuOption("Vehicle Overview //todo add", () -> {}), //todo add functionality
                        new MenuOption("Vehicle Condition //todo add", () -> {}), //todo add functionality
                        new MenuOption("Schedule a Repair/Inspection //todo add", () -> {}), //todo add functionality
                        new MenuOption("Maintenance Status //todo add", () -> {}), // todo add functionality
                        new MenuOption("Return to My Vehicles", () -> MenuManager.showMenu(Constants.MY_VEHICLES_KEY))
                );

                // Add menu option to access the vehicle's menu
                MenuManager.addOption(Constants.MY_VEHICLES_KEY, new MenuOption(s, () -> MenuManager.showMenu(num))); //todo add functionality
            }

            // display the main menu
            MenuManager.showMenu(Constants.ALSET_MAIN_MENU_KEY);
        } else IOManager.println("Unable to login. Please try again.");
    }

    /**
     * Begins the edgar1 login sequence.
     * <p>
     * This login sequence logs the user into edgar1.
     */
    static void edgar1LoginSequence() {
        // Get credentials
        String id = IOManager.getStringInput("Enter your Oracle id for edgar1:");
        String pwd = IOManager.getPasswordInput("Enter your Oracle password for edgar1:");

        // Connect to database
        IOManager.println("Connecting to database...");
        Connection conn = ConnectionManager.createEdgar1Connection(id, pwd);
        if (conn == null)
            IOManager.println("Invalid id/password (make sure are you connected to Lehigh wifi or using the Lehigh VPN)");
        else {
            IOManager.println("Connected successfully.");
            MenuManager.showMenu(Constants.ALSET_LOGIN_MENU_KEY);
        }
    }

    /**
     * Begins the Alset logout sequence, which logs the user out of the program.
     */
    static void alsetLogoutSequence() {
        IOManager.println("Logging out of " + UserManager.getCurrent().getEmail() + "...");

        // Remove user's vehicles from 'My Vehicles' Menu
        int size = MenuManager.getSize(Constants.MY_VEHICLES_KEY);
        for (int i = size - 1; i > 0; i--)
            MenuManager.removeOption(Constants.MY_VEHICLES_KEY, i);

        // Remove vehicle menus
        for (Vehicle v : UserManager.getCurrent().getVehicles())
            MenuManager.deleteMenu(v.getSerialNum());

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

    /**
     * Allows the user to change their password without
     * logging in.
     * <p>
     * The user enters their email, and a message gets sent
     * to that email. (Since this is just a class project,
     * and the emails are not real, the would-be emailed
     * message gets printed out into the console).
     * <p>
     * The user must then enter the verification key sent
     * via email. If correct, the user is prompted to enter
     * their new password, which is then saved to the database.
     */
    static void forgotPwdSequence() {
        String email = IOManager.getStringInput("Enter your Alset email:");
        boolean exists = DBManager.emailExists(email);
        if (exists) {
            String pass = User.getRandomPassword();
            IOManager.println("A verification key has been sent to " + email);
            IOManager.println("Email from security@alset.com to " + email + ": Your Alset verification key is '" + pass + "'.");
            String key = IOManager.getStringInput("Enter verification key:");
            if (key.equals(pass)) {
                IOManager.println("Verification Successful.");
                while (true) {
                    String newPass = IOManager.getPasswordInput("Enter new password:");
                    String newPass2 = IOManager.getPasswordInput("Confirm new password:");

                    if (newPass.equals(newPass2)) {
                        boolean success = DBManager.updatePassword(email, newPass);
                        if (success) {
                            IOManager.println("Password has been updated successfully.");
                            break;
                        } else {
                            IOManager.println("Error while updating password. Please try again.");
                        }
                    } else {
                        IOManager.println("Password confirmation does not match. Please try again.");
                    }
                }
            } else {
                IOManager.println("Verification failed.");
            }
        } else {
            IOManager.print("Unrecognised email, please try again.");
        }
    }

    static void createAcctSequence() {//todo add code
    }

    static void adminLoginSequence() {//todo add code
    }

    /**
     * Closes the database connection
     * and displays the edgar1 login menu.
     */
    static void endConnectionSequence() {
        IOManager.println("Closing Connection...");
        ConnectionManager.closeConnection();
        MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
    }
}
