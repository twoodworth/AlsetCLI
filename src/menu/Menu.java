package menu;

import java.util.Arrays;
import java.util.Objects;

/**
 * A class which is used for creating command-line interface menus.
 */
class Menu {
    /**
     * The title of the menu. When the menu gets displayed, the title
     * is displayed as the first line in the menu.
     */
    String title;

    /**
     * An array of selectable options in the menu. Each option's description
     * is displayed on its own line in the menu for the user to select from.
     */
    MenuOption[] options;

    Runnable reloadFunction;

    /**
     * Constructs a new menu with parameters title and options.
     *
     * @param title   the menu title
     * @param options an array of all the menu's options
     */
    Menu(String title, MenuOption... options) {
        this.title = title;
        this.options = options;
        this.reloadFunction = () -> {
        };
    }

    /**
     * Constructs a new menu with parameters title and options.
     *
     * @param title          the menu title
     * @param reloadFunction function to run before showing
     * @param options        an array of all the menu's options
     */
    Menu(String title, Runnable reloadFunction, MenuOption... options) {
        this.title = title;
        this.options = options;
        this.reloadFunction = reloadFunction;
    }

    /**
     * Adds an option to the menu.
     *
     * @param option: New menu option
     */
    void addOption(MenuOption option) {
        MenuOption[] temp = Arrays.copyOf(options, options.length + 1);
        temp[options.length] = option;
        options = temp;
    }

    /**
     * Returns the menu title
     *
     * @return menu title
     */
    String getTitle() {
        return title;
    }

    /**
     * Sets the menu title
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Replaces the option at the given index with a new option
     *
     * @param index:  Inedex to add the new option too
     * @param option: New option
     */
    void setOption(int index, MenuOption option) {
        options[index] = option;
    }

    /**
     * Removes the option at the given index
     *
     * @param index: Index to remove
     */
    void removeOption(int index) {
        MenuOption[] temp = new MenuOption[options.length - 1];
        int i = 0;
        for (int j = 0; j < options.length; j++) {
            if (j == index) continue;
            temp[i] = options[j];
            i++;
        }
        options = temp;
    }

    /**
     * Returns the number of options belonging to the menu.
     *
     * @return size of menu
     */
    int size() {
        return options.length;
    }

    /**
     * Reloads the menu
     */
    void reload() {
        reloadFunction.run();
    }

    /**
     * Returns a formatted string containing the menu title and a list of its options. The formatted
     * string is designed to be printed to the console so that the user can read and select from the
     * options.
     *
     * @return menu in string format
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n").append(title);
        int count = 0;
        for (MenuOption option : options) {
            sb.append("\n\t").append("[").append(count++).append("]").append("\t").append(option.getDescription());
        }
        return sb.append("\n").toString();
    }

    /**
     * Checks for equality between a menu instance and another object.
     *
     * @param o: Object to check for equality
     * @return true if the object is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(title, menu.title) &&
                Arrays.equals(options, menu.options);
    }

    /**
     * Provides a hash code for menus.
     *
     * @return hash value
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(title);
        result = 31 * result + Arrays.hashCode(options);
        return result;
    }
}
