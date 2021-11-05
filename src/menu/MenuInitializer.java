package menu;

import constants.Constants;

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
            initialized = true;
        }
    }

    /**
     * Initializes the start menu
     */
    private static void initializeAlsetLoginMenu() {
        MenuManager.createMenu(
                Constants.ALSET_LOGIN_MENU_KEY,
                "Alset Login Menu",
                new MenuOption("Log Into Alset \t // Sample: (email=ghost@sbcglobal.net, password=O0y8q6WPXFxo)", Sequences::alsetLoginSequence),
                new MenuOption("Forgot password", Sequences::forgotPwdSequence),
                new MenuOption("Create New Account //todo add functionality", Sequences::createAcctSequence),
                new MenuOption("Admin Login //todo add functionality", Sequences::adminLoginSequence),
                new MenuOption("End connection", Sequences::endConnectionSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    /**
     * Initializes the Edgar1 login menu
     */
    private static void initializeEdgar1Menu() {
        MenuManager.createMenu(
                Constants.EDGAR1_MENU_KEY,
                "Edgar1 Login Menu",
                new MenuOption("Log Into Edgar1 \t//todo remove: user=trw324 | pwd=P823539274", Sequences::edgar1LoginSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    private static void initializeMainMenu() {
        MenuManager.createMenu(
                Constants.ALSET_MAIN_MENU_KEY,
                "Alset Main Menu",
                new MenuOption("Manage Vehicles //todo add functionality", () -> {
                }),
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
}
