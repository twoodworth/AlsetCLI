package database;

import connection.ConnectionManager;
import constants.Constants;
import main.IOManager;
import menu.MenuManager;
import vehicle.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Manages interactions with the Alset database
 */
public class DBManager {

    /**
     * Private constructor of DatabaseManager
     */
    private DBManager() {
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
            ResultSet rs = s.executeQuery();
            valid = rs.next() && rs.getString("password").equals(pwd);
            rs.close();
            return valid;
        } catch (SQLException e) {
            IOManager.println("Error: Lost connection to database. Please log back into Edgar1");
            MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
            return false;
        }
    }

    /**
     * Checks if there is an account belonging to the given email.
     *
     * @param email: Email to check
     * @return true if an account is found, otherwise false.
     */
    public static boolean emailExists(String email) {
        try {
            boolean valid;
            PreparedStatement s = ConnectionManager.getCurrentConnection().prepareStatement("SELECT * FROM customer WHERE email=?");
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            valid = rs.next();
            rs.close();
            return valid;
        } catch (SQLException e) {
            IOManager.println("Error: Lost connection to database. Please log back into Edgar1");
            MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
            return false;
        }
    }

    /**
     * Checks if there is an account belonging to the given email.
     *
     * @param email: Email to check
     * @param pass:  New password
     * @return true if password was successfully updated, otherwise false.
     */
    public static boolean updatePassword(String email, String pass) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(
                            "UPDATE customer SET password=? WHERE email=?"
                    );
            s.setString(1, pass);
            s.setString(2, email);
            s.execute();
            return true;
        } catch (SQLException e) {
            IOManager.println("Error: Lost connection to database. Please log back into Edgar1");
            MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
            return false;
        }
    }

    /**
     * Returns the name of a given customer.
     *
     * @param email: Email of customer
     * @return array of customer's first, middle, and last names
     */
    public static String[] getName(String email) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement("SELECT first, middle, last FROM customer_name WHERE email=?");
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                String[] name = new String[3];
                name[0] = rs.getString("first");
                name[1] = rs.getString("middle");
                name[2] = rs.getString("last");
                return name;
            } else {
                return null;
            }
        } catch (SQLException e) {
            IOManager.println("Error: Lost connection to database. Please log back into Edgar1");
            MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
            return null;
        }
    }

    public static HashSet<Vehicle> getVehicles(String email) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement("SELECT serial_num, year, name, is_manufactured FROM owner NATURAL JOIN vehicle_model NATURAL JOIN vehicle WHERE email=?");
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            HashSet<Vehicle> vehicles = new HashSet<>();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("serial_num"),
                        rs.getInt("year"),
                        rs.getString("name"),
                        Boolean.parseBoolean(rs.getString("is_manufactured"))
                );
                vehicles.add(vehicle);
            }
            return vehicles;
        } catch (SQLException e) {
            IOManager.println("Error: Lost connection to database. Please log back into Edgar1");
            MenuManager.showMenu(Constants.EDGAR1_MENU_KEY);
            return null;
        }
    }


}
