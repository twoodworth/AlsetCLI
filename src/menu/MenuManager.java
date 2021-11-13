package menu;

import main.IOManager;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

/**
 * Singleton-style class which manages all of the different menus
 * in the Alset program.
 */
public class MenuManager {

    /**
     * Maps a set of IDs to corresponding menus.
     */
    private static final HashMap<String, Menu> menus = new HashMap<>();

    /**
     * Contains a history of all the menus the user has viewed. Every time the user
     * views a new menu, the old menu is added to history.
     */
    private static final Stack<Menu> history = new Stack<>();

    /**
     * The menu currently being displayed to the user.
     */
    private static Menu current = null;

    /**
     * Private constructor of MenuManager
     */
    private MenuManager() {
    }

    /**
     * Creates a new menu using the given parameters
     *
     * @param id:      ID of menu
     * @param title:   Title of menu
     * @param options: Array of menu options
     * @throws IllegalArgumentException if a menu with the given ID already exists.
     */
    public static void createMenu(String id, String title, MenuOption... options) throws IllegalArgumentException {
        if (menus.containsKey(id)) {
            throw new IllegalArgumentException("A menu with id " + id + "already exists.");
        } else {
            Menu menu = new Menu(title, options);
            menus.put(id, menu);
        }
    }

    /**
     * Adds an option to the menu with the given ID.
     *
     * @param id:     ID of menu
     * @param option: Option to add to the menu
     * @throws NoSuchElementException if no menus exist with the given ID
     */
    public static void addOption(String id, MenuOption option) throws NoSuchElementException {
        Menu menu = menus.get(id);
        if (menu == null) throw new NoSuchElementException("No menu exists with id " + id);
        else {
            menu.addOption(option);
        }
    }

    /**
     * Removes a menu option from a menu
     *
     * @param id:    ID of menu
     * @param index: Option index to remove
     * @throws NoSuchElementException if no menus exist with the given ID
     */
    public static void removeOption(String id, int index) throws NoSuchElementException {
        Menu menu = menus.get(id);
        if (menu == null) throw new NoSuchElementException("No menu exists with id " + id);
        else {
            menu.removeOption(index);
        }
    }

    /**
     * Shows the user the menu with the given ID
     *
     * @param id ID of menu to show
     * @throws NoSuchElementException if no menu exists with the given ID
     */
    public static void showMenu(String id) throws NoSuchElementException {
        Menu menu = menus.get(id);
        if (menu == null) throw new NoSuchElementException("No menu with ID of " + id + " exists.");
        history.push(current);
        showMenu(menu);
    }

    /**
     * Pops the top menu in the history stack and
     * displays it to the user.
     */
    public static void showPrevious() {
        Menu previous = history.pop();
        showMenu(previous);
    }

    /**
     * Returns the size of a menu, which equals the number of menuOptions that the menu has.
     *
     * @param id: ID of menu
     * @return size of menu
     * @throws NoSuchElementException if no menus exist with the given ID
     */
    public static int getSize(String id) throws NoSuchElementException {
        Menu menu = menus.get(id);
        if (menu == null) throw new NoSuchElementException("No menu with ID of " + id + " exists.");
        return menu.size();
    }

    /**
     * Displays the provided menu to the user.
     *
     * @param menu: Menu to display to the user
     */
    private static void showMenu(Menu menu) {
        current = menu;
        while (current == menu) {
            IOManager.println(menu.toString());
            int l = menu.options.length;
            Integer input = IOManager.getIntInput("Select an option:", 0, l - 1);
            if (input == null) {
                IOManager.println("Input is invalid.");
            } else {
                menu.options[input].runAction();
            }
        }
    }

    /**
     * Deletes the menu with the given ID.
     * <p>
     * If the menu does not exist, or is currently being
     * displayed to the user, no menu will be deleted and
     * this function will return false.
     * <p>
     * Otherwise, the function will return true.
     *
     * @param id: ID of menu to delete
     * @return true if the menu was deleted, false if it was not or does not exist.
     */
    static boolean deleteMenu(String id) {
        Menu menu = menus.get(id);
        if (menu == null) return false;
        if (current.equals(menu)) return false;
        menus.remove(id);
        history.remove(menu);
        return true;
    }

    static String getCurrentKey() {
        if (current == null) return null;

        Set<String> keys = menus.keySet();
        for (String key : keys) {
            if (menus.get(key).equals(current)) {
                return key;
            }
        }

        return null;
    }
}
