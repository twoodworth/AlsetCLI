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
import vehicle.Model;
import vehicle.Vehicle;
import vehicle.VehicleSelections;

import java.util.*;
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
            initializeSelectModelMenu();
            initializeSelectYearMenu();
            initializeSelectOptionsMenu();
            initializeCompleteOrderMenu();
            initializeSelectLocationMenu();
            initializeManageShowroomMenu();
            initializeViewShowroomMenu();
            initializeAddShowroomVehicleMenu();
            initializeRetireVehicleMenu();
            initializeManageListingsMenu();
            initializeViewListingsMenu();
            initializeUpdateListingsMenu();
            initializeSellListingMenu();
            initializeSelectUnrepairableModelMenu();
            initializeSelectUnrepairableYearMenu();
            initializeSelectRepairableModelMenu();
            initializeSelectRepairableYearMenu();
            initializeSelectAllOptionsMenu();
            initializeProductManagerMenu();
            initializeSelectAllYearsMenu();
            initializeSelectAllModelsMenu();
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
                new MenuOption("Create New Customer Account", Sequences::createNewAccountSequence),
                new MenuOption("Browse Service Locations",  () -> MenuManager.showMenu(Key.BROWSE_LOCATIONS_MENU)),
                new MenuOption("Login as Service Manager", Sequences::serviceManagerSequence),
                new MenuOption("Login as Product Manager", Sequences::productManagerSequence),
                new MenuOption("Close connection", Sequences::endConnectionSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    private static void initializeProductManagerMenu() {
        MenuManager.createMenu(
                Key.PRODUCT_MANAGER_MENU,
                "Product Manager Menu",
                new MenuOption("Add New Model", Sequences::addModelSequence),
                new MenuOption("Add New Option", Sequences::addOptionSequence),
                new MenuOption("Add New Location", Sequences::addServiceLocationSequence),
                new MenuOption("Set Model Price", Sequences::setModelPrice),
                new MenuOption("Set Option Price", Sequences::setOptionPrice),
                new MenuOption("Add Model Option", Sequences::addModelOptionSequence),
                new MenuOption("Recall a Product", Sequences::recallSequence),
                new MenuOption("Log Out", MenuManager::showPrevious)
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
                new MenuOption("Purchase Vehicle", Sequences::purchaseVehicleSequence),
                new MenuOption("View Purchase History", Sequences::purchaseHistorySequence),
                new MenuOption("Browse Service Locations", () -> MenuManager.showMenu(Key.BROWSE_LOCATIONS_MENU)),
                new MenuOption("Log Out", Sequences::alsetLogoutSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the 'Select Model' menu
     */
    private static void initializeSelectModelMenu() {
        MenuManager.createMenu(
                Key.SELECT_MODEL_MENU,
                MenuInitializer::reloadSelectModelMenu,
                "Select Model"
        );
    }

    /**
     * Reloads the select model menu
     */
    private static void reloadSelectModelMenu() {
        int size = MenuManager.getSize(Key.SELECT_MODEL_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_MODEL_MENU, i);
        Set<String> buyable = DBManager.getBuyableModels();
        for (String s : buyable) {
            MenuManager.addOption(
                    Key.SELECT_MODEL_MENU,
                    new MenuOption("Model " + s, () -> VehicleSelections.setModel(s))
            );
        }
    }

    /**
     * Initializes the 'Select Model' menu
     */
    private static void initializeSelectAllModelsMenu() {
        MenuManager.createMenu(
                Key.SELECT_ALL_MODELS_MENU,
                MenuInitializer::reloadSelectAllModelsMenu,
                "Select Model"
        );
    }

    /**
     * Reloads the select model menu
     */
    private static void reloadSelectAllModelsMenu() {
        int size = MenuManager.getSize(Key.SELECT_ALL_MODELS_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_ALL_MODELS_MENU, i);
        Set<String> models = DBManager.getAllModels().stream().map(Model::getName).collect(Collectors.toSet());
        for (String m : models) {
            MenuManager.addOption(
                    Key.SELECT_ALL_MODELS_MENU,
                    new MenuOption("Model " + m, () -> VehicleSelections.setModel(m))
            );
        }
    }

    /**
     * Initializes the select unrepairable model menu
     */
    private static void initializeSelectUnrepairableModelMenu() {
        MenuManager.createMenu(
                Key.SELECT_UNREPAIRABLE_MODEL_MENU,
                MenuInitializer::reloadSelectUnrepairableModelMenu,
                "Select a Model"
        );
    }

    /**
     * Reloads the select unrepairable model menu
     */
    private static void reloadSelectUnrepairableModelMenu() {
        int size = MenuManager.getSize(Key.SELECT_UNREPAIRABLE_MODEL_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_UNREPAIRABLE_MODEL_MENU, i);
        Set<String> unrepairable =
                DBManager
                        .getUnrepairableModels(ServiceManager.getCurrent())
                        .stream()
                        .map(Model::getName)
                        .collect(Collectors.toSet());
        for (String s : unrepairable) {
            MenuManager.addOption(
                    Key.SELECT_UNREPAIRABLE_MODEL_MENU,
                    new MenuOption("Model " + s, () -> VehicleSelections.setModel(s))
            );
        }
    }

    /**
     * Initializes the select repairable model menu
     */
    private static void initializeSelectRepairableModelMenu() {
        MenuManager.createMenu(
                Key.SELECT_REPAIRABLE_MODEL_MENU,
                MenuInitializer::reloadSelectRepairableModelMenu,
                "Select a Model"
        );
    }

    /**
     * Reloads the select repairable model menu
     */
    private static void reloadSelectRepairableModelMenu() {
        int size = MenuManager.getSize(Key.SELECT_REPAIRABLE_MODEL_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_REPAIRABLE_MODEL_MENU, i);
        Set<String> repairable =
                DBManager
                        .getRepairableModels(ServiceManager.getCurrent())
                        .stream()
                        .map(Model::getName)
                        .collect(Collectors.toSet());
        for (String s : repairable) {
            MenuManager.addOption(
                    Key.SELECT_REPAIRABLE_MODEL_MENU,
                    new MenuOption("Model " + s, () -> VehicleSelections.setModel(s))
            );
        }
    }

    /**
     * Initializes the select repairable year menu
     */
    private static void initializeSelectRepairableYearMenu() {
        MenuManager.createMenu(
                Key.SELECT_REPAIRABLE_YEAR_MENU,
                MenuInitializer::reloadSelectRepairableYearMenu,
                "Select a Year"
        );
    }

    /**
     * Initializes the select repairable year menu
     */
    private static void reloadSelectRepairableYearMenu() {
        int size = MenuManager.getSize(Key.SELECT_REPAIRABLE_YEAR_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_REPAIRABLE_YEAR_MENU, i);

        String name = VehicleSelections.getModelName();
        Set<Integer> repairable =
                DBManager
                        .getRepairableModels(ServiceManager.getCurrent())
                        .stream()
                        .filter(m -> name.equals(m.getName()))
                        .map(Model::getYear)
                        .collect(Collectors.toSet());
        for (Integer i : repairable) {
            MenuManager.addOption(
                    Key.SELECT_REPAIRABLE_YEAR_MENU,
                    new MenuOption(i.toString(), () -> VehicleSelections.setYear(i))
            );
        }
    }

    /**
     * Initializes the select unrepairable year menu
     */
    private static void initializeSelectUnrepairableYearMenu() {
        MenuManager.createMenu(
                Key.SELECT_UNREPAIRABLE_YEAR_MENU,
                MenuInitializer::reloadSelectUnrepairableYearMenu,
                "Select a Year"
        );
    }

    /**
     * Reloads the select unrepairable year menu
     */
    private static void reloadSelectUnrepairableYearMenu() {
        int size = MenuManager.getSize(Key.SELECT_UNREPAIRABLE_YEAR_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_UNREPAIRABLE_YEAR_MENU, i);
        String name = VehicleSelections.getModelName();
        Set<Integer> unrepairable =
                DBManager
                        .getUnrepairableModels(ServiceManager.getCurrent())
                        .stream()
                        .filter(m -> name.equals(m.getName()))
                        .map(Model::getYear)
                        .collect(Collectors.toSet());
        for (Integer i : unrepairable) {
            MenuManager.addOption(
                    Key.SELECT_UNREPAIRABLE_YEAR_MENU,
                    new MenuOption(i.toString(), () -> VehicleSelections.setYear(i))
            );
        }
    }

    /**
     * Initializes the select location menu
     */
    private static void initializeSelectLocationMenu() {
        MenuManager.createMenu(
                Key.SELECT_LOCATION_MENU,
                MenuInitializer::reloadSelectLocationMenu,
                "Select Pickup Location"
        );
    }

    /**
     * Reloads the select location menu
     */
    private static void reloadSelectLocationMenu() {
        int size = MenuManager.getSize(Key.SELECT_LOCATION_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_LOCATION_MENU, i);
        String name = VehicleSelections.getModelName();
        int year = VehicleSelections.getYear();
        Vehicle v = new Vehicle("temp", year, name, false, null);
        Set<ServiceLocation> locations = DBManager.getRepairableLocations(v);
        if (locations == null) locations = new HashSet<>();
        List<ServiceLocation> sorted = locations.stream().sorted(Comparator.comparing(ServiceLocation::getName)).collect(Collectors.toList());
        for (ServiceLocation s : sorted) {
            MenuManager.addOption(
                    Key.SELECT_LOCATION_MENU,
                    new MenuOption(s.getName(), () -> VehicleSelections.setLocation(s))
            );
        }
    }

    /**
     * Initializes the complete order menu
     */
    private static void initializeCompleteOrderMenu() {
        MenuManager.createMenu(
                Key.COMPLETE_ORDER_MENU,
                "Select an Option",
                new MenuOption("Cancel Order", () -> MenuManager.showMenu(Key.CUSTOMER_MENU, "Order Cancelled.")),
                new MenuOption("Proceed to Checkout", () -> Sequences.getCardSequence(UserManager.getCurrent().getEmail()))
        );
    }

    /**
     * Initializes the select options menu
     */
    private static void initializeSelectOptionsMenu() {
        MenuManager.createMenu(
                Key.SELECT_OPTIONS_MENU,
                MenuInitializer::reloadSelectOptionsMenu,
                "Choose Additional Features",
                new MenuOption("Finish Selecting Features", () -> {
                })
        );
    }

    /**
     * Initializes the select options menu
     */
    private static void initializeSelectAllOptionsMenu() {
        MenuManager.createMenu(
                Key.SELECT_ALL_OPTIONS_MENU,
                MenuInitializer::reloadSelectAllOptionsMenu,
                "Select a Customization Option"
        );
    }

    /**
     * Reloads the select options menu
     */
    private static void reloadSelectAllOptionsMenu() {
        int size = MenuManager.getSize(Key.SELECT_ALL_OPTIONS_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_ALL_OPTIONS_MENU, i);
        Set<String> options = DBManager.getAllOptions();
        if (options == null) {
            MenuManager.showMenu(Key.PRODUCT_MANAGER_MENU, "Failed to load data, please try again.");
            return;
        }
        for (String option : options) {
            MenuManager.addOption(
                    Key.SELECT_ALL_OPTIONS_MENU,
                    new MenuOption(option, () -> VehicleSelections.addCustomOption(option))
            );
        }
    }

    /**
     * Reloads the select options menu
     */
    private static void reloadSelectOptionsMenu() {
        int size = MenuManager.getSize(Key.SELECT_OPTIONS_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.SELECT_OPTIONS_MENU, i);
        String name = VehicleSelections.getModelName();
        int year = VehicleSelections.getYear();
        Model model = new Model(year, name);
        Set<String> buyable = DBManager.getBuyableOptions(model);
        buyable.removeAll(VehicleSelections.getOptions());
        for (String s : buyable) {
            MenuManager.addOption(
                    Key.SELECT_OPTIONS_MENU,
                    new MenuOption(s, () -> {
                        VehicleSelections.addCustomOption(s);
                        MenuManager.showMenuOnce(Key.SELECT_OPTIONS_MENU, s + " has been added.");
                    })
            );
        }
    }

    /**
     * Initializes the select year menu
     */
    private static void initializeSelectYearMenu() {
        MenuManager.createMenu(
                Key.SELECT_YEAR_MENU,
                MenuInitializer::reloadSelectYearMenu,
                "Select Year"
        );
    }

    /**
     * Reloads the Select Year Menu
     */
    private static void reloadSelectYearMenu() {
        int size = MenuManager.getSize(Key.SELECT_YEAR_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_YEAR_MENU, i);
        Set<Integer> buyable = DBManager.getBuyableYears(VehicleSelections.getModelName());
        for (int i : buyable) {
            MenuManager.addOption(
                    Key.SELECT_YEAR_MENU,
                    new MenuOption(String.valueOf(i), () -> VehicleSelections.setYear(i))
            );
        }
    }

    /**
     * Initializes the select year menu
     */
    private static void initializeSelectAllYearsMenu() {
        MenuManager.createMenu(
                Key.SELECT_ALL_YEARS_MENU,
                MenuInitializer::reloadSelectAllYearsMenu,
                "Select Year"
        );
    }

    /**
     * Reloads the Select Year Menu
     */
    private static void reloadSelectAllYearsMenu() {
        int size = MenuManager.getSize(Key.SELECT_ALL_YEARS_MENU);
        for (int i = size - 1; i >= 0; i--) MenuManager.removeOption(Key.SELECT_ALL_YEARS_MENU, i);
        Set<Integer> years = DBManager.getAllYears(VehicleSelections.getModelName());
        for (int i : years) {
            MenuManager.addOption(
                    Key.SELECT_ALL_YEARS_MENU,
                    new MenuOption(String.valueOf(i), () -> VehicleSelections.setYear(i))
            );
        }
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
                new MenuOption("Add Repairable Model", Sequences::addRepairableModelSequence),
                new MenuOption("Remove Repairable Model", Sequences::removeRepairableModelSequence),
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Key.SERVICE_MANAGER_MENU))
        );
    }

    /**
     * Initializes the Manage Showroom Menu
     */
    private static void initializeManageShowroomMenu() {
        MenuManager.createMenu(
                Key.MANAGE_SHOWROOM_MENU,
                "Manage Showroom",
                new MenuOption("View Vehicles", () -> MenuManager.showMenu(Key.VIEW_SHOWROOM_MENU)),
                new MenuOption("Order Vehicle", Sequences::orderShowroomVehicleSequence),
                new MenuOption("Add Vehicle", () -> MenuManager.showMenu(Key.ADD_SHOWROOM_VEHICLE_MENU, "Only add a vehicle if it has finished being delivered to your service location.")),
                new MenuOption("Retire/Sell Vehicle", () -> MenuManager.showMenu(Key.RETIRE_SHOWROOM_VEHICLE_MENU, "All retired vehicles get listed for sale.")),
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Key.SERVICE_MANAGER_MENU))
        );
    }

    /**
     * Initializes the Retire Vehicle Menu
     */
    private static void initializeRetireVehicleMenu() {
        MenuManager.createMenu(
                Key.RETIRE_SHOWROOM_VEHICLE_MENU,
                MenuInitializer::reloadRetireVehicleMenu,
                "Retire Vehicle From Showroom",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the retire vehicle menu
     */
    private static void reloadRetireVehicleMenu() {
        int size = MenuManager.getSize(Key.RETIRE_SHOWROOM_VEHICLE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.RETIRE_SHOWROOM_VEHICLE_MENU, i);
        Set<Vehicle> vehicles = DBManager.getManufacturedShowroomVehicles(ServiceManager.getCurrent());
        for (Vehicle v : vehicles) {
            MenuManager.addOption(
                    Key.RETIRE_SHOWROOM_VEHICLE_MENU,
                    new MenuOption(v.getYear() + " " + v.getModelName() + " (SN: " + v.getSerialNum() + ")",
                            () -> {
                                IOManager.clear();
                                Long price = IOManager.getLongInput("Enter price to list this vehicle for:", 0L, Long.MAX_VALUE);
                                if (price == null) {
                                    MenuManager.setNextMessage("Invalid Price.");
                                    return;
                                }

                                boolean success = DBManager.removeShowroomVehicle(v, price);
                                if (success) {
                                    MenuManager.showMenu(Key.MANAGE_SHOWROOM_MENU, v.getYear() + " " + v.getModelName() + " (SN: " + v.getSerialNum() + ") was successfully removed\n" +
                                            "from the showroom, and is now listed for $" + price);
                                } else {
                                    MenuManager.showMenu(Key.MANAGE_SHOWROOM_MENU, "Unable to update database. Please try again.");
                                }
                            }
                    )
            );
        }
    }

    /**
     * Initializes the 'Add Showroom Vehicle' Menu
     */
    private static void initializeAddShowroomVehicleMenu() {
        MenuManager.createMenu(
                Key.ADD_SHOWROOM_VEHICLE_MENU,
                MenuInitializer::reloadAddShowroomVehicleMenu,
                "Add Vehicle to Showroom",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'Add showroom Vehicle' menu
     */
    private static void reloadAddShowroomVehicleMenu() {
        int size = MenuManager.getSize(Key.ADD_SHOWROOM_VEHICLE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.ADD_SHOWROOM_VEHICLE_MENU, i);
        Set<Vehicle> ordered = DBManager.getOrderedVehicles();
        for (Vehicle v : ordered) {
            MenuManager.addOption(
                    Key.ADD_SHOWROOM_VEHICLE_MENU,
                    new MenuOption(v.getYear() + " " + v.getModelName() + " (SN: " + v.getSerialNum() + ")",
                            () -> {
                                boolean success = DBManager.addShowroomVehicle(v);
                                if (success) {
                                    MenuManager.setNextMessage(v.getYear() + " " + v.getModelName() + " (SN: " + v.getSerialNum() + ") successfully added to showroom");
                                } else {
                                    MenuManager.setNextMessage("Unable to update database. Please try again.");
                                }
                                MenuManager.showMenu(Key.MANAGE_SHOWROOM_MENU);
                            }
                    )
            );
        }
    }

    /**
     * Initializes the 'Remove Garage Vehicle' Menu
     */
    private static void initializeRemoveGarageVehicleMenu() {
        MenuManager.createMenu(
                Key.REMOVE_GARAGE_VEHICLE_MENU,
                MenuInitializer::reloadRemoveGarageVehicleMenu,
                "Remove Garage Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious));
    }

    /**
     * Reloads the 'Remove Garage Vehicle' Menu
     */
    private static void reloadRemoveGarageVehicleMenu() {
        int size = MenuManager.getSize(Key.REMOVE_GARAGE_VEHICLE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.REMOVE_GARAGE_VEHICLE_MENU, i);
        ServiceLocation loc = ServiceManager.getCurrent();
        Set<GarageData> data = DBManager.getFinishedVehicleGarageData(loc);
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
                                if (user == null) {
                                    MenuManager.showMenu(Key.MANAGE_GARAGE_MENU, "Failed to load data. Please try again.");
                                    return;
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
                MenuInitializer::reloadMyVehiclesMenu,
                "My Vehicles",
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Key.CUSTOMER_MENU))
        );
    }

    /**
     * Reloads the 'My Vehicles' Menu
     */
    private static void reloadMyVehiclesMenu() {
        int size = MenuManager.getSize(Key.CUSTOMER_VEHICLES_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.CUSTOMER_VEHICLES_MENU, i);
        User current = UserManager.getCurrent();

        // Load in the user's vehicles to the vehicle manager menu.
        Set<Vehicle> vehicles = current.getVehicles();
        for (Vehicle v : vehicles) {
            String num = v.getSerialNum();
            String s = v.getYear() + " Model " + v.getModelName() + " (SN: " + num + ")";

            // Add menu option to access the vehicle's menu
            MenuManager.addOption(Key.CUSTOMER_VEHICLES_MENU, new MenuOption(s, () -> Sequences.vehicleOverviewSequence(v)));
        }
    }

    /**
     * Initializes the Service Manager Menu
     */
    private static void initializeServiceManagerMenu() {
        MenuManager.createMenu(
                Key.SERVICE_MANAGER_MENU,
                () -> {
                    ServiceLocation loc = ServiceManager.getCurrent();
                    MenuManager.setTitle(Key.SERVICE_MANAGER_MENU, loc.getName());
                },
                "Name",
                new MenuOption("Location Overview", Sequences::locationOverviewSequence),
                new MenuOption("Manage Showroom", () -> MenuManager.showMenu(Key.MANAGE_SHOWROOM_MENU)),
                new MenuOption("Manage Listings", () -> MenuManager.showMenu(Key.MANAGE_LISTINGS_MENU)),
                new MenuOption("Manage Garage", () -> MenuManager.showMenu(Key.MANAGE_GARAGE_MENU)),
                new MenuOption("Log Out", Sequences::serviceManagerLogoutSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the 'Manage Listings' Menu
     */
    private static void initializeManageListingsMenu() {
        MenuManager.createMenu(
                Key.MANAGE_LISTINGS_MENU,
                "Manage Listings",
                new MenuOption("View Listings", () -> MenuManager.showMenu(Key.VIEW_LISTINGS_MENU)),
                new MenuOption("Update Listings", () -> MenuManager.showMenu(Key.UPDATE_LISTINGS_MENU)),
                new MenuOption("Sell Vehicle", () -> MenuManager.showMenu(Key.SELL_LISTING_MENU)),
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Key.SERVICE_MANAGER_MENU))
        );
    }

    /**
     * Initializes the 'Sell Listing' Menu
     */
    private static void initializeSellListingMenu() {
        MenuManager.createMenu(
                Key.SELL_LISTING_MENU,
                MenuInitializer::reloadSellListingMenu,
                "Sell Listed Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'Sell Listing' Menu
     */
    private static void reloadSellListingMenu() {
        int size = MenuManager.getSize(Key.SELL_LISTING_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.SELL_LISTING_MENU, i);
        Map<Vehicle, Long> list = DBManager.getVehicleListings();

        for (Vehicle v : list.keySet()) {
            String sn = v.getSerialNum();
            MenuManager.addOption(
                    Key.SELL_LISTING_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " (SN: " + sn + ")",
                            () -> Sequences.sellListedVehicleSequence(v, list.get(v))
                    )
            );
        }
    }

    /**
     * Initializes the 'Update Listings' Menu
     */
    private static void initializeUpdateListingsMenu() {
        MenuManager.createMenu(
                Key.UPDATE_LISTINGS_MENU,
                MenuInitializer::reloadUpdateListingsMenu,
                "Update Listings",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'Update Listings' Menu
     */
    private static void reloadUpdateListingsMenu() {
        int size = MenuManager.getSize(Key.UPDATE_LISTINGS_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.UPDATE_LISTINGS_MENU, i);
        Map<Vehicle, Long> list = DBManager.getVehicleListings();

        for (Vehicle v : list.keySet()) {
            String sn = v.getSerialNum();
            MenuManager.addOption(
                    Key.UPDATE_LISTINGS_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " (SN: " + sn + ")",
                            () -> {
                                // clear console
                                IOManager.clear("Current Price: $" + list.get(v));
                                Long price = IOManager.getLongInput("Enter new price:", 0, Long.MAX_VALUE);
                                if (price == null) {
                                    MenuManager.setNextMessage("Invalid Price.");
                                    return;
                                }
                                boolean success = DBManager.updatePriceListing(v, price);
                                if (success) {
                                    MenuManager.setNextMessage(v.getYear() + " " + v.getModelName() + " (SN: " + v.getSerialNum() + ") is now listed for $" + price);
                                } else {
                                    MenuManager.showMenu(Key.MANAGE_SHOWROOM_MENU, "Unable to update database. Please try again.");
                                }

                            }
                    )
            );
        }
    }

    /**
     * Initializes the 'View Listings' Menu
     */
    private static void initializeViewListingsMenu() {
        MenuManager.createMenu(
                Key.VIEW_LISTINGS_MENU,
                MenuInitializer::reloadViewListingsMenu,
                "View Listings",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'View Listings' Menu
     */
    private static void reloadViewListingsMenu() {
        int size = MenuManager.getSize(Key.VIEW_LISTINGS_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.VIEW_LISTINGS_MENU, i);
        Map<Vehicle, Long> list = DBManager.getVehicleListings();


        for (Vehicle v : list.keySet()) {
            String sn = v.getSerialNum();
            MenuManager.addOption(
                    Key.VIEW_LISTINGS_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " (SN: " + sn + ")",
                            () -> {
                                // clear console
                                IOManager.clear();

                                // print basic info
                                IOManager.println("\nVehicle Overview:");
                                IOManager.println("\tSerial Number: " + sn);
                                IOManager.println("\tModel: " + v.getModelName());
                                IOManager.println("\tYear: " + v.getYear());

                                Set<String> options = DBManager.getOptions(sn);
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
                                IOManager.println("\n\tPrice: $" + list.get(v) + "\n");
                                IOManager.getStringInput("Enter any value to continue:");
                            }
                    )
            );
        }
    }

    /**
     * Initializes the 'View Garage' Menu
     */
    private static void initializeViewGarageMenu() {
        MenuManager.createMenu(
                Key.VIEW_GARAGE_MENU,
                MenuInitializer::reloadViewGarageMenu,
                "Garage Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'View Garage' Menu
     */
    private static void reloadViewGarageMenu() {
        int size = MenuManager.getSize(Key.VIEW_GARAGE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.VIEW_GARAGE_MENU, i);
        ServiceLocation loc = ServiceManager.getCurrent();
        Set<GarageData> data = DBManager.getGarageData(loc);
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
                                if (condition != null) {
                                    IOManager.println("\tMileage: " + condition.getMileage() + " miles");
                                    IOManager.println("\tLast Inspection: " + condition.getLastInspectionFormatted());
                                    IOManager.println("\tHas Detected Damage: " + condition.hasDamage());
                                }

                                Set<String> options = DBManager.getOptions(sn);
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

    /**
     * Initializes the 'View Showroom' Menu
     */
    private static void initializeViewShowroomMenu() {
        MenuManager.createMenu(
                Key.VIEW_SHOWROOM_MENU,
                MenuInitializer::reloadViewShowroomMenu,
                "Showroom Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'View Showroom' Menu
     */
    private static void reloadViewShowroomMenu() {
        int size = MenuManager.getSize(Key.VIEW_SHOWROOM_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.VIEW_SHOWROOM_MENU, i);
        ServiceLocation loc = ServiceManager.getCurrent();
        Set<Vehicle> vehicles = DBManager.getShowroomVehicles(loc);

        for (Vehicle v : vehicles) {
            String sn = v.getSerialNum();
            MenuManager.addOption(
                    Key.VIEW_SHOWROOM_MENU,
                    new MenuOption(
                            v.getYear() + " " + v.getModelName() + " (SN: " + sn + ")",
                            () -> {
                                // clear console
                                IOManager.clear();

                                // print basic info
                                if (!v.isManufactured())
                                    IOManager.println("THIS VEHICLE IS CURRENTLY BEING MANUFACTURED/DELIVERED.");
                                IOManager.println("\nVehicle Overview:");
                                IOManager.println("\tSerial Number: " + sn);
                                IOManager.println("\tModel: " + v.getModelName());
                                IOManager.println("\tYear: " + v.getYear());

                                // print additional options
                                Set<String> options = DBManager.getOptions(sn);
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
                    )
            );
        }
    }

    /**
     * Initializes the 'Browse Locations' Menu
     */
    private static void initializeBrowseLocationsMenu() {
        MenuManager.createMenu(
                Key.BROWSE_LOCATIONS_MENU,
                MenuInitializer::reloadBrowseLocationsMenu,
                "Browse Service Locations",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'Browse Locations' Menu
     */
    private static void reloadBrowseLocationsMenu() {
        int size = MenuManager.getSize(Key.BROWSE_LOCATIONS_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.BROWSE_LOCATIONS_MENU, i);
        Set<ServiceLocation> locations = DBManager.getServiceLocations();
        if (locations == null) return;
        List<ServiceLocation> sorted = locations.stream().sorted(Comparator.comparing(ServiceLocation::getName)).collect(Collectors.toList());
        for (ServiceLocation s : sorted) {
            MenuManager.addOption(
                    Key.BROWSE_LOCATIONS_MENU,
                    new MenuOption(s.getName(), () -> Sequences.viewLocationOverview(s))
            );
        }
    }

    /**
     * Initializes the 'FinishGarageVehicle' Menu
     */
    private static void initializeFinishGarageVehicleMenu() {
        MenuManager.createMenu(
                Key.FINISH_GARAGE_VEHICLE_MENU,
                MenuInitializer::reloadFinishGarageVehicleMenu,
                "Finish Garage Vehicles",
                new MenuOption("Return to Previous Menu", MenuManager::showPrevious)
        );
    }

    /**
     * Reloads the 'Select Card' Menu
     */
    private static void reloadSelectCardMenu() {
        int size = MenuManager.getSize(Key.SELECT_CARD_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.SELECT_CARD_MENU, i);
        String email = ServiceManager.getCurrentEmail();
        Set<Card> cards = DBManager.getCards(email);
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

    /**
     * Reloads the 'Finish Garage Vehicle' Menu
     */
    private static void reloadFinishGarageVehicleMenu() {
        int size = MenuManager.getSize(Key.FINISH_GARAGE_VEHICLE_MENU);
        for (int i = size - 1; i > 0; i--) MenuManager.removeOption(Key.SELECT_CARD_MENU, i);


        ServiceLocation loc = ServiceManager.getCurrent();
        if (loc == null) return;

        Set<GarageData> data = DBManager.getUnfinishedVehicleGarageData(loc);
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
