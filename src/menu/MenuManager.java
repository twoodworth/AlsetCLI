package menu;

import constants.Constants;
import main.InputManager;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Stack;

/**
 * Singleton-style class which manages all of the different menus
 * in the Alset program.
 */
public class MenuManager {

    /**
     * Singleton instance of MenuManager
     */
    private static MenuManager instance = null;

    /**
     * Maps a set of IDs to corresponding menus.
     */
    private final HashMap<String, Menu> menus = new HashMap<>();

    /**
     * Contains a history of all the menus the user has viewed. Every time the user
     * views a new menu, the old menu is added to history.
     */
    private final Stack<Menu> history = new Stack<>();

    /**
     * The menu currently being displayed to the user.
     */
    private volatile Menu current = null;


    /**
     * Returns the singleton instance of MenuManager, and constructs
     * it if it does not already exist.
     *
     * @return instance of MenuManager
     */
    public static MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }
        return instance;
    }

    /**
     * Constructs the MenuManager class.
     */
    private MenuManager() {
    }

    public boolean createMenu(String id, String title, MenuOption... options) {
        if (menus.containsKey(id)) return false;
        else {
            Menu menu = new Menu(title, options);
            menus.put(id, menu);
            return true;
        }
    }

    public void showMenu(String id) {
        Menu menu = menus.get(id);
        history.push(current);
        showMenu(menu);
    }

    public void showPrevious() {
        Menu previous = history.pop();
        showMenu(previous);
    }

    private void showMenu(Menu menu) {
        current = menu;
        while (current == menu) {
            System.out.println(menu.toString());
            int l = menu.options.length;
            Integer input = InputManager.getIntInput("Select an option:", 0, l - 1);
            if (input == null) {
                System.out.println("Input is invalid.");
            } else {
                menu.options[input].runAction();
            }
        }
    }
}
