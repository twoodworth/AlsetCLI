package database;

import connection.ConnectionManager;
import constants.Constants;
import menu.MenuManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages interactions with the Alset database
 */
public class DatabaseManager {

    /**
     * Private constructor of DatabaseManager
     */
    private DatabaseManager() {
    }


    /**
     * Checks if the given email and password are correct and correspond to an Alset account
     *
     * @param email: Email to check
     * @param pwd:   Password to check
     * @return true if the credentials are valid, otherwise false.
     */
    public static boolean validLoginData(String email, String pwd) {
        try {
            boolean valid;
            PreparedStatement s = ConnectionManager.getCurrentConnection().prepareStatement("SELECT password FROM customer WHERE email=?");
            s.setString(1, email);
            s.execute();
            ResultSet rs = s.executeQuery();
            valid = rs.next() && rs.getString("password").equals(pwd);
            rs.close();
            return valid;
        } catch (SQLException e) {
            System.out.println("Error: Lost connection to database. Please log back into Edgar1");
            MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
            return false;
        }
    }
}
