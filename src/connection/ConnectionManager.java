package connection;

import constants.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection current = null;

    public static Connection createEdgar1Connection(String user, String pwd) {
        return createConnection(Constants.EDGAR1_URL, user, pwd);
    }

    public static Connection createConnection(String url, String user, String pwd) {
        try {
            return DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            return null;
        }
    }

    public static Connection getCurrentConnection() {
        return current;
    }
}
