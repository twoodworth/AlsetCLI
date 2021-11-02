package user;

import connection.ConnectionManager;
import database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                PreparedStatement s = ConnectionManager.getCurrentConnection().prepareStatement("SELECT first, middle, last FROM customer_name WHERE email=?");
                s.setString(1, email);
                s.execute();
                ResultSet rs = s.executeQuery();
                if (rs.next()) {
                    String first = rs.getString("first");
                    String middle = rs.getString("middle");
                    String last = rs.getString("last");
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
