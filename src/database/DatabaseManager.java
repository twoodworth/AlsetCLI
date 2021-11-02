package database;

import connection.ConnectionManager;
import constants.Constants;
import menu.MenuManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance = null;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public boolean validLoginData(String email, String pwd) {
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
            MenuManager.getInstance().showMenu(Constants.EDGAR1_MENU_KEY);
            return false;
        }
    }
}
