package menu;

import connection.ConnectionManager;
import constants.Constants;
import main.InputManager;
import user.UserManager;

public class MenuInitializer {
    private static final MenuManager manager = MenuManager.getInstance();
    private static boolean initialized = false;

    public static void initializeMenus() {
        if (!initialized) {
            initializeEdgar1Menu();
            initializeStartMenu();
            initialized = true;
        }
    }

    private static void initializeStartMenu() {
        manager.createMenu(
                Constants.START_MENU_KEY,
                "Start Menu",
                new MenuOption("Log Into Alset", MenuInitializer::alsetLoginSequence)

        );
    }

    private static void alsetLoginSequence() {
        var email = InputManager.getStringInput("Enter your Alset email:");
        var password = InputManager.getPasswordInput("Enter your Alset password:");
        var success = UserManager.login(email, password);

        if (success) System.out.println("Success");
        else System.out.println("Unable to login. Please try again.");
    }

    private static void forgotPwdSequence() {

    }

    private static void edgar1LoginSequence() {
        var id = InputManager.getStringInput("Enter your Oracle id for edgar1:");
        var pwd = InputManager.getPasswordInput("Enter your Oracle password for edgar1:");
        var conn = ConnectionManager.createEdgar1Connection(id, pwd);
        if (conn == null)
            System.out.println("Invalid id/password (make sure are you connected to Lehigh wifi or using the Lehigh VPN)");
        else manager.showMenu(Constants.START_MENU_KEY);
    }

    private static void initializeEdgar1Menu() {
        manager.createMenu(
                Constants.EDGAR1_MENU_KEY,
                "Edgar1 Login Menu",
                new MenuOption("Log Into Edgar1", MenuInitializer::edgar1LoginSequence),
                new MenuOption("Exit Program", () -> System.exit(0))
        );
    }
}
