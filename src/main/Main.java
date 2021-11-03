package main;

import constants.Constants;
import menu.MenuInitializer;
import menu.MenuManager;

public class Main {

    /**
     * The main method which starts all other processes in the program.
     *
     * @param args: Arguments provided by the user upon calling the program to start
     */
    public static void main(String[] args) {
        initialize();
        startSequence();
    }

    /**
     * Initializes the menu manager, along with all the menus,
     * for later use.
     */
    private static void initialize() {
        MenuManager.getInstance();
        MenuInitializer.initializeMenus();
    }

    /**
     * Prints the initial sequence of messages upon startup, and
     * displays the Edgar1 login menu.
     */
    private static void startSequence() {
        System.out.println(Constants.ALSET_LOGO);
        System.out.println("**Remove this** | user=trw324 | pwd=P823539274"); //todo remove
        MenuManager.getInstance().showMenu(Constants.EDGAR1_MENU_KEY);
    }
}
