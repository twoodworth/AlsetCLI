package main;

import constants.Constants;
import menu.MenuInitializer;
import menu.MenuManager;

public class Main {

    public static void main(String[] args) {
        initialize();
        startSequence();
    }

    private static void initialize() {
        MenuManager.getInstance();
        MenuInitializer.initializeMenus();
    }

    private static void startSequence() {
        System.out.println(Constants.WELCOME_SCREEN);
        System.out.println("**Remove this** | user=trw324 | pwd=P823539274");
        MenuManager.getInstance().showMenu(Constants.EDGAR1_MENU_KEY);
    }
}
