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
     * Sets the service location to be managed
     *
     * @param location: Service location
     */
    public static void setCurrent(ServiceLocation location) {
        current = location;
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
     * Logs the program out of the current service location.
     */
    public static void logout() {
        current = null;
    }
}
