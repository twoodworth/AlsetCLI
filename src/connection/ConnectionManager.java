package connection;

import constants.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages connections to the database, and to other potential servers.
 */
public class ConnectionManager {

    /**
     * The connection currently being utilized by the user.
     */
    private static Connection current = null;

    /**
     * Private constructor of ConnectionManager
     */
    private ConnectionManager() {
    }

    /**
     * Establishes a connection with edgar1.
     *
     * @param user: Username to connect with
     * @param pwd:  Password to connect with
     * @return The established connection, or null if a connection was unable to be made.
     */
    public static Connection createEdgar1Connection(String user, String pwd) {
        return createConnection(Constants.EDGAR1_URL, user, pwd);
    }

    /**
     * Establishes a connection with a given URL
     *
     * @param url:  The URL to connect with
     * @param user: The username to connect with
     * @param pwd:  The password to connect with
     * @return The established connection, or null if a connection was unable to be made.
     */
    public static Connection createConnection(String url, String user, String pwd) {
        try {
            Connection conn = DriverManager.getConnection(url, user, pwd);
            if (current != null) {
                boolean closed = closeConnection();
                if (!closed) System.out.println("Error while closing previous connection.");
            }
            current = conn;
            return current;
        } catch (SQLException e) {
            current = null;
            return null;
        }
    }

    /**
     * Returns true if the connection is closed, otherwise false.
     *
     * @return true if connection is closed, otherwise false
     */
    public static boolean closeConnection() {
        if (current != null) {
            try {
                current.close();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the connection currently being held.
     *
     * @return current connection
     */
    public static Connection getCurrentConnection() {
        return current;
    }
}
