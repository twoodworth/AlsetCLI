package location;

/**
 * Manages service locations within this program
 */
public class ServiceManager {
    /**
     * Service location currently being managed
     */
    private static ServiceLocation current = null;

    /**
     * Email currently being serviced
     */
    private static String currentEmail = null;

    /**
     * Sets the service location to be managed
     *
     * @param location: Service location
     */
    public static void setCurrent(ServiceLocation location) {
        current = location;
    }

    /**
     * Sets the email to be managed
     *
     * @param email: Email
     */
    public static void setCurrentEmail(String email) {
        currentEmail = email;
    }

    /**
     * Returns the service location currently being managed
     *
     * @return service location
     */
    public static ServiceLocation getCurrent() {
        return current;
    }

    /**
     * Returns the email currently being managed.
     *
     * @return email
     */
    public static String getCurrentEmail() {
        return currentEmail;
    }

    /**
     * Logs the program out of the current service location.
     */
    public static void logout() {
        current = null;
        currentEmail = null;
    }

    /**
     * Logs the program out of the current email
     */
    public static void logoutEmail() {
        currentEmail = null;
    }


}
