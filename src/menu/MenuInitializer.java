package menu;

import card.Card;
import card.CardManager;
import constants.Key;
import database.DBManager;
import io.IOManager;
import location.GarageData;
import location.ServiceLocation;
import location.ServiceManager;
import user.User;
import user.UserManager;
import vehicle.Condition;
import vehicle.Vehicle;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Initializes all of the menus in the program.
 */
public class MenuInitializer {

    /**
     * Whether or not the menus have been initialized
     */
    private static boolean initialized = false;

    /**
     * Initializes all the menus by calling the initializers for all the
     * individual menus.
     */
    public static void initializeMenus() {
        if (!initialized) {
            initializeEdgar1Menu();
            initializeAlsetLoginMenu();
            initializeMainMenu();
            initializeMyVehiclesMenu();
            initializeManageGarageMenu();
            initializeSelectCardMenu();
            initializeFinishGarageVehicleMenu();
            initializeServiceManagerMenu();
            initializeViewGarageMenu();
            initializeRemoveGarageVehicleMenu();
            initializeBrowseLocationsMenu();
            initialized = true;
        }
    }

    /**
     * Initializes the start menu
     */
    private static void initializeAlsetLoginMenu() {
        MenuManager.createMenu(
                Key.ALSET_LOGIN_MENU,
                "Alset Login Menu",
                new MenuOption("Login as Customer", Sequences::alsetLoginSequence),
                new MenuOption("Forgot Customer Password", Sequences::forgotPwdSequence),
                new MenuOption("Create New Customer Account //todo add functionality", Sequences::createAcctSequence),//todo add functionality
                new MenuOption("Browse Service Locations",  () -> MenuManager.showMenu(Key.BROWSE_LOCATIONS_MENU)),
                new MenuOption("Login as Service Manager", Sequences::serviceManagerSequence),
                new MenuOption("Login as Product Manager \t//todo add functionality", Sequences::productManagerSequence),//todo add functionality
                new MenuOption("Close connection", Sequences::endConnectionSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the Edgar1 login menu
     */
    private static void initializeEdgar1Menu() {
        MenuManager.createMenu(
                Key.EDGAR1_LOGIN_MENU,
                "Edgar1 Login Menu",
                new MenuOption("Log Into Edgar1", Sequences::edgar1LoginSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the main menu
     */
    private static void initializeMainMenu() {
        MenuManager.createMenu(
                Key.CUSTOMER_MENU,
                "Alset Main Menu",
                new MenuOption("My Vehicles", () -> MenuManager.showMenu(Key.CUSTOMER_VEHICLES_MENU)),
                new MenuOption("Purchase Vehicles //todo add functionality", () -> {
                }),
                new MenuOption("View Purchase History", Sequences::purchaseHistorySequence),
                new MenuOption("Browse Service Locations", () -> MenuManager.showMenu(Key.BROWSE_LOCATIONS_MENU)),
                new MenuOption("Log Out", Sequences::alsetLogoutSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the Garage Manager menu
     */
    private static void initializeManageGarageMenu() {
        MenuManager.createMenu(
                Key.MANAGE_GARAGE_MENU,
                "Manage Garage",
                new MenuOption("View Vehicles", () -> MenuManager.showMenu(Key.VIEW_GARAGE_MENU)),
                new MenuOption("Add Vehicle", Sequences::addGarageVehicleSequence),
                new MenuOption("Finish Vehicle", Sequences::finishGarageVehicleSequence),
                new MenuOption("Remove Vehicle", () -> MenuManager.showMenu(Key.REMOVE_GARAGE_VEHICLE_MENU)),
                new MenuOption("Add Repairable Model", () -> {
                }),//todo add
                new MenuOption("Remove Repairable Model", () -> {
                }),//todo add
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Key.SERVICE_MANAGER_MENU))
        );
    }

    private static void initializeRemoveGarageVehicleMenu() {
        MenuManager.createMenu(
                Key.REMOVE_GARAGE_VEHICLE_MENU,
                MenuInitializer::reloadRemoveGarageVehicleMenu,
                "Remove Garage Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious));
    }

    private static void reloadRemoveGarageVehicleMenu() {
        int size = MenuManager.getSize(Key.REMOVE_GARAGE_VEHICLE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.REMOVE_GARAGE_VEHICLE_MENU, i);
        ServiceLocation loc = ServiceManager.getCurrent();
        HashSet<GarageData> data = DBManager.getFinishedVehicleGarageData(loc);
        if (data == null) return;
        for (GarageData gd : data) {
            String sn = gd.getSerialNum();
            Vehicle v = DBManager.getVehicle(sn);
            if (v == null) {
                MenuManager.setNextMessage("Unable to load info.");
                return;
            }
            MenuManager.addOption(//todo optimize
                    Key.REMOVE_GARAGE_VEHICLE_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " (SN: " + v.getSerialNum() + ")",
                            () -> {
                                IOManager.clear("Please hand the console over to the customer.");
                                IOManager.getStringInput("Enter any value to continue:");
                                IOManager.clear("Welcome to " + loc.getName() + "!");
                                User user = Sequences.getPickupUserSequence(v);
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
                                Card card = Sequences.getCardSequence(user.getEmail());
                                if (card == null) {
                                    IOManager.clear("Unable to load selected card. Please return console to employee.");
                                    IOManager.getStringInput("Enter any value to continue:");
                                    return;
                                } else {
                                    IOManager.println("Card Selected: " + card.getNumCensored());
                                    IOManager.getStringInput("Enter any value to complete transaction: ");
                                }

                                // complete repair transaction
                                boolean success = DBManager.removeGarageVehicle(gd, card);
                                if (success) {
                                    IOManager.clear("Transaction has been completed. You may now claim your vehicle.");
                                    IOManager.println("Return console to employee.");
                                    IOManager.getStringInput("Enter any value to continue:");
                                } else {
                                    MenuManager.setNextMessage("Failed to update database, please try again.");
                                }
                                MenuManager.showMenu(Key.MANAGE_GARAGE_MENU);
                            }
                    )
            );
        }
    }

    /**
     * Initializes the 'Select Card' menu
     */
    private static void initializeSelectCardMenu() {
        MenuManager.createMenu(
                Key.SELECT_CARD_MENU,
                MenuInitializer::reloadSelectCardMenu,
                "Select Card",
                new MenuOption("Add New Card", Sequences::addNewCardSequence)
        );
    }


    /**
     * Initializes the 'My Vehicles' menu
     */
    private static void initializeMyVehiclesMenu() {
        MenuManager.createMenu(
                Key.CUSTOMER_VEHICLES_MENU,
                MenuInitializer::reloadMyVehilesMenu,
                "My Vehicles",
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Key.CUSTOMER_MENU))
        );
    }

    private static void reloadMyVehilesMenu() {
        int size = MenuManager.getSize(Key.CUSTOMER_VEHICLES_MENU);
        System.out.println(size);//todo remove
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.CUSTOMER_VEHICLES_MENU, i);
        User current = UserManager.getCurrent();

        // Load in the user's vehicles to the vehicle manager menu.
        HashSet<Vehicle> vehicles = current.getVehicles();
        System.out.println(vehicles.size());//todo remove
        for (Vehicle v : vehicles) {
            String num = v.getSerialNum();
            String s = v.getYear() + " Model " + v.getModelName() + " (SN: " + num + ")";

            // Add menu option to access the vehicle's menu
            MenuManager.addOption(Key.CUSTOMER_VEHICLES_MENU, new MenuOption(s, () -> Sequences.vehicleOverviewSequence(v)));
        }
    }

    private static void initializeServiceManagerMenu() {
        MenuManager.createMenu(
                Key.SERVICE_MANAGER_MENU,
                () -> {
                    ServiceLocation loc = ServiceManager.getCurrent();
                    MenuManager.setTitle(Key.SERVICE_MANAGER_MENU, loc.getName());
                },
                "Name",
                new MenuOption("Location Overview", Sequences::locationOverviewSequence),
                new MenuOption("Manage Showroom \t//todo add", () -> {
                }),//todo add
                new MenuOption("Manage Listings\t//todo add", () -> {
                }),//todo add
                new MenuOption("Manage Garage", () -> MenuManager.showMenu(Key.MANAGE_GARAGE_MENU)),
                new MenuOption("Log Out", Sequences::serviceManagerLogoutSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    private static void initializeViewGarageMenu() {
        MenuManager.createMenu(
                Key.VIEW_GARAGE_MENU,
                MenuInitializer::reloadViewGarageMenu,
                "Garage Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    private static void reloadViewGarageMenu() {
        int size = MenuManager.getSize(Key.VIEW_GARAGE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.VIEW_GARAGE_MENU, i);
        ServiceLocation loc = ServiceManager.getCurrent();
        HashSet<GarageData> data = DBManager.getGarageData(loc);
        if (data == null) return;

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
                                IOManager.clear();
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
    }

    private static void initializeBrowseLocationsMenu() {
        MenuManager.createMenu(
                Key.BROWSE_LOCATIONS_MENU,
                MenuInitializer::reloadBrowseLocationsMenu,
                "Browse Service Locations",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    private static void reloadBrowseLocationsMenu() {
        int size = MenuManager.getSize(Key.BROWSE_LOCATIONS_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.BROWSE_LOCATIONS_MENU, i);
        HashSet<ServiceLocation> locations = DBManager.getServiceLocations();
        if (locations == null) return;
        List<ServiceLocation> sorted = locations.stream().sorted(Comparator.comparing(ServiceLocation::getName)).collect(Collectors.toList());
        for (ServiceLocation s : sorted) {
            MenuManager.addOption(
                    Key.BROWSE_LOCATIONS_MENU,
                    new MenuOption(s.getName(), () -> Sequences.viewLocationOverview(s))
            );
        }
    }

    private static void initializeFinishGarageVehicleMenu() {
        MenuManager.createMenu(
                Key.FINISH_GARAGE_VEHICLE_MENU,
                MenuInitializer::reloadFinishGarageVehicleMenu,
                "Finish Garage Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    private static void reloadSelectCardMenu() {
        int size = MenuManager.getSize(Key.SELECT_CARD_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.SELECT_CARD_MENU, i);
        String email = ServiceManager.getCurrentEmail();
        HashSet<Card> cards = DBManager.getCards(email);
        if (cards == null) return;
        for (Card card : cards) {
            MenuManager.addOption(
                    Key.SELECT_CARD_MENU,
                    new MenuOption(
                            card.getNumCensored(),
                            () -> CardManager.setSelected(card)
                    )
            );
        }
    }

    private static void reloadFinishGarageVehicleMenu() {
        int size = MenuManager.getSize(Key.FINISH_GARAGE_VEHICLE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.SELECT_CARD_MENU, i);


        ServiceLocation loc = ServiceManager.getCurrent();
        if (loc == null) return;

        HashSet<GarageData> data = DBManager.getUnfinishedVehicleGarageData(loc);
        if (data == null) return;

        for (GarageData gd : data) {
            String sn = gd.getSerialNum();
            Vehicle v = DBManager.getVehicle(sn);
            if (v == null) return;
            boolean isManufactured = v.isManufactured();
            boolean isInspection = gd.getReason().equals("Inspection");
            MenuManager.addOption(
                    Key.FINISH_GARAGE_VEHICLE_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " - " + gd.getReason() + " (SN: " + sn + ")",
                            () -> {
                                // clear console
                                IOManager.clear();

                                // ask question
                                String question;
                                if (!isManufactured)
                                    question = "Has the vehicle with a serial number of " + sn + " arrived?";
                                else if (isInspection)
                                    question = "Have you finished inspecting the vehicle with serial number " + sn + "?";
                                else
                                    question = "Have you finished servicing the vehicle with serial number " + sn + "?";
                                boolean finished = IOManager.getBooleanInput(question);
                                if (!finished) {
                                    MenuManager.setNextMessage("Service is not finished.");
                                    return;
                                }

                                // determine if info is correct
                                boolean correct = Sequences.confirmVehicleInfo(gd, v);
                                if (!correct) {
                                    MenuManager.setNextMessage("Information is not correct.");
                                    return;
                                }

                                // update database
                                boolean success;
                                if (!isManufactured) success = DBManager.finishManufactured(sn);
                                else if (isInspection) success = DBManager.finishInspection(v);
                                else success = DBManager.finishServicing(v);

                                if (!success) {
                                    MenuManager.setNextMessage("Failed to update database. Please try again.");
                                    return;
                                }

                                // send email
                                String email = DBManager.getEmail(v);
                                if (email != null) {
                                    UserManager.sendEmail(email, "Your vehicle is ready for pickup at " + loc.getName() + ".");
                                    IOManager.clear("Vehicle pickup notification has been sent to " + email);
                                } else {
                                    IOManager.clear();
                                }

                                // print confirmation
                                IOManager.println("Vehicle is now ready for pickup.");
                                IOManager.getStringInput("Enter any value to continue:");
                                MenuManager.showMenu(Key.MANAGE_GARAGE_MENU);
                            }
                    )
            );
        }
    }
}
