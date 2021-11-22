package constants;

/**
 * Stores a list of constant String values used as keys.
 */
public class Keys {//todo convert to enum

    /**
     * Key mapping to the start menu
     */
    public static final String ALSET_LOGIN_MENU_KEY = "a";

    /**
     * Key mapping to the Edgar1 Login menu
     */
    public static final String EDGAR1_MENU_KEY = "b";

    /**
     * Key mapping to the main menu
     */
    public static final String ALSET_MAIN_MENU_KEY = "c";

    /**
     * Key mapping to the 'My Vehicles' menu
     */
    public static final String MY_VEHICLES_KEY = "d";

    public static final String SERVICE_MANAGER_KEY = "e";

    /**
     * Key mapping to the Inspection Locations menu
     */
    public static final String INSPECTION_LOCATIONS_LIST = "i";

    /**
     * URL of edgar1
     */
    public static final String EDGAR1_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

    /**
     * String containing all possible characters that
     * can appear in a random password.
     */
    public static final String RANDOM_PASS_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final String VIEW_GARAGE_KEY = "f";
    public static final String MANAGE_GARAGE_KEY = "g";

    /**
     * Private constructor of Constants
     */
    private Keys() {
    }
}
