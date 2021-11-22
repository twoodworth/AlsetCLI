package menu;

import constants.Keys;

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
            initializeInspectionLocation();
            initializeManageGarageMenu();
            initialized = true;
        }
    }

    /**
     * Initializes the start menu
     */
    private static void initializeAlsetLoginMenu() {
        MenuManager.createMenu(
                Keys.ALSET_LOGIN_MENU_KEY,
                "Alset Login Menu",
                new MenuOption("Login as Customer \t // Sample: (email=ghost@sbcglobal.net, password=O0y8q6WPXFxo)", Sequences::alsetLoginSequence),
                new MenuOption("Login as Service Manager \t// Sample: (password=KuB[$W2e)", Sequences::serviceManagerSequence),//todo add functionality
                new MenuOption("Login as Product Manager \t//todo add functionality", Sequences::productManagerSequence),//todo add functionality
                new MenuOption("Forgot password", Sequences::forgotPwdSequence),
                new MenuOption("Create New Account //todo add functionality", Sequences::createAcctSequence),//todo add functionality
                new MenuOption("Close connection", Sequences::endConnectionSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the Edgar1 login menu
     */
    private static void initializeEdgar1Menu() {
        MenuManager.createMenu(
                Keys.EDGAR1_MENU_KEY,
                "Edgar1 Login Menu",
                new MenuOption("Log Into Edgar1 \t//todo remove: user=trw324 | pwd=P823539274", Sequences::edgar1LoginSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the main menu
     */
    private static void initializeMainMenu() {
        MenuManager.createMenu(
                Keys.ALSET_MAIN_MENU_KEY,
                "Alset Main Menu",
                new MenuOption("My Vehicles", () -> MenuManager.showMenu(Keys.MY_VEHICLES_KEY, "")),
                new MenuOption("Purchase Vehicles //todo add functionality", () -> {
                }),
                new MenuOption("Manage Account //todo add functionality", () -> {
                }),
                new MenuOption("View Transactions //todo add functionality", () -> {
                }),
                new MenuOption("Log Out", Sequences::alsetLogoutSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    private static void initializeManageGarageMenu() {
        MenuManager.createMenu(
                Keys.MANAGE_GARAGE_KEY,
                "Manage Garage",
                new MenuOption("View Vehicles", Sequences::viewGarageSequence),
                new MenuOption("Add Vehicle\t //todo add", () -> {}),//todo add
                new MenuOption("Remove Vehicle\t //todo add", () -> {}),//todo add
                new MenuOption("Return to Previous Menu", () -> MenuManager.showPrevious(""))
        );
    }

    /**
     * Initializes the 'My Vehicles' menu
     */
    private static void initializeMyVehiclesMenu() {
        MenuManager.createMenu(
                Keys.MY_VEHICLES_KEY,
                "My Vehicles",
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Keys.ALSET_MAIN_MENU_KEY, ""))
        );
    }

    /**
     * Initializes the 'Inspection Location' menu
     */
    private static void initializeInspectionLocation() {
        MenuManager.createMenu(
                Keys.INSPECTION_LOCATIONS_LIST,
                "Inspection Locations",
                new MenuOption("Return to Vehicle Menu", () -> MenuManager.showPrevious(""))
        );
    }
}
