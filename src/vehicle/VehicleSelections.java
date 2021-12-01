package vehicle;

import location.ServiceLocation;

import java.util.HashSet;

/**
 * A class which is used for storing vehicle related selections for future reference.
 */
public class VehicleSelections {
    /**
     * Model currently selected
     */
    private static String model = null;

    /**
     * Year currently selected
     */
    private static Integer year = null;

    /**
     * Vehicle options currently selected
     */
    private static HashSet<String> options = new HashSet<>();

    /**
     * Location currently selected
     */
    private static ServiceLocation location = null;

    /**
     * Gets the model name that is currently selected
     *
     * @return model name
     */
    public static String getModel() {
        return model;
    }

    /**
     * Gets the location that is currently selected
     *
     * @return service location
     */
    public static ServiceLocation getLocation() {
        return location;
    }

    /**
     * Sets the current location
     *
     * @param location: New service location
     */
    public static void setLocation(ServiceLocation location) {
        VehicleSelections.location = location;
    }

    /**
     * Gets the model year currently selected
     *
     * @return year selected
     */
    public static Integer getYear() {
        return year;
    }

    /**
     * Gets the set of options currently selected
     *
     * @return selected options
     */
    public static HashSet<String> getOptions() {
        return new HashSet<>(options);
    }

    /**
     * Sets the model name
     *
     * @param model: model name
     */
    public static void setModel(String model) {
        VehicleSelections.model = model;
    }

    /**
     * Sets the model year
     *
     * @param year: model year
     */
    public static void setYear(int year) {
        VehicleSelections.year = year;
    }

    /**
     * Adds a custom option to the set of options
     *
     * @param string: New option
     */
    public static void addCustomOption(String string) {
        options.add(string);
    }

    /**
     * Resets all selected values to null / empty
     */
    public static void reset() {
        model = null;
        year = null;
        options = new HashSet<>();
    }
}
