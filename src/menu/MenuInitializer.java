package menu;

import connection.ConnectionManager;
import constants.Constants;
import main.InputManager;
import user.UserManager;

import java.sql.Connection;

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
        String email = InputManager.getStringInput("Enter your Alset email:");
        String password = InputManager.getPasswordInput("Enter your Alset password:");
        boolean success = UserManager.login(email, password);

        if (success) System.out.println("Success");
        else System.out.println("Unable to login. Please try again.");
    }

    private static void forgotPwdSequence() {

    }

    private static void edgar1LoginSequence() {
        String id = InputManager.getStringInput("Enter your Oracle id for edgar1:");
        String pwd = InputManager.getPasswordInput("Enter your Oracle password for edgar1:");
        System.out.println("Connecting to database...");
        Connection conn = ConnectionManager.createEdgar1Connection(id, pwd);
        if (conn == null)
            System.out.println("Invalid id/password (make sure are you connected to Lehigh wifi or using the Lehigh VPN)");
        else {
            System.out.println("Connected successfully.");
            manager.showMenu(Constants.START_MENU_KEY);
        }
    }

    private static void initializeEdgar1Menu() {
        manager.createMenu(
                Constants.EDGAR1_MENU_KEY,
                "Edgar1 Login Menu",
                new MenuOption("Log Into Edgar1", MenuInitializer::edgar1LoginSequence),
                new MenuOption("Exit Program", () -> InputManager.exitProgram(0))
        );
    }
}
