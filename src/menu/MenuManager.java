package menu;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
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
     * Scanner used for reading the user inputs
     */
    private final Scanner scanner = new Scanner(System.in);

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
    private Menu current = null;


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
            var menu = new Menu(title, options);
            menus.put(id, menu);
            return true;
        }
    }

    public void showMenu(String id) {
        var menu = menus.get(id);
        history.push(current);
        showMenu(menu);
    }

    public void showPrevious() {
        var previous = history.pop();
        showMenu(previous);
    }

    private void showMenu(Menu menu) {
        current = menu;
        while (current == menu) {
            System.out.println(menu.toString());
            System.out.println("\nSelect an option:");
            try {
                var input = scanner.nextInt();
                var l = menu.options.length;
                if (input >= l) {
                    System.out.println("Input is invalid.");
                } else {
                    menu.options[input].runAction();
                }
            } catch (InputMismatchException e) {
                System.out.println("Input is invalid.\n");
            }
        }
    }
}
