package main;

import constants.Key;
import menu.MenuInitializer;
import menu.MenuManager;

/**
 * Main class of the Alset CLI program. Initializes and starts the program.
 */
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
     * Initializes the program
     */
    private static void initialize() {
        MenuInitializer.initializeMenus();
    }

    /**
     * Prints the initial sequence of messages upon startup, and
     * displays the Edgar1 login menu.
     */
    private static void startSequence() {
        MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, "Welcome to Alset, please connect to the database.");
    }
}
