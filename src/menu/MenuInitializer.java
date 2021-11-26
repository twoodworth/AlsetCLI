package menu;

import constants.Key;

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
                new MenuOption("Login as Service Manager", Sequences::serviceManagerSequence),
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
                new MenuOption("My Vehicles", () -> MenuManager.showMenu(Key.CUSTOMER_VEHICLES_MENU, "")),
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

    /**
     * Initializes the Garage Manager menu
     */
    private static void initializeManageGarageMenu() {
        MenuManager.createMenu(
                Key.MANAGE_GARAGE_MENU,
                "Manage Garage",
                new MenuOption("View Vehicles", Sequences::viewGarageSequence),
                new MenuOption("Add Vehicle", Sequences::addGarageVehicleSequence),
                new MenuOption("Finish Vehicle", Sequences::finishGarageVehicleSequence),
                new MenuOption("Remove Vehicle\t //todo add", () -> {}),//todo add
                new MenuOption("Return to Previous Menu", () -> MenuManager.showPrevious(""))
        );
    }

    /**
     * Initializes the 'My Vehicles' menu
     */
    private static void initializeMyVehiclesMenu() {
        MenuManager.createMenu(
                Key.CUSTOMER_VEHICLES_MENU,
                "My Vehicles",
                new MenuOption("Return to Main Menu", () -> MenuManager.showMenu(Key.CUSTOMER_MENU, ""))
        );
    }
}
