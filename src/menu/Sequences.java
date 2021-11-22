package menu;

import connection.ConnectionManager;
import constants.Keys;
import database.DBManager;
import io.IOManager;
import location.Address;
import location.GarageData;
import location.ServiceLocation;
import location.ServiceManager;
import product.Condition;
import product.Model;
import product.Vehicle;
import user.User;
import user.UserManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        // clear console
        IOManager.clear("");

        // get credentials
        String email = IOManager.getStringInput("Enter your Alset email:");
        String password = IOManager.getPasswordInput("Enter your Alset password:");
        boolean success = UserManager.login(email, password);

        if (success) {
            User current = UserManager.getCurrent();

            // Load in the user's vehicles to the vehicle manager menu.
            HashSet<Vehicle> vehicles = current.getVehicles();
            for (Vehicle v : vehicles) {
                String num = v.getSerialNum();
                String s = v.getYear() + " Model " + v.getModelName() + " (SN: " + num + ")";

                // Add menu option to access the vehicle's menu
                MenuManager.addOption(Keys.MY_VEHICLES_KEY, new MenuOption(s, () -> vehicleOverviewSequence(v)));
            }


            // create welcome message
            StringBuilder sb = new StringBuilder("Welcome back, ");
            sb.append(current.getFirst()).append(" ");
            String mid = current.getMiddle();
            if (mid != null && !mid.isEmpty()) sb.append(mid.charAt(0)).append(". ");
            sb.append(current.getLast()).append("!");

            // display the main menu
            MenuManager.showMenu(Keys.ALSET_MAIN_MENU_KEY, sb.toString());
        } else MenuManager.setNextMessage("Unable to login. Please try again.");
    }

