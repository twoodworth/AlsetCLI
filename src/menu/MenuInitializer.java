package menu;

import connection.ConnectionManager;
import constants.Constants;
import main.InputManager;
import user.UserManager;

import java.sql.Connection;

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
            initializeStartMenu();
            initialized = true;
        }
    }

    /**
     * Initializes the start menu
     */
    private static void initializeStartMenu() {
        MenuManager.getInstance().createMenu(
                Constants.START_MENU_KEY,
                "Start Menu",
                new MenuOption("Log Into Alset", Sequences::alsetLoginSequence)
        );
    }

    /**
     * Initializes the Edgar1 login menu
     */
    private static void initializeEdgar1Menu() {
        MenuManager.getInstance().createMenu(
                Constants.EDGAR1_MENU_KEY,
                "Edgar1 Login Menu",
                new MenuOption("Log Into Edgar1", Sequences::edgar1LoginSequence),
                new MenuOption("Exit Program", () -> InputManager.exitProgram(0))
        );
    }
}
