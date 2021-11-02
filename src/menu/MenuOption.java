package menu;

/**
 * A selectable option whose description gets displayed by a menu. When a menu option
 * gets selected by the user via a menu, the Runnable action gets called to
 * run.
 */
public class MenuOption {
    /**
     * The description which gets displayed to the user
     */
    private final String description;

    /**
     * The action which takes place when the user selects this option.
     */
    private final Runnable action;

    /**
     * Constructs a new MenuOption.
     *
     * @param description: The option description
     * @param action:      The menu action
     */
    public MenuOption(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }

    /**
     * Returns the menu option's description
     *
     * @return description of option
     */
    public String getDescription() {
        return description;
    }

    /**
     * Runs the menu option's action
     */
    void runAction() {
        action.run();
    }
}
