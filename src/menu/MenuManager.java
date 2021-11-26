package menu;

import constants.Key;
import io.IOManager;

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
    private static final HashMap<Key, Menu> menus = new HashMap<>();


    /**
     * Contains a history of all the menus the user has viewed. Every time the user
     * views a new menu, the old menu is added to history.
     */
    private static final Stack<Menu> history = new Stack<>();

    /**
     * The menu currently being displayed to the user.
     */
    private static Menu current = null;

    private static String nextMessage = "";

    /**
     * Private constructor of MenuManager
     */
    private MenuManager() {
    }

    /**
     * Creates a new menu using the given parameters
     *
     * @param key:     ID of menu
     * @param title:   Title of menu
     * @param options: Array of menu options
     * @throws IllegalArgumentException if a menu with the given ID already exists.
     */
    public static void createMenu(Key key, String title, MenuOption... options) throws IllegalArgumentException {
        if (menus.containsKey(key)) {
            throw new IllegalArgumentException("A menu with key " + key + "already exists.");
        } else {
            Menu menu = new Menu(title, options);
            menus.put(key, menu);
        }
    }

    /**
     * Adds an option to the menu with the given ID.
     *
     * @param key:     Key of menu
     * @param option: Option to add to the menu
     * @throws NoSuchElementException if no menus exist with the given ID
     */
    public static void addOption(Key key, MenuOption option) throws NoSuchElementException {
        Menu menu = menus.get(key);
        if (menu == null) throw new NoSuchElementException("No menu exists with id " + key.toString());
        else {
            menu.addOption(option);
        }
    }

    /**
     * Removes a menu option from a menu
     *
     * @param key:    key of menu
     * @param index: Option index to remove
     * @throws NoSuchElementException if no menus exist with the given ID
     */
    public static void removeOption(Key key, int index) throws NoSuchElementException {
        Menu menu = menus.get(key);
        if (menu == null) throw new NoSuchElementException("No menu exists with key " + key.toString());
        else {
            menu.removeOption(index);
        }
    }

    /**
     * Shows the user the menu with the given ID
     *
     * @param key Key of menu to show
     * @throws NoSuchElementException if no menu exists with the given ID
     */
    public static void showMenu(Key key, String message) throws NoSuchElementException {
        Menu menu = menus.get(key);
        if (menu == null) throw new NoSuchElementException("No menu with key of " + key.toString() + " exists.");
        history.push(current);
        showMenu(menu, message);
    }

    /**
     * Pops the top menu in the history stack and
     * displays it to the user.
     */
    public static void showPrevious(String message) {
        Menu previous = history.pop();
        showMenu(previous, message);
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
     * @param key: key of menu
     * @return size of menu
     * @throws NoSuchElementException if no menus exist with the given ID
     */
    public static int getSize(Key key) throws NoSuchElementException {
        Menu menu = menus.get(key);
        if (menu == null) throw new NoSuchElementException("No menu with key of " + key + " exists.");
        return menu.size();
    }

    /**
     * Displays the provided menu to the user.
     *
     * @param menu: Menu to display to the user
     */
    private static void showMenu(Menu menu, String message) {
        current = menu;
        nextMessage = message;
        while (current == menu) {
            IOManager.clear(nextMessage);
            nextMessage = "";
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
     * Displays the provided menu to the user.
     *
     * @param menu: Menu to display to the user
     */
    private static void showMenu(Menu menu) {
        current = menu;
        while (current == menu) {
            IOManager.clear(nextMessage);
            nextMessage = "";
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
     * Sets the message to be displayed next time MenuManager#showMenu(Key) is called.
     *
     * @param message: Message to display
     */
    public static void setNextMessage(String message) {
        nextMessage = message;
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
     * @param key: Key of menu to delete
     * @return true if the menu was deleted, false if it was not or does not exist.
     */
    static boolean deleteMenu(Key key) {
        Menu menu = menus.get(key);
        if (menu == null) return false;
        if (current.equals(menu)) return false;
        menus.remove(key);
        history.remove(menu);
        return true;
    }

    /**
     * Returns the key of the menu currently being displayed
     * @return
     */
    static Key getCurrentKey() {
        if (current == null) return null;

        Set<Key> keys = menus.keySet();
        for (Key key : keys) {
            if (menus.get(key).equals(current)) {
                return key;
            }
        }

        return null;
    }
}
