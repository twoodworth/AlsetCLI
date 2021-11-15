package location;

public class ServiceManager {
    private static ServiceLocation current = null;

    public static void setCurrent(ServiceLocation location) {
        current = location;
    }

    public static ServiceLocation getCurrent() {
        return current;
    }

    public static boolean logout() {
        boolean already = current == null;
        current = null;
        return already;
    }
}
