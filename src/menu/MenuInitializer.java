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
            initialized = true;
        }
    }

    /**
     * Initializes the start menu
     */
    private static void initializeAlsetLoginMenu() {
        MenuManager.getInstance().createMenu(
                Constants.ALSET_LOGIN_MENU_KEY,
                "Alset Login Menu",
                new MenuOption("Log Into Alset", Sequences::alsetLoginSequence),
                new MenuOption("Forgot password", Sequences::forgotPwdSequence),
                new MenuOption("Create New Account", Sequences::createAcctSequence),
                new MenuOption("Log in as admin", Sequences::adminLoginSequence),
                new MenuOption("End connection", Sequences::endConnectionSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }

    //Start Menu:
    //    [0] Log into Alset // go to Log In Menu
    //    [1] Forgot password // go to Forgot Password Menu
    //    [2] Create New Account // Go to Create Account Menu
    //    [3] Log in as admin // Go to Admin Log in Menu
    //    [4] Exit // Exit program

    /**
     * Initializes the Edgar1 login menu
     */
    private static void initializeEdgar1Menu() {
        MenuManager.getInstance().createMenu(
                Constants.EDGAR1_MENU_KEY,
                "Edgar1 Login Menu",
                new MenuOption("Log Into Edgar1", Sequences::edgar1LoginSequence),
                new MenuOption("Exit Program", Sequences::exitSequence)
        );
    }
}
