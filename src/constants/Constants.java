package constants;

/**
 * Stores a list of constant String values for other classes to reference.
 */
public class Constants {

    /**
     * Key mapping to the start menu
     */
    public static final String ALSET_LOGIN_MENU_KEY = "a";

    /**
     * Key mapping to the main menu
     */
    public static final String ALSET_MAIN_MENU_KEY = "c";

    /**
     * Prefix for all printouts
     */
    public static final String ALSET_PREFIX = "[Alset] ";

    /**
     * Key mapping to the Edgar1 Login menu
     */
    public static final String EDGAR1_MENU_KEY = "b";

    /**
     * URL of edgar1
     */
    public static final String EDGAR1_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

    /**
     * String containing all possible characters that
     * can appear in a random password.
     */
    public static final String RANDOM_PASS_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * String representation of Alset Logo
     */
    public static final String ALSET_LOGO =
            "\n" +
                    "+-----------------------------------------------------------------------------------------------------+\n" +
                    "|                                                                                                     |\n" +
                    "|     #########         ##                 #############                            ###############   |\n" +
                    "|                       ##                 ##                  ##############             ##          |\n" +
                    "|    ###########        ##                 ##                  ##                         ##          |\n" +
                    "|   ##         ##       ##                 #############       ##############             ##          |\n" +
                    "|   ##         ##       ##                            ##       ##                         ##          |\n" +
                    "|   ##         ##       ############       #############       ##############             ##          |\n" +
                    "|                                                                                                     |\n" +
                    "+-----------------------------------------------------------------------------------------------------+\n";

    /**
     * Private constructor of Constants
     */
    private Constants() {
    }
}
