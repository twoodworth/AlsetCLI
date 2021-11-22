package constants;

public class Strings {

    /**
     * Prefix for all printouts
     */
    public static final String ALSET_PREFIX = "[Alset] ";

    /**
     * Printout for database connection errors
     */
    public static final String DB_ERROR = "Error: Lost connection to database. Please log back into Edgar1";


    /**
     * String containing all possible characters that
     * can appear in a random password.
     */
    public static final String RANDOM_PASS_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

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

}
