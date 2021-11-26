package menu;

import card.Card;
import card.CardManager;
import connection.ConnectionManager;
import constants.Key;
import database.DBManager;
import io.IOManager;
import location.Address;
import location.GarageData;
import location.ServiceLocation;
import location.ServiceManager;
import user.User;
import user.UserManager;
import vehicle.Condition;
import vehicle.Model;
import vehicle.Vehicle;

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
                MenuManager.addOption(Key.CUSTOMER_VEHICLES_MENU, new MenuOption(s, () -> vehicleOverviewSequence(v)));
            }


            // create welcome message
            StringBuilder sb = new StringBuilder("Welcome back, ");
            sb.append(current.getFirst()).append(" ");
            String mid = current.getMiddle();
            if (mid != null && !mid.isEmpty()) sb.append(mid.charAt(0)).append(". ");
            sb.append(current.getLast()).append("!");

            // display the main menu
            MenuManager.showMenu(Key.CUSTOMER_MENU, sb.toString());
        } else MenuManager.setNextMessage("Unable to login. Please try again.");
    }

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
            MenuManager.showMenu(Key.ALSET_LOGIN_MENU, "Connected successfully as " + id + ".");
        }
    }

    /**
     * Begins the Alset logout sequence, which logs the user out of the program.
     */
    static void alsetLogoutSequence() {
        IOManager.println("Logging out of " + UserManager.getCurrent().getEmail() + "...");

        // Remove user's vehicles from 'My Vehicles' Menu
        int size = MenuManager.getSize(Key.CUSTOMER_VEHICLES_MENU);
        for (int i = size - 1; i > 0; i--)
            MenuManager.removeOption(Key.CUSTOMER_VEHICLES_MENU, i);


        // Log user out + display login menu
        UserManager.logout();
        MenuManager.showMenu(Key.ALSET_LOGIN_MENU, "Successfully logged out.");
    }

    /**
     * Exits the program.
     */
    static void exitSequence() {
        IOManager.clear("");
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

    /**
     * Allows a service manager to formally complete servicing
     * a vehicle in their service location's garage
     */
    static void finishGarageVehicleSequence() {
        ServiceLocation loc = ServiceManager.getCurrent();

        // Get unfinished vehicles in garage
        HashSet<GarageData> data = DBManager.getGarageData(loc);
        if (data == null) {
            MenuManager.setNextMessage("Unable to load info.");
            return;
        }

        // remove finished vehicles from set
        for (GarageData gd : new HashSet<>(data)) {
            if (gd.isReady()) data.remove(gd);
        }

        // check if there are no unfinished vehicles
        if (data.size() == 0) {
            MenuManager.setNextMessage("There are no unfinished vehicles in the garage.");
            return;
        }

        MenuManager.deleteMenu(Key.FINISH_GARAGE_VEHICLE_MENU);
        MenuManager.createMenu(Key.FINISH_GARAGE_VEHICLE_MENU, "Finish Garage Vehicles");
        MenuManager.addOption(Key.FINISH_GARAGE_VEHICLE_MENU, new MenuOption("Return to Previous Menu", MenuManager::showPrevious));
        for (GarageData gd : data) {
            String sn = gd.getSerialNum();
            Vehicle v = DBManager.getVehicle(sn);
            if (v == null) {
                MenuManager.setNextMessage("Unable to load info.");
                return;
            }
            boolean isManufactured = v.isManufactured();
            boolean isInspection = gd.getReason().equals("Inspection");
            MenuManager.addOption(
                    Key.FINISH_GARAGE_VEHICLE_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " - " + gd.getReason() + " (SN: " + sn + ")",
                            () -> {
                                if (!isManufactured) {

                                    // clear console
                                    IOManager.clear("");

                                    while (true) {
                                        // ask if arrived
                                        String arrived = IOManager.getStringInput("Has the vehicle with a serial number of " + sn + " arrived? (y/n): ");

                                        if (arrived.equals("y")) {
                                            break;
                                        } else if (arrived.equals("n")) {
                                            MenuManager.setNextMessage("Vehicle has not arrived.");
                                            return;
                                        } else {
                                            IOManager.clear("Invalid response, please try again.");
                                        }
                                    }

                                    while (true) {
                                        IOManager.clear("Please confirm the following:");

                                        // print basic info
                                        IOManager.println("\tSerial Number: " + sn);
                                        IOManager.println("\tModel: " + v.getModelName());
                                        IOManager.println("\tYear: " + v.getYear());

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

                                        // ask if correct
                                        String correct = IOManager.getStringInput("Is all of the information correct? (y/n): ");

                                        if (correct.equals("y")) {
                                            break;
                                        } else if (correct.equals("n")) {
                                            MenuManager.setNextMessage("Vehicle is not correct.");
                                            return;
                                        } else {
                                            IOManager.clear("Invalid response, please try again.");
                                        }
                                    }

                                    boolean success = DBManager.finishManufactured(sn);
                                    if (!success) {
                                        MenuManager.setNextMessage("Failed to update database. Please try again.");
                                        return;
                                    }
                                    String email = DBManager.getEmail(v);
                                    if (email != null) {
                                        UserManager.sendEmail(email, "Your vehicle is ready for pickup at " + loc.getName() + "");
                                        IOManager.clear("Vehicle pickup notification has been sent to " + email);
                                    } else {
                                        IOManager.clear("");
                                    }
                                    IOManager.println("Vehicle is now ready for pickup.");
                                    IOManager.getStringInput("Enter any value to continue:");

                                } else if (isInspection) {
                                    while (true) {
                                        // ask if finished
                                        String response = IOManager.getStringInput("Have you finished inspecting the vehicle with serial number " + sn + "? (y/n): ");

                                        if (response.equals("y")) {
                                            break;
                                        } else if (response.equals("n")) {
                                            MenuManager.setNextMessage("Inspection is not finished.");
                                            return;
                                        } else {
                                            IOManager.clear("Invalid response, please try again.");
                                        }
                                    }

                                    boolean needs;
                                    while (true) {
                                        // ask if arrived
                                        String response = IOManager.getStringInput("Is this vehicle in need of maintenance? (y/n): ");

                                        if (response.equals("y")) {
                                            needs = true;
                                            break;
                                        } else if (response.equals("n")) {
                                            needs = false;
                                            break;
                                        } else {
                                            IOManager.clear("Invalid response, please try again.");
                                        }
                                    }

                                    boolean success = DBManager.finishInspection(v, needs);
                                    if (!success) {
                                        MenuManager.setNextMessage("Failed to update database. Please try again.");
                                        return;
                                    }

                                    String email = DBManager.getEmail(v);
                                    if (email != null) {
                                        UserManager.sendEmail(email, "Your vehicle is ready for pickup at " + loc.getName() + "");
                                        IOManager.clear("Vehicle pickup notification has been sent to " + email);
                                    } else {
                                        IOManager.clear("");
                                    }
                                    IOManager.println("Inspection has been completed.");
                                    IOManager.getStringInput("Enter any value to continue:");
                                } else {
                                    while (true) {
                                        // ask if finished
                                        String response = IOManager.getStringInput("Have you finished servicing the vehicle with serial number " + sn + "? (y/n): ");

                                        if (response.equals("y")) {
                                            break;
                                        } else if (response.equals("n")) {
                                            MenuManager.setNextMessage("Service is not completed.");
                                            return;
                                        } else {
                                            IOManager.clear("Invalid response, please try again.");
                                        }
                                    }

                                    boolean success = DBManager.finishServicing(v);
                                    if (!success) {
                                        MenuManager.setNextMessage("Failed to update database. Please try again.");
                                        return;
                                    }

                                    String email = DBManager.getEmail(v);
                                    if (email != null) {
                                        UserManager.sendEmail(email, "Your vehicle is ready for pickup at " + loc.getName() + "");
                                        IOManager.clear("Vehicle pickup notification has been sent to " + email);
                                    } else {
                                        IOManager.clear("");
                                    }
                                    IOManager.println("Servicing has been completed.");
                                    IOManager.getStringInput("Enter any value to continue:");
                                }
                            }
                    )
            );
        }
        MenuManager.showMenu(Key.FINISH_GARAGE_VEHICLE_MENU, "");
    }

    /**
     * Allows a service manager to add a vehicle into their garage.
     */
    static void addGarageVehicleSequence() {
        ServiceLocation loc = ServiceManager.getCurrent();

        // clear console
        IOManager.clear("");

        // Get vehicle/repair info
        String sn = IOManager.getStringInput("Enter 9-digit vehicle serial number:");
        Vehicle v = DBManager.getVehicle(sn);
        if (v == null) {
            MenuManager.setNextMessage("Error: No vehicle found with the given serial number.");
            return;
        }
        if (DBManager.isAtGarage(v)) {
            MenuManager.setNextMessage("Vehicle is already being serviced. If you think this is an error, please try again.");
            return;
        }
        HashSet<Model> repairable = DBManager.getRepairableModels(loc);
        if (repairable == null) {
            MenuManager.setNextMessage("Error: Unable to load data. Please try again.");
            return;
        }
        Model m = v.getModel();
        if (!repairable.contains(m)) {
            MenuManager.setNextMessage(loc.getName() + " cannot repair a " + m.getYear() + " Model " + m.getName());
            return;
        }
        String type = IOManager.getStringInput("Enter Repair/Service Type:");
        Long price = null;
        while (price == null) {
            price = IOManager.getLongInput("Enter Repair/Service Price:", 0, Long.MAX_VALUE);
            if (price == null) {
                IOManager.clear("Invalid value (must be an integer), please try again.");
            }
        }
        Double time = null;
        while (time == null) {
            time = IOManager.getDoubleInput("Enter Expected Service Time (as a decimal, in hours):", 0.0, Double.MAX_VALUE);
            if (time == null) {
                IOManager.clear("Invalid value, please try again.");
            }
        }

        IOManager.clear("Please hand the console over to the customer.");
        IOManager.getStringInput("Enter any value to continue:");
        IOManager.clear("Welcome to " + loc.getName() + "!");
        User user = null;
        int i = 0;
        while (user == null) {
            String email = IOManager.getStringInput("Enter your Alset email:");
            String password = IOManager.getPasswordInput("Enter your Alset password:");
            boolean valid = DBManager.validLoginData(email, password);
            if (valid) {
                HashSet<Vehicle> customerVehicles = DBManager.getVehicles(email);
                if (customerVehicles == null) {
                    MenuManager.setNextMessage("Unable to load data. Please try again.");
                    return;
                } else if (!customerVehicles.contains(v)) {
                    MenuManager.setNextMessage("Error: Customer does not own the vehicle with the SN " + sn);
                    return;
                } else {
                    String[] name = DBManager.getName(email);
                    if (name == null) {
                        MenuManager.setNextMessage("Unable to load data. Please try again.");
                        return;
                    } else {
                        user = new User(name[0], name[1], name[2], email, password, customerVehicles);
                    }
                }
            } else if (i == 4) {
                IOManager.clear("Failed too many times. Please return console to employee.");
                IOManager.getStringInput("Enter any value to continue:");
                return;
            } else {
                IOManager.clear("Invalid Username/Password. Please try again.");
                i++;
            }
        }
        IOManager.clear("Please confirm the following information:");

        // customer confirmation
        while (true) {
            // customer info
            IOManager.println("Customer Info: ");
            IOManager.println("\tName: " + user.getFirst() + " " + user.getMiddle() + " " + user.getLast());
            IOManager.println("\tEmail: " + user.getEmail());

            // Vehicle Info
            IOManager.println("\nVehicle Info: ");
            IOManager.println("\tSerial Number: " + v.getSerialNum());
            IOManager.println("\tModel Name: " + v.getModelName());
            IOManager.println("\tModel Year: " + v.getYear());

            // Service Info
            IOManager.println("\nService Info: ");
            IOManager.println("\tService Location: " + loc.getName());
            IOManager.println("\tService Requested: " + type);
            IOManager.println("\tService Price: $" + price);
            IOManager.println("\tService Duration: " + time + " hours");
            IOManager.println();
            // get confirmation
            String confirm = IOManager.getStringInput("Is this information correct? (y/n):");
            if (confirm.equals("y")) {
                IOManager.clear("Information has been confirmed as correct. Return console to employee.");
                IOManager.getStringInput("Enter any value to continue:");
                break;
            } else if (confirm.equals("n")) {
                IOManager.clear("Information is incorrect. Return console to employee.");
                IOManager.getStringInput("Enter any value to continue:");
                return;
            } else {
                IOManager.clear("Invalid response, please try again.");
            }
        }

        // employee confirmation
        IOManager.clear("Please confirm the following information:");
        while (true) {
            // customer info
            IOManager.println("Customer Info: ");
            IOManager.println("\tName: " + user.getFirst() + " " + user.getMiddle() + " " + user.getLast());
            IOManager.println("\tEmail: " + user.getEmail());

            // Vehicle Info
            IOManager.println("\nVehicle Info: ");
            IOManager.println("\tSerial Number: " + v.getSerialNum());
            IOManager.println("\tModel Name: " + v.getModelName());
            IOManager.println("\tModel Year: " + v.getYear());

            // Service Info
            IOManager.println("\nService Info: ");
            IOManager.println("\tService Location: " + loc.getName());
            IOManager.println("\tService Requested: " + type);
            IOManager.println("\tService Price: $" + price);
            IOManager.println("\tService Duration: " + time + " hours");
            IOManager.println();
            // get confirmation
            String confirm = IOManager.getStringInput("Is this information correct? (y/n):");
            if (confirm.equals("y")) {
                break;
            } else if (confirm.equals("n")) {
                IOManager.clear("Information is incorrect.");
                IOManager.getStringInput("Enter any value to continue:");
                return;
            } else {
                IOManager.clear("Invalid response, please try again.");
            }
        }

        long length = (long) (time * 60.0 * 60.0);
        boolean success = DBManager.addGarageVehicle(loc, v, type, user.getEmail(), price, length);

        if (success) {
            IOManager.clear("Service request has been accepted.");
            IOManager.println("Bring the vehicle into your garage and begin services.");
        } else {
            IOManager.clear("Error while processing service request, Please try again.");
        }
        IOManager.getStringInput("Enter any value to continue:");


    }

    /**
     * Allows a service manager to log into their service location.
     */
    static void serviceManagerSequence() {
        // clear console
        IOManager.clear("");

        String password = IOManager.getPasswordInput("Enter password:");
        ServiceLocation location = DBManager.getServiceLocation(password);
        if (location == null) {
            MenuManager.setNextMessage("Invalid password.");
        } else {
            ServiceManager.setCurrent(location);
            MenuManager.deleteMenu(Key.SERVICE_MANAGER_MENU);
            MenuManager.createMenu(Key.SERVICE_MANAGER_MENU, location.getName(),
                    new MenuOption("Location Overview", Sequences::locationOverviewSequence),
                    new MenuOption("Manage Showroom \t//todo add", () -> {
                    }),//todo add
                    new MenuOption("Manage Listings\t//todo add", () -> {
                    }),//todo add
                    new MenuOption("Manage Garage", () -> MenuManager.showMenu(Key.MANAGE_GARAGE_MENU, "")),
                    new MenuOption("Log Out", Sequences::serviceManagerLogoutSequence),
                    new MenuOption("Exit Program", Sequences::exitSequence)

            );

            MenuManager.showMenu(Key.SERVICE_MANAGER_MENU, "Successfully logged in as " + location.getName() + " Service Manager.");
        }
    }

    /**
     * Allows a service manager to view all the vehicles
     * currently in their garage.
     */
    static void viewGarageSequence() {
        // get all vehicles being repaired or ready for pickup in the garage
        ServiceLocation loc = ServiceManager.getCurrent();
        HashSet<GarageData> data = DBManager.getGarageData(loc);
        if (data == null) {
            MenuManager.setNextMessage("Unable to load info.");
            return;
        }
        if (data.size() == 0) {
            MenuManager.setNextMessage("Garage is empty.");
        }
        MenuManager.deleteMenu(Key.VIEW_GARAGE_MENU);
        MenuManager.createMenu(Key.VIEW_GARAGE_MENU, "Garage Vehicles");
        MenuManager.addOption(Key.VIEW_GARAGE_MENU, new MenuOption("Return to Previous Menu", () -> MenuManager.showPrevious("")));
        for (GarageData gd : data) {
            String sn = gd.getSerialNum();
            Vehicle v = DBManager.getVehicle(sn);
            if (v == null) {
                MenuManager.setNextMessage("Unable to load info.");
                return;
            }
            MenuManager.addOption(
                    Key.VIEW_GARAGE_MENU,
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
                                if (gd.getReason().equals("Vehicle Purchase"))
                                    IOManager.println("\tService Provided: Manufacturing & Delivering Vehicle");
                                else IOManager.println("\tService Provided: " + gd.getReason());
                                // pause until user enters value
                                IOManager.println();
                                IOManager.getStringInput("Enter any value to continue:");
                            }
                    )
            );
        }
        MenuManager.showMenu(Key.VIEW_GARAGE_MENU, "");
    }

    /**
     * Allows a service manager to overview information
     * about their service location.
     */
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

    /**
     * Allows a service manager to log out of their service location.
     */
    static void serviceManagerLogoutSequence() {
        IOManager.println("Logging out of " + ServiceManager.getCurrent().getName() + "...");
        ServiceManager.logout();
        MenuManager.showMenu(Key.ALSET_LOGIN_MENU, "Successfully logged out.");
    }

    /**
     * Closes the database connection
     * and displays the edgar1 login menu.
     */
    static void endConnectionSequence() {
        IOManager.println("Closing Connection...");
        ConnectionManager.closeConnection();
        MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, "Connection Successfully closed.");
    }

    /**
     * Allows the user to overview information about a given vehicle.
     *
     * @param v: Vehicle
     */
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


    /**
     * Allows the service manager to remove a vehicle from their garage,
     * returning it to its owner.
     * <p>
     * To pick up their vehicle, the owner must confirm their identity
     * and pay for the provided service.
     */
    public static void removeGarageVehicleSequence() {
        ServiceLocation loc = ServiceManager.getCurrent();

        // Get finished vehicles in garage
        HashSet<GarageData> data = DBManager.getGarageData(loc);
        if (data == null) {
            MenuManager.setNextMessage("Unable to load info.");
            return;
        }

        // remove unfinished vehicles from set
        for (GarageData gd : new HashSet<>(data)) {
            if (!gd.isReady()) data.remove(gd);
        }

        // check if there are no finished vehicles
        if (data.size() == 0) {
            MenuManager.setNextMessage("There are no vehicles ready for pickup in the garage.");
            return;
        }

        MenuManager.deleteMenu(Key.REMOVE_GARAGE_VEHICLE_MENU);
        MenuManager.createMenu(Key.REMOVE_GARAGE_VEHICLE_MENU, "Remove Garage Vehicles");
        MenuManager.addOption(Key.REMOVE_GARAGE_VEHICLE_MENU, new MenuOption("Return to Previous Menu", MenuManager::showPrevious));
        for (GarageData gd : data) {
            String sn = gd.getSerialNum();
            Vehicle v = DBManager.getVehicle(sn);
            if (v == null) {
                MenuManager.setNextMessage("Unable to load info.");
                return;
            }
            MenuManager.addOption(
                    Key.REMOVE_GARAGE_VEHICLE_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " (SN: " + v.getSerialNum() + ")",
                            () -> {
                                IOManager.clear("Please hand the console over to the customer.");
                                IOManager.getStringInput("Enter any value to continue:");
                                IOManager.clear("Welcome to " + loc.getName() + "!");
                                User user = null;
                                int i = 0;
                                while (user == null) {
                                    String email = IOManager.getStringInput("Enter your Alset email:");
                                    String password = IOManager.getPasswordInput("Enter your Alset password:");
                                    boolean valid = DBManager.validLoginData(email, password);
                                    if (valid) {
                                        HashSet<Vehicle> customerVehicles = DBManager.getVehicles(email);
                                        if (customerVehicles == null) {
                                            MenuManager.setNextMessage("Unable to load data. Please try again.");
                                            return;
                                        } else if (!customerVehicles.contains(v)) {
                                            MenuManager.setNextMessage("Error: Customer does not own the vehicle with the SN " + sn);
                                            return;
                                        } else {
                                            String[] name = DBManager.getName(email);
                                            if (name == null) {
                                                MenuManager.setNextMessage("Unable to load data. Please try again.");
                                                return;
                                            } else {
                                                user = new User(name[0], name[1], name[2], email, password, customerVehicles);
                                            }
                                        }
                                    } else if (i == 4) {
                                        IOManager.clear("Failed too many times. Please return console to employee.");
                                        IOManager.getStringInput("Enter any value to continue:");
                                        return;
                                    } else {
                                        IOManager.clear("Invalid Username/Password. Please try again.");
                                        i++;
                                    }
                                }
                                IOManager.clear("Please confirm the following information:");

                                // customer confirmation
                                while (true) {
                                    // customer info
                                    IOManager.println("Customer Info: ");
                                    IOManager.println("\tName: " + user.getFirst() + " " + user.getMiddle() + " " + user.getLast());
                                    IOManager.println("\tEmail: " + user.getEmail());

                                    // Vehicle Info
                                    IOManager.println("\nVehicle Info: ");
                                    IOManager.println("\tSerial Number: " + v.getSerialNum());
                                    IOManager.println("\tModel Name: " + v.getModelName());
                                    IOManager.println("\tModel Year: " + v.getYear());

                                    // Service Info
                                    IOManager.println("\nService Info: ");
                                    IOManager.println("\tService Location: " + loc.getName());
                                    IOManager.println("\tService Requested: " + gd.getReason());
                                    IOManager.println("\tService Price: $" + gd.getRepairPrice());
                                    IOManager.println();
                                    // get confirmation
                                    String confirm = IOManager.getStringInput("Is this information correct? (y/n):");
                                    if (confirm.equals("y")) {
                                        IOManager.clear("Information has been confirmed as correct. Return console to employee.");
                                        IOManager.getStringInput("Enter any value to continue:");
                                        break;
                                    } else if (confirm.equals("n")) {
                                        IOManager.clear("Information is incorrect. Return console to employee.");
                                        IOManager.getStringInput("Enter any value to continue:");
                                        return;
                                    } else {
                                        IOManager.clear("Invalid response, please try again.");
                                    }
                                }


                                IOManager.println("Please return the console to the employee.");
                                IOManager.getStringInput("Enter any value to continue:");

                                // employee confirmation
                                IOManager.clear("Please confirm the following information:");
                                while (true) {
                                    // customer info
                                    IOManager.println("Customer Info: ");
                                    IOManager.println("\tName: " + user.getFirst() + " " + user.getMiddle() + " " + user.getLast());
                                    IOManager.println("\tEmail: " + user.getEmail());

                                    // Vehicle Info
                                    IOManager.println("\nVehicle Info: ");
                                    IOManager.println("\tSerial Number: " + v.getSerialNum());
                                    IOManager.println("\tModel Name: " + v.getModelName());
                                    IOManager.println("\tModel Year: " + v.getYear());

                                    // Service Info
                                    IOManager.println("\nService Info: ");
                                    IOManager.println("\tService Location: " + loc.getName());
                                    IOManager.println("\tService Requested: " + gd.getReason());
                                    IOManager.println("\tService Price: $" + gd.getRepairPrice());
                                    IOManager.println();
                                    // get confirmation
                                    String confirm = IOManager.getStringInput("Is this information correct? (y/n):");
                                    if (confirm.equals("y")) {
                                        break;
                                    } else if (confirm.equals("n")) {
                                        IOManager.clear("Information is incorrect.");
                                        IOManager.getStringInput("Enter any value to continue:");
                                        return;
                                    } else {
                                        IOManager.clear("Invalid response, please try again.");
                                    }
                                }

                                IOManager.clear("Please give the console to the customer for payment.");
                                IOManager.getStringInput("Enter any value to continue:");

                                // get card
                                Card card = getCardSequence(user.getEmail());
                                if (card == null) {
                                    IOManager.clear("Unable to load selected card. Please return console to employee.");
                                    IOManager.getStringInput("Enter any value to continue:");
                                    return;
                                } else {
                                    IOManager.println("Card Selected: " + card.getNumCensored());
                                }

                                // complete repair transaction
                                boolean success = DBManager.removeGarageVehicle(gd, card);
                                if (success) {
                                    IOManager.clear("Transaction has been completed.");
                                    IOManager.println("The customer may claim their vehicle.");
                                    IOManager.getStringInput("Enter any value to continue:");
                                } else {
                                    MenuManager.setNextMessage("Failed to update database, please try again.");
                                }

                            }
                    )
            );
        }

    }

    private static Card getCardSequence(String email) {
        CardManager.setSelected(null);
        MenuInitializer.reloadSelectCardMenu(email);
        MenuManager.showMenuOnce(Key.SELECT_CARD_MENU, "");
        return CardManager.getSelected();

    }

    public static boolean addNewCardSequence() {
        String num = null;
        while (num == null) {
            num = IOManager.getPasswordInput("Enter 16-digit credit card number:");
            if (!num.matches("\\d{16}")) {
                num = null;
                IOManager.clear("Number must be a 16-digit sequence, please try again.");
            }
        }
        String first = IOManager.getStringInput("Enter first name on card:");
        String middle = IOManager.getStringInput("Enter middle name on card, or 'N/A' if none:");
        String last = IOManager.getStringInput("Enter last name on card:");
        String cvv = null;
        while (cvv == null) {
            cvv = IOManager.getStringInput("Enter Card Verification Value:");
            if (!cvv.matches("\\d{3,4}")) {
                cvv = null;
                IOManager.clear("Value must be a 3-or-4-digit sequence, please try again.");
            }
        }
        Integer expMonth = null;
        while (expMonth == null) {
            expMonth = IOManager.getIntInput("Enter Expiration month (MM):", 1, 12);
            if (expMonth == null) {
                IOManager.clear("Number must be an integer from 1 to 12");
            }
        }
        Integer expYear = null;
        while (expYear == null) {
            expYear = IOManager.getIntInput("Enter Expiration month (YY):", 0, 99);
            if (expYear == null) {
                IOManager.clear("Number must be an integer from 0 to 99");
            }
        }
        String zip = null;
        while (zip == null) {
            zip = IOManager.getStringInput("Enter card Zip/postal code:");
            if (!cvv.matches("\\d{5,10}")) {
                cvv = null;
                IOManager.clear("Code must be a 5-to-10-digit sequence, please try again.");
            }
        }
        String type = null;
        while (type == null) {
            type = IOManager.getStringInput("Enter card type (d for debit, c for credit):");
            if (!type.equals("d") && !type.equals("c")) {
                type = null;
                IOManager.clear("Invalid input. Valid options are 'd' or 'c', please try again.");
            }
        }
        if (type.equals("d")) type = "Debit";
        else type = "Credit";

        Card card = new Card(num, cvv, expMonth, expYear, zip, type, first, middle, last);

        boolean success = DBManager.addNewCard(card);
        if (!success) {
            MenuManager.setNextMessage("Unable to load data, please try again.");
            return false;
        }
        CardManager.setSelected(card);
        return true;
    }
}
