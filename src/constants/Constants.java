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
     * Key mapping to the Edgar1 Login menu
     */
    public static final String EDGAR1_MENU_KEY = "b";

    /**
     * URL of edgar1
     */
    public static final String EDGAR1_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

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
