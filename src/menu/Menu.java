package menu;

/**
 * A class which is used for creating command-line interface menus.
 */
public class Menu {
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

    /**
     * Constructs a new menu with parameters title and options.
     *
     * @param title   the menu title
     * @param options an array of all the menu's options
     */
    public Menu(String title, MenuOption... options) {
        this.title = title;
        this.options = options;
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
        var sb = new StringBuilder(title);
        int count = 0;
        for (var option : options) {
            sb.append("\n\t").append(count++).append("\t").append(option.getDescription());
        }
        return sb.toString();
    }
}
