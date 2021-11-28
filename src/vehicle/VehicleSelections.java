package vehicle;

import location.ServiceLocation;
import location.ServiceManager;

import java.util.HashSet;

public class VehicleSelections {
    private static String model = null;
    private static Integer year = null;
    private static HashSet<String> options = new HashSet<>();
    private static ServiceLocation location = null;

    public static String getModel() {
        return model;
    }

    public static ServiceLocation getLocation() {
        return location;
    }

    public static void setLocation(ServiceLocation location) {
        VehicleSelections.location = location;
    }

    public static Integer getYear() {
        return year;
    }

    public static HashSet<String> getOptions() {
        return new HashSet<>(options);
    }

    public static void setModel(String model) {
        VehicleSelections.model = model;
    }

    public static void setYear(int year) {
        VehicleSelections.year = year;
    }

    public static void addCustomOption(String string) {
        options.add(string);
    }

    public static void reset() {
        model = null;
        year = null;
        options = new HashSet<>();
    }
}
