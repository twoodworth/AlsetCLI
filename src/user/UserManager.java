package user;

import connection.ConnectionManager;
import constants.Constants;
import database.DatabaseManager;
import menu.MenuManager;

import java.sql.SQLException;

public class UserManager {

    public static User current = null;

    public static User getCurrent() {
        return current;
    }

    public static boolean login(String email, String password) {
        if (DatabaseManager.getInstance().validLoginData(email, password)) {
            try {
                boolean success;
                var s = ConnectionManager.getCurrentConnection().prepareStatement("SELECT first, middle, last FROM customer_name WHERE email=?");
                s.setString(1, email);
                s.execute();
                var rs = s.executeQuery();
                if (rs.next()) {
                    var first = rs.getString("first");
                    var middle = rs.getString("middle");
                    var last = rs.getString("last");
                    current = new User(first, middle, last, email, password);
                    success = true;
                } else {
                    success = false;
                }
                rs.close();
                return success;
            } catch (SQLException e) {
                return false;
            }
        } else {
            return false;
        }
    }

}