//    static void inspectVehicleSequence() { //todo add code
//        // get vehicle
//        if (v == null) {
//            IOManager.println("Error loading vehicle info.");
//            return;
//        }
//
//        // determine if it is currently being worked on
//        ServiceLocation location = DBManager.getServiceLocation(v);
//        if (location != null) {
//            IOManager.println("This vehicle is currently being serviced at " + location.getName() + ".");
//            IOManager.println("Please pick it up before ordering an inspection.");
//            IOManager.println();
//            IOManager.getStringInput("Enter any value to continue:");
//            return;
//        }
//
//        // get service locations
//        HashSet<ServiceLocation> locs = DBManager.getRepairableLocations(v);
//        if (locs == null) {
//            IOManager.println("Error loading service info.");
//            return;
//        }
//
//        // Remove previous vehicles from 'My Vehicles' Menu
//        int size = MenuManager.getSize(Keys.INSPECTION_LOCATIONS_LIST);
//        for (int i = size - 1; i > 0; i--) {
//            MenuManager.removeOption(Keys.INSPECTION_LOCATIONS_LIST, i);
//        }
//
//        for (ServiceLocation l : locs) {
//            MenuManager.addOption(Keys.INSPECTION_LOCATIONS_LIST, new MenuOption(l.getName(), () -> {
//            })); //todo add functionality
//        }
//
//        MenuManager.showMenu(Keys.INSPECTION_LOCATIONS_LIST);
//    }

    /**
     * Begins the edgar1 login sequence.
     * <p>
     * This login sequence logs the user into edgar1.
     */
    static void edgar1LoginSequence() {
        // clear console
        IOManager.clear("");

        // Get credentials
        String id = IOManager.getStringInput("Enter your Oracle id for edgar1:");
        String pwd = IOManager.getPasswordInput("Enter your Oracle password for edgar1:");

        // Connect to database
        IOManager.println("Connecting to database...");
        Connection conn = ConnectionManager.createEdgar1Connection(id, pwd);
        if (conn == null)
            MenuManager.setNextMessage("Invalid id/password (make sure are you connected to Lehigh wifi or using the Lehigh VPN)");
        else {
            MenuManager.showMenu(Keys.ALSET_LOGIN_MENU_KEY, "Connected successfully as " + id);
        }
    }

    /**
     * Begins the Alset logout sequence, which logs the user out of the program.
     */
    static void alsetLogoutSequence() {
        IOManager.println("Logging out of " + UserManager.getCurrent().getEmail() + "...");

        // Remove user's vehicles from 'My Vehicles' Menu
        int size = MenuManager.getSize(Keys.MY_VEHICLES_KEY);
        for (int i = size - 1; i > 0; i--)
            MenuManager.removeOption(Keys.MY_VEHICLES_KEY, i);


        // Log user out + display login menu
        UserManager.logout();
        MenuManager.showMenu(Keys.ALSET_LOGIN_MENU_KEY, "Successfully logged out.");
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
        //clear console
        IOManager.clear("");

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
                            MenuManager.setNextMessage("Password has been updated successfully.");
                        } else {
                            MenuManager.setNextMessage("Error while updating password. Please try again.");
                        }
                        break;
                    } else {
                        IOManager.clear("Password confirmation does not match. Please Try again");
                    }
                }
            } else {
                MenuManager.setNextMessage("Verification failed.");
            }
        } else {
            MenuManager.setNextMessage("Unrecognised email, please try again.");
        }
    }

    static void createAcctSequence() {//todo add code
    }

    static void serviceManagerSequence() {//todo add code
        // clear console
        IOManager.clear("");

        String password = IOManager.getPasswordInput("Enter password:");
        ServiceLocation location = DBManager.getServiceLocation(password);
        if (location == null) {
            MenuManager.setNextMessage("Invalid password.");
        } else {
            ServiceManager.setCurrent(location);
            MenuManager.deleteMenu(Keys.SERVICE_MANAGER_KEY);
            MenuManager.createMenu(Keys.SERVICE_MANAGER_KEY, location.getName(),
                    new MenuOption("Location Overview", Sequences::locationOverviewSequence),
                    new MenuOption("Manage Showroom \t//todo add", () -> {
                    }),//todo add
                    new MenuOption("Manage Listings\t//todo add", () -> {
                    }),//todo add
                    new MenuOption("Manage Garage", () -> MenuManager.showMenu(Keys.MANAGE_GARAGE_KEY, "")),
                    new MenuOption("Log Out", Sequences::serviceManagerLogoutSequence),
                    new MenuOption("Exit Program", Sequences::exitSequence)

            );

            MenuManager.showMenu(Keys.SERVICE_MANAGER_KEY, "Successfully logged in as " + location.getName() + " Service Manager.");
        }
    }

    static void viewGarageSequence() {
        // get all vehicles being repaired or ready for pickup in the garage
        ServiceLocation loc = ServiceManager.getCurrent();
        HashSet<GarageData> data = DBManager.getGarageData(loc);
        if (data == null) {
            MenuManager.setNextMessage("Unable to load info.");
            return;
        }
        MenuManager.deleteMenu(Keys.VIEW_GARAGE_KEY);
        MenuManager.createMenu(Keys.VIEW_GARAGE_KEY, "Garage Vehicles");
        MenuManager.addOption(Keys.VIEW_GARAGE_KEY, new MenuOption("Return to Previous Menu", () -> MenuManager.showPrevious("")));
        for (GarageData gd : data) {
            String sn = gd.getSerialNum();
            Vehicle v = DBManager.getVehicle(sn);
            if (v == null) {
                MenuManager.setNextMessage("Unable to load info.");
                return;
            }
            MenuManager.addOption(
                    Keys.VIEW_GARAGE_KEY,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " (SN: " + sn + ")",
                            () -> {
                                // clear console
                                IOManager.clear("");
                                // print basic info
                                IOManager.println("\nVehicle Overview:");
                                IOManager.println("\tSerial Number: " + sn);
                                IOManager.println("\tOwner email: " + gd.getOwner());
                                IOManager.println("\tModel: " + v.getModelName());
                                IOManager.println("\tYear: " + v.getYear());

                                // print condition
                                Condition condition = v.getCondition();
                                IOManager.println("\tMileage: " + condition.getMileage() + " miles");
                                IOManager.println("\tLast Inspection: " + condition.getLastInspectionFormatted());
                                IOManager.println("\tHas Detected Damage: " + condition.hasDamage());

                                HashSet<String> options = DBManager.getOptions(sn);
                                // print additional options
                                if (options == null || options.isEmpty()) {
                                    IOManager.println("\tAdditional Features: None");
                                } else {
                                    IOManager.println("\tAdditional Features:");
                                    for (String option : options) {
                                        IOManager.println("\t\t- " + option);
                                    }
                                }
                                IOManager.println("\nVehicle Status:");
                                IOManager.println("\tReady for pickup: " + gd.isReady());
                                if (gd.getStartTime() > 0)
                                    IOManager.println("\tRepair Started: " + gd.getStartTimeFormatted());
                                if (gd.getEndTime() > 0)
                                    IOManager.println("\tRepair Ended: " + gd.getEndTimeFormatted());
                                if (gd.getReason() == null)
                                    IOManager.println("\tService Provided: Manufacturing & Delivering Vehicle");
                                else IOManager.println("\tService Provided: " + gd.getReason());
                                // pause until user enters value
                                IOManager.println();
                                IOManager.getStringInput("Enter any value to continue:");
                            }
                    )
            );
        }
        MenuManager.showMenu(Keys.VIEW_GARAGE_KEY, "");
    }

    static void locationOverviewSequence() {
        // get location
        ServiceLocation location = ServiceManager.getCurrent();
        if (location == null) {
            MenuManager.setNextMessage("Unable to load info.");
            return;
        }

        HashSet<Model> models = DBManager.getRepairableModels(location);
        if (models == null) {
            MenuManager.setNextMessage("Unable to load info.");
            return;
        }

        HashMap<String, ArrayList<Integer>> modelMap = new HashMap<>();

        // insert data into map
        for (Model model : models) {
            String name = model.getName();
            modelMap.putIfAbsent(name, new ArrayList<>());
            modelMap.get(name).add(model.getYear());
        }

        // sort data in map
        for (String name : modelMap.keySet())
            modelMap.get(name).sort(Integer::compareTo);
        String[] keys = new String[modelMap.size()];
        modelMap.keySet().toArray(keys);
        Arrays.sort(keys);


        Address address = location.getAddress();

        //clear console
        IOManager.clear("");

        // print basic info
        IOManager.println("\nLocation Overview:");
        IOManager.println("\tLocation Name: " + location.getName());
        IOManager.println("\tLocation ID: " + location.getId());
        IOManager.println("\tAddress: " + address.getStreet());
        IOManager.println("\t\t " + address.getCity() + ", " + address.getState() + " " + address.getZip());
        IOManager.println("\t\t " + address.getCountry() + ", " + address.getPlanet());

        // print repairable models
        IOManager.println("\tRepairable Models:");
        for (String key : keys) {
            StringBuilder sb = new StringBuilder("\t\t- Model ").append(key).append(" [Years: ");
            ArrayList<Integer> years = modelMap.get(key);
            int l = years.size();
            for (int i = 0; i < l - 1; i++)
                sb.append(years.get(i)).append(", ");
            sb.append(years.get(l - 1)).append("]");
            IOManager.println(sb.toString());
        }
        IOManager.println();
        IOManager.getStringInput("Enter any value to continue:");
    }

    static void serviceManagerLogoutSequence() {
        IOManager.println("Logging out of " + ServiceManager.getCurrent().getName() + "...");
        ServiceManager.logout();
        MenuManager.showMenu(Keys.ALSET_LOGIN_MENU_KEY, "Successfully logged out.");
    }

    /**
     * Closes the database connection
     * and displays the edgar1 login menu.
     */
    static void endConnectionSequence() {
        IOManager.println("Closing Connection...");
        ConnectionManager.closeConnection();
        MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, "Connection Successfully closed.");
    }

    static void vehicleOverviewSequence(Vehicle v) {
        String sn = v.getSerialNum();

        // get options
        HashSet<String> options = DBManager.getOptions(sn);

        // get condition
        Condition condition = v.getCondition();
        if (condition == null) {
            MenuManager.setNextMessage("Error loading vehicle overview.");
            return;
        }

        ServiceLocation location = DBManager.getServiceLocation(v);

        //clear console
        IOManager.clear("");

        // Print service status
        IOManager.println("\nVehicle Overview:");
        if (location != null) {
            if (DBManager.isReadyForPickup(v)) {
                IOManager.println("\tVEHICLE IS READY FOR PICKUP AT " + location.getName().toUpperCase());
            } else {
                IOManager.println("\tVEHICLE IS CURRENTLY BEING SERVICED AT " + location.getName().toUpperCase());
            }
        }

        // print basic info
        IOManager.println("\tSerial Number: " + sn);
        IOManager.println("\tModel: " + v.getModelName());
        IOManager.println("\tYear: " + v.getYear());

        // print condition
        IOManager.println("\tMileage: " + condition.getMileage() + " miles");
        IOManager.println("\tLast Inspection: " + condition.getLastInspectionFormatted());
        IOManager.println("\tHas Detected Damage: " + condition.hasDamage());

        // print additional options
        if (options == null || options.isEmpty()) {
            IOManager.println("\tAdditional Features: None");
        } else {
            IOManager.println("\tAdditional Features:");
            for (String option : options) {
                IOManager.println("\t\t- " + option);
            }
        }

        // pause until user enters value
        IOManager.println();
        IOManager.getStringInput("Enter any value to continue:");
    }

    public static void productManagerSequence() {//todo add
    }
}
