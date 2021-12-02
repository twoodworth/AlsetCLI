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
import vehicle.VehicleSelections;

import java.sql.Connection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        IOManager.clear();

        // get credentials
        String email = IOManager.getStringInput("Enter your Alset email:");
        String password = IOManager.getPasswordInput("Enter your Alset password:");
        boolean success = UserManager.login(email, password);

        if (success) {
            User current = UserManager.getCurrent();
            System.out.println(current.getVehicles().size());//todo remove

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
        IOManager.clear();

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

        // Log user out + display login menu
        UserManager.logout();
        MenuManager.showMenu(Key.ALSET_LOGIN_MENU, "Successfully logged out.");
    }

    /**
     * Exits the program.
     */
    static void exitSequence() {
        IOManager.clear();
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
        IOManager.clear();

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

    /**
     * Allows a service manager to formally complete servicing
     * a vehicle in their service location's garage
     */
    static void finishGarageVehicleSequence() {
        ServiceLocation loc = ServiceManager.getCurrent();

        // Get unfinished vehicles in garage
        Set<GarageData> data = DBManager.getUnfinishedVehicleGarageData(loc);
        if (data == null) {
            MenuManager.setNextMessage("Unable to load info.");
            return;
        }

        // check if there are no unfinished vehicles
        if (data.size() == 0) {
            MenuManager.setNextMessage("There are no unfinished vehicles in the garage.");
            return;
        }

        MenuManager.showMenu(Key.FINISH_GARAGE_VEHICLE_MENU);
    }

    /**
     * Used for having the user confirm if the given vehicle info is correct
     *
     * @param data: Data to confirm
     * @param v:    Vehicle that the data is related to
     * @return true if the user confirms, otherwise false
     */
    static boolean confirmVehicleInfo(GarageData data, Vehicle v) {
        String sn = data.getSerialNum();
        Set<String> options = DBManager.getOptions(sn);

        IOManager.clear();
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("\t").append("Serial Number: ").append(sn).append("\n");
        infoBuilder.append("\t").append("Model: ").append(v.getModelName()).append("\n");
        infoBuilder.append("\t").append("Year: ").append(v.getYear()).append("\n");
        infoBuilder.append("\t").append("Additional Features: ");
        if (options == null || options.isEmpty()) infoBuilder.append("None");
        else for (String option : options) infoBuilder.append("\n\t\t- ").append(option);
        infoBuilder.append("\n\n").append("Is all the above information correct?");

        // determine if correct
        return IOManager.getBooleanInput(infoBuilder.toString());
    }

    /**
     * Allows a service manager to add a vehicle into their garage.
     */
    static void addGarageVehicleSequence() {
        ServiceLocation loc = ServiceManager.getCurrent();

        // clear console
        IOManager.clear();

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
        Set<Model> repairable = DBManager.getRepairableModels(loc);

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
            time = IOManager.getDoubleInput("Enter Expected Service Duration (as a decimal, in hours):", 0.0, Double.MAX_VALUE);
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
                Set<Vehicle> customerVehicles = DBManager.getVehicles(email);
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
            String middle = " ";
            if (user.getMiddle() != null) middle = " " + user.getMiddle() + " ";
            IOManager.println("\tName: " + user.getFirst() + middle + user.getLast());
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
            String middle = " ";
            if (user.getMiddle() != null) middle = " " + user.getMiddle() + " ";
            IOManager.println("\tName: " + user.getFirst() + middle + user.getLast());
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
        IOManager.clear();

        String password = IOManager.getPasswordInput("Enter password:");
        ServiceLocation location = DBManager.getServiceLocation(password);
        if (location == null)
            MenuManager.setNextMessage("Invalid password.");
        else {
            ServiceManager.setCurrent(location);
            MenuManager.showMenu(Key.SERVICE_MANAGER_MENU, "Successfully logged in as " + location.getName() + " Service Manager.");
        }
    }

    /**
     * Allows a service manager to overview information
     * about their service location.
     */
    static void locationOverviewSequence() {
        // get location
        ServiceLocation location = ServiceManager.getCurrent();

        // show info
        viewLocationOverview(location);
    }

    /**
     * Prints out an overview of the given service location
     *
     * @param location: Service Location
     */
    public static void viewLocationOverview(ServiceLocation location) {
        if (location == null) {
            IOManager.clear("Unable to load info.");
            IOManager.getStringInput("Enter any value to continue:");
            return;
        }

        Set<Model> models = DBManager.getRepairableModels(location);

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
        IOManager.clear();

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
        Set<String> options = DBManager.getOptions(sn);

        // get condition
        Condition condition;

        if (v.isManufactured()) {
            condition = v.getCondition();
            if (condition == null) {
                MenuManager.setNextMessage("Error loading vehicle overview.");
                return;
            }
        } else {
            condition = null;
        }


        ServiceLocation location = DBManager.getServiceLocation(v);

        //clear console
        IOManager.clear();

        // Print service status
        IOManager.println("\nVehicle Overview:");
        if (condition == null) {
            IOManager.println("VEHICLE IS CURRENTLY BEING MANUFACTURED.");
        } else if (location != null) {
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
        if (condition != null) {
            IOManager.println("\tMileage: " + condition.getMileage() + " miles");
            IOManager.println("\tLast Inspection: " + condition.getLastInspectionFormatted());
            IOManager.println("\tHas Detected Damage: " + condition.hasDamage());
        }

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

    public static void productManagerSequence() {
        // clear console
        IOManager.clear();

        String password = IOManager.getPasswordInput("Enter password:");
        boolean correct = DBManager.correctProductManagerPassword(password);
        if (correct) {
            MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU, "Successfully logged in as Product Manager.");
        } else {
            MenuManager.setNextMessage("Invalid password.");
        }
    }

    /**
     * Used in order to construct a Card object based on user inputs
     *
     * @param email: Email of user
     * @return card
     */
    static Card getCardSequence(String email) {
        CardManager.setSelected(null);
        ServiceManager.setCurrentEmail(email);
        MenuManager.showMenuOnce(Key.SELECT_CARD_MENU);
        return CardManager.getSelected();

    }

    /**
     * Used for getting the user that is picking up a vehicle
     *
     * @param v: Vehicle being picked up
     * @return user
     */
    static User getPickupUserSequence(Vehicle v) {
        User user = null;
        int i = 0;
        while (user == null) {
            String email = IOManager.getStringInput("Enter your Alset email:");
            String password = IOManager.getPasswordInput("Enter your Alset password:");
            boolean valid = DBManager.validLoginData(email, password);
            if (valid) {
                Set<Vehicle> customerVehicles = DBManager.getVehicles(email);
                if (customerVehicles == null) {
                    return null;
                } else if (!customerVehicles.contains(v)) {
                    MenuManager.setNextMessage("Error: Customer does not own the vehicle with the SN " + v.getSerialNum());
                    return null;
                } else {
                    String[] name = DBManager.getName(email);
                    if (name == null) {
                        MenuManager.setNextMessage("Unable to load data. Please try again.");
                        return null;
                    } else {
                        user = new User(name[0], name[1], name[2], email, password, customerVehicles);
                    }
                }
            } else if (i == 4) {
                IOManager.clear("Failed too many times. Please return console to employee.");
                IOManager.getStringInput("Enter any value to continue:");
                return null;
            } else {
                IOManager.clear("Invalid Username/Password. Please try again.");
                i++;
            }
        }
        return user;
    }

    /**
     * Allows the user to add a new card to the database.
     *
     * @return true if successfully added, otherwise false
     */
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
            if (!zip.matches("\\d{5,10}")) {
                zip = null;
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

        Card card = new Card(num, cvv, expMonth, expYear + 2000, zip, type, first, middle, last);

        boolean success = DBManager.addNewCard(card);
        if (!success) {
            MenuManager.setNextMessage("Unable to load data, please try again.");
            return false;
        }
        CardManager.setSelected(card);
        return true;
    }

    /**
     * Provides the user with their purchase history
     */
    public static void purchaseHistorySequence() {
        User current = UserManager.getCurrent();
        List<String> purchases = DBManager.getTransactionList(current.getEmail());
        if (purchases == null) {
            MenuManager.setNextMessage("Unable to load data, please try again.");
            return;
        }
        if (purchases.size() == 0) {
            MenuManager.setNextMessage("You have made not made any purchases.");
            return;
        }
        IOManager.clear("Vehicle Purchase History:");
        for (String purchase : purchases) {
            IOManager.println(purchase);
        }
        IOManager.println();
        IOManager.getStringInput("Enter any value to continue:");
    }

    /**
     * Allows a service manager to order a new vehicle for their showroom.
     */
    public static void orderShowroomVehicleSequence() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_MODEL_MENU);
        MenuManager.showMenuOnce(Key.SELECT_YEAR_MENU);
        MenuManager.showMenuOnce(Key.SELECT_OPTIONS_MENU);
        Model model = new Model(VehicleSelections.getYear(), VehicleSelections.getModelName());


        boolean success = DBManager.orderShowroomVehicle(model, VehicleSelections.getOptions(), ServiceManager.getCurrent());

        if (success) {
            IOManager.clear("Order completed. Expect vehicle to arrive within the next 1-2 months.");
            IOManager.getStringInput("Enter any value to continue:");
            MenuManager.showMenu(Key.MANAGE_SHOWROOM_MENU);
        } else {
            MenuManager.setNextMessage("Failed to update database, please try again.");
        }

    }

    /**
     * Allows a service manager to sell a listed vehicle to a customer
     *
     * @param v:     Vehicle being sold
     * @param price: Sales price
     */
    public static void sellListedVehicleSequence(Vehicle v, long price) {
        VehicleSelections.reset();
        IOManager.clear("Please hand the console over to the customer.");
        IOManager.getStringInput("Enter any value to continue:");
        IOManager.clear("Welcome to " + ServiceManager.getCurrent().getName() + "!");
        int i = 0;
        String email;
        while (true) {
            email = IOManager.getStringInput("Enter your Alset email:");
            String password = IOManager.getPasswordInput("Enter your Alset password:");
            boolean valid = DBManager.validLoginData(email, password);
            if (valid) {
                break;
            } else if (i == 4) {
                IOManager.clear("Failed too many times. Please return console to employee.");
                IOManager.getStringInput("Enter any value to continue:");
                return;
            } else {
                IOManager.clear("Invalid Username/Password. Please try again.");
                i++;
            }
        }

        Card card = getCardSequence(email);
        String[] name = DBManager.getName(email);
        Set<String> options = DBManager.getOptions(v.getSerialNum());
        if (name == null || card == null || options == null) {
            MenuManager.setNextMessage("Unable to load data.");
            return;
        }

        IOManager.clear("Please confirm the following information:");

        // customer confirmation
        while (true) {
            // customer info
            String middle = " ";
            if (name[1] != null) middle = " " + name[1] + " ";
            IOManager.println("Customer Info: ");
            IOManager.println("\tName: " + name[0] + middle + name[2]);
            IOManager.println("\tEmail: " + email);

            // Vehicle Info
            IOManager.println("\nVehicle Info: ");
            IOManager.println("\tSerial Number: " + v.getSerialNum());
            IOManager.println("\tModel Name: " + v.getModelName());
            IOManager.println("\tModel Year: " + v.getYear());
            if (options.isEmpty()) IOManager.println("\tAdditional Options: None");
            else {
                IOManager.println("\tAdditional Options:");
                for (String s : options) {
                    IOManager.println("\t\t- " + s);
                }
            }

            // Transaction info
            IOManager.println("\nTransaction Info: ");
            IOManager.println("Vehicle Cost: $" + price);
            IOManager.println("Card: " + card.getNumCensored() + "\n");

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
            IOManager.println("\tName: " + name[0] + " " + name[1] + " " + name[2]);
            IOManager.println("\tEmail: " + email);

            // Vehicle Info
            IOManager.println("\nVehicle Info: ");
            IOManager.println("\tSerial Number: " + v.getSerialNum());
            IOManager.println("\tModel Name: " + v.getModelName());
            IOManager.println("\tModel Year: " + v.getYear());
            if (options.isEmpty()) IOManager.println("\tAdditional Options: None");
            else {
                IOManager.println("\tAdditional Options:");
                for (String s : options) {
                    IOManager.println("\t\t- " + s);
                }
            }

            // Transaction info
            IOManager.println("\nTransaction Info: ");
            IOManager.println("Vehicle Cost: $" + price);
            IOManager.println("Card: " + card.getNumCensored() + "\n");


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

        boolean success = DBManager.sellListedVehicle(email, price, v, card);

        if (success) {
            IOManager.clear("Vehicle has been sold.");
            IOManager.println("Customer may now claim it as their own.");
        } else {
            IOManager.clear("Error while processing service request, Please try again.");
        }
        IOManager.getStringInput("Enter any value to continue:");
        MenuManager.showMenu(Key.MANAGE_LISTINGS_MENU);
    }

    /**
     * Allows a user to purchase a new vehicle
     */
    public static void purchaseVehicleSequence() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_MODEL_MENU);
        MenuManager.showMenuOnce(Key.SELECT_YEAR_MENU);
        MenuManager.showMenuOnce(Key.SELECT_OPTIONS_MENU);
        MenuManager.showMenuOnce(Key.SELECT_LOCATION_MENU);

        Model model = new Model(VehicleSelections.getYear(), VehicleSelections.getModelName());
        Long cost = DBManager.getModelCost(model);
        if (cost == null) {
            MenuManager.setNextMessage("Unable to load info, please try again.");
            return;
        }
        StringBuilder sb = new StringBuilder(model.getYear() + " Model " + model.getName() + " Price:\n");
        String format = "%12s";
        sb.append("Base Price\t").append(String.format(format, "$" + cost)).append("\n");
        HashSet<String> options = VehicleSelections.getOptions();
        for (String option : options) {
            Long optionCost = DBManager.getOptionCost(option);
            if (optionCost == null) {
                MenuManager.setNextMessage("Unable to load info, please try again.");
                return;
            }
            cost += optionCost;
            sb.append(option).append("\t").append(String.format(format, "$" + optionCost)).append("\n");
        }
        sb.append("\n").append("Total Cost:\t").append(String.format(format, "$" + cost));


        CardManager.setSelected(null);
        MenuManager.showMenuOnce(Key.COMPLETE_ORDER_MENU, sb.toString());

        Card card = CardManager.getSelected();
        if (card == null) return;
        IOManager.clear("Card Selected: " + card.getNumCensored());
        IOManager.getStringInput("Enter any value to complete transaction: ");

        // complete transaction
        ServiceLocation location = VehicleSelections.getLocation();
        boolean success = DBManager.purchaseVehicle(model, options, location, card, cost, UserManager.getCurrent());
        if (success) {
            IOManager.clear("Purchase completed. You will receive an email when your vehicle is ready for pickup.");
            IOManager.getStringInput("Enter any value to continue:");
        } else {
            MenuManager.setNextMessage("Failed to update database, please try again.");
        }
        MenuManager.showMenu(Key.CUSTOMER_MENU);
    }

    /**
     * Allows a service manager to add a model to their set of repairable models
     */
    public static void addRepairableModelSequence() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_UNREPAIRABLE_MODEL_MENU);
        MenuManager.showMenuOnce(Key.SELECT_UNREPAIRABLE_YEAR_MENU);
        String name = VehicleSelections.getModelName();
        int year = VehicleSelections.getYear();
        Model model = new Model(year, name);
        boolean success = DBManager.addRepairableModel(model);
        if (success) {
            MenuManager.showMenu(Key.MANAGE_GARAGE_MENU, year + " Model " + name + " is now repairable.");
        } else {
            MenuManager.showMenu(Key.MANAGE_GARAGE_MENU, "Failed to add model, please try again");
        }
    }

    /**
     * Allows a service manager to remove a model from their set of repairable models
     */
    public static void removeRepairableModelSequence() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_REPAIRABLE_MODEL_MENU);
        MenuManager.showMenuOnce(Key.SELECT_REPAIRABLE_YEAR_MENU);
        String name = VehicleSelections.getModelName();
        int year = VehicleSelections.getYear();
        Model model = new Model(year, name);
        boolean success = DBManager.removeRepairableModel(model);
        if (success) {
            MenuManager.showMenu(Key.MANAGE_GARAGE_MENU, year + " Model " + name + " is no longer repairable.");
        } else {
            MenuManager.showMenu(Key.MANAGE_GARAGE_MENU, "Failed to remove model, please try again.");
        }
    }

    public static void createNewAccountSequence() {
        IOManager.clear("Creating a new account.");
        String email;

        // get email, check if already used or invalid
        while (true) {
            email = IOManager.getStringInput("Enter your email address:");
            Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(email);
            if (DBManager.emailExists(email)) {
                IOManager.clear("Email already exists.");
                boolean again = IOManager.getBooleanInput("Do you want to try again?");
                if (!again) {
                    return;
                }
            } else if (!matcher.find()) {
                IOManager.clear("Email is invalid.");
                boolean again = IOManager.getBooleanInput("Do you want to try again?");
                if (!again) {
                    return;
                }
            } else {
                break;
            }
        }

        String key = User.getRandomPassword();
        while (true) {
            IOManager.clear("Email sent to " + email + ": Your validation key is " + key);
            IOManager.println("A validation key has been sent to " + email);
            String inputKey = IOManager.getStringInput("Enter the key:");
            if (inputKey.equals(key)) {
                IOManager.clear("Key is correct.");
                break;
            } else {
                IOManager.clear("Key is not valid.");
                boolean again = IOManager.getBooleanInput("Do you want to try again?");
                if (!again) {
                    return;
                }
            }
        }

        // get name
        String first = IOManager.getStringInput("Enter your legal first name:");
        String middle = IOManager.getStringInput("Enter your legal middle name (or N/A) if none:");
        String last = IOManager.getStringInput("Enter your legal last name:");

        //enter address
        IOManager.clear(("What is your address? Enter 'N/A' where not applicable."));
        String planet = IOManager.getStringInput("Enter your Planet:");
        String country = IOManager.getStringInput("Enter your Country:");
        String state = IOManager.getStringInput("Enter your State:");
        String city = IOManager.getStringInput("Enter your City:");
        String street = IOManager.getStringInput("Enter your Street Number:");
        String zip = IOManager.getStringInput("Enter your ZIP Code:");
        String apartment = IOManager.getStringInput("Enter your apartment room:");

        Address address = new Address(planet, country, state, city, street, zip, apartment);

        while (true) {
            String password = IOManager.getPasswordInput("Enter password:");
            String confirmation = IOManager.getPasswordInput("Confirm password: ");
            if (password.equals(confirmation)) {
                boolean success = DBManager.createAccount(email, first, middle, last, address, password);
                if (success) {
                    IOManager.clear("Account has been created. You may now use it to log in.");
                    IOManager.getStringInput("Enter any value to continue:");
                } else {
                    MenuManager.setNextMessage("Error while creating account, please try again.");
                }
                break;
            } else {
                IOManager.clear("Password does not match confirmation.");
                boolean again = IOManager.getBooleanInput("Do you want to try again?");
                if (!again) {
                    return;
                }
            }
        }
    }

    /**
     * Used for adding a new vehicle model
     */
    public static void addModelSequence() {
        IOManager.clear("Adding a new model");

        String name;
        while (true) {
            name = IOManager.getStringInput("Enter model name:");
            if (name.length() == 1) {
                break;
            } else {
                IOManager.clear("Model names must be exactly 1 character long.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (again) IOManager.clear();
                else return;
            }

        }
        Integer year;
        while (true) {
            year = IOManager.getIntInput("Enter model year:", 2008, Integer.MAX_VALUE);
            if (year == null) {
                IOManager.clear("Invalid input, must be a year after 2007.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (again) IOManager.clear();
                else return;
            } else {
                break;
            }
        }

        Model model = new Model(year, name);
        if (DBManager.getAllModels().contains(model)) {
            IOManager.clear("Model already exists.");
            IOManager.getStringInput("Enter any value to continue:");
            return;
        }

        Long price;
        while (true) {
            price = IOManager.getLongInput("Enter a sales price (or '-1' if not for sale):", -1L, Long.MAX_VALUE);
            if (price == null) {
                IOManager.clear("Invalid input, must be an integer greater than or equal to -1.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (again) IOManager.clear();
                else return;
            } else {
                break;
            }
        }

        if (price == -1) price = null;

        if (price == null) IOManager.clear(year + " Model " + name + " will be created, but not for sale.");
        else IOManager.clear(year + " Model " + name + " will be created and sold for $" + price);


        boolean correct = IOManager.getBooleanInput("Is the above information correct?");

        if (correct) {
            boolean success = DBManager.addModel(year, name, price);
            if (success) MenuManager.setNextMessage("New model added successfully.");
            else MenuManager.setNextMessage("Failed to add new model, please try again.");
        } else {
            MenuManager.setNextMessage("Information was incorrect, model not added.");
        }
    }

    /**
     * Used for adding a new custom option
     */
    public static void addOptionSequence() {

        IOManager.clear("Adding a new customization option.");

        String name = IOManager.getStringInput("Enter option name:");
        Set<String> all = DBManager.getAllOptions();
        if (all == null) {
            IOManager.clear("Error while loading data, please try again.");
            IOManager.getStringInput("Enter any value to continue:");
            return;
        }
        if (all.contains(name)) {
            IOManager.clear("This customization option already exists.");
            IOManager.getStringInput("Enter any value to continue:");
            return;
        }

        Long price;
        while (true) {
            price = IOManager.getLongInput("Enter a sales price (or '-1' if not for sale):", -1L, Long.MAX_VALUE);
            if (price == null) {
                IOManager.clear("Invalid input, must be an integer greater than or equal to 0.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (again) IOManager.clear();
                else return;
            } else {
                break;
            }
        }
        if (price == -1) price = null;

        if (price == null) IOManager.clear("Custom option '" + name + "' will be created, but not for sale.");
        else IOManager.clear("Custom option '" + name + "' will be created and sold for $" + price);


        boolean correct = IOManager.getBooleanInput("Is the above information correct?");
        if (correct) {
            boolean success = DBManager.addOption(name, price);
            if (success) MenuManager.setNextMessage("New option added successfully.");
            else MenuManager.setNextMessage("Failed to add new option, please try again.");
        } else {
            MenuManager.setNextMessage("Information was incorrect, option not added.");
        }

    }

    public static void addServiceLocationSequence() {

        IOManager.clear("Adding a new service location.");

        // location name
        String name;
        while (true) {
            name = IOManager.getStringInput("Enter location name:");
            Set<String> all = DBManager.getServiceLocations().stream().map(ServiceLocation::getName).collect(Collectors.toSet());
            if (all.contains(name)) {
                IOManager.clear("A service location already exists with that name.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (!again) {
                    return;
                }
            } else {
                break;
            }
        }

        // get password
        String password;
        while (true) {
            password = IOManager.getPasswordInput("Enter location password:");
            String confirmation = IOManager.getPasswordInput("Confirm password:");

            if (!password.equals(confirmation)) {
                IOManager.clear("Confirmation does not match password.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (!again) {
                    return;
                }
            } else {
                break;
            }
        }

        // get password
        IOManager.clear(("What is the location's address? Enter 'N/A' where not applicable."));
        String planet = IOManager.getStringInput("Enter Planet:");
        String country = IOManager.getStringInput("Enter Country:");
        String state = IOManager.getStringInput("Enter State:");
        String city = IOManager.getStringInput("Enter City:");
        String street = IOManager.getStringInput("Enter Street Number:");
        String zip = IOManager.getStringInput("Enter ZIP Code:");
        String apartment = IOManager.getStringInput("Enter apartment:");

        Address address = new Address(planet, country, state, city, street, zip, apartment);

        IOManager.clear("Service location will now be added.");
        IOManager.getStringInput("Enter any value to continue:");

        boolean success = DBManager.addLocation(name, password, address);
        if (success) MenuManager.setNextMessage("New location added successfully.");
        else MenuManager.setNextMessage("Failed to add new location, please try again.");
    }

    public static void setModelPrice() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_ALL_MODELS_MENU);
        MenuManager.showMenuOnce(Key.SELECT_ALL_YEARS_MENU);

        Long price;
        while (true) {
            price = IOManager.getLongInput("Enter new price (or '-1' to stop selling):", -1, Long.MAX_VALUE);
            if (price == null) {
                IOManager.clear("Invalid input, must be an integer greater than or equal to -1.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (again) IOManager.clear();
                else MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU);
            } else {
                break;
            }
        }
        if (price == -1) price = null;
        Model model = new Model(VehicleSelections.getYear(), VehicleSelections.getModelName());
        boolean success = DBManager.updateModelPrice(model, price);
        if (success) MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU,"Price updated successfully.");
        else MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU,"Failed to add new location, please try again.");
    }

    public static void setOptionPrice() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_ALL_OPTIONS_MENU);
        String option = new ArrayList<>(VehicleSelections.getOptions()).get(0);

        Long price;
        while (true) {
            price = IOManager.getLongInput("Enter new price (or '-1' to stop selling):", -1, Long.MAX_VALUE);
            if (price == null) {
                IOManager.clear("Invalid input, must be an integer greater than or equal to -1.");
                boolean again = IOManager.getBooleanInput("Try again?");
                if (again) IOManager.clear();
                else MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU);
            } else {
                break;
            }
        }
        if (price == -1) price = null;
        boolean success = DBManager.updateOptionPrice(option, price);
        if (success) MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU, "Price updated successfully.");
        else MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU, "Failed to add new location, please try again.");
    }

    public static void addModelOptionSequence() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_ALL_MODELS_MENU);
        MenuManager.showMenuOnce(Key.SELECT_ALL_YEARS_MENU);
        MenuManager.showMenuOnce(Key.SELECT_ALL_OPTIONS_MENU);

        String name = VehicleSelections.getModelName();
        int year = VehicleSelections.getYear();
        String option = new ArrayList<>(VehicleSelections.getOptions()).get(0);
        Model model = new Model(year, name);

        Set<String> buyable = DBManager.getBuyableOptions(model);
        if (buyable.contains(option)) {
            MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU, "The selected model can already have this option added.");
            return;
        }

        boolean success = DBManager.addModelOption(model, option);
        if (success) MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU, "Option added successfully.");
        else MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU, "Failed to add new location, please try again.");
    }

    public static void recallSequence() {
        VehicleSelections.reset();
        MenuManager.showMenuOnce(Key.SELECT_ALL_MODELS_MENU);
        MenuManager.showMenuOnce(Key.SELECT_ALL_YEARS_MENU);
        MenuManager.showMenuOnce(Key.SELECT_OPTIONS_MENU, "Recalls will only be applied to vehicles with all the selected options.");
        IOManager.clear();
        Model model = new Model(VehicleSelections.getYear(), VehicleSelections.getModelName());
        Set<String> options = VehicleSelections.getOptions();
        String message = IOManager.getStringInput("Enter brief recall message to send via email:");
        Set<String> emails = DBManager.getRecallEmails(model, options);

        if (emails.isEmpty()) {
            IOManager.clear("No customers currently drive the vehicle + options being recalled, so no notifications were sent.");
            IOManager.getStringInput("Enter any value to continue:");
            MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU);
            return;
        }
        IOManager.clear("Recall notification has been sent to all the following emails:");
        for (String email : emails) {
            IOManager.println("\t- " + email);
            /*
                No email actually gets sent here since these are fake emails.
             */
        }
        IOManager.println();
        IOManager.getStringInput("Enter any value to continue:");
        MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU);
    }
}
