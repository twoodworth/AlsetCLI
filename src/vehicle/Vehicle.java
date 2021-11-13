package vehicle;

/**
 * Represents a Vehicle, and contains vehicle data.
 */
public class Vehicle {

    /**
     * The vehicle's serial number.
     */
    private final String serialNum;

    /**
     * The vehicle's model year
     */
    private final int year;

    /**
     * The vehicle's model name
     */
    private final String model;

    /**
     * Whether the vehicle is manufactured or not
     */
    private boolean isManufactured;

    /**
     * Contains information on the condition of the vehicle
     */
    private Condition condition;

    /**
     * Constructs a new vehicle
     *
     * @param serialNum:      Serial number of vehicle
     * @param year:           model year of vehicle
     * @param model:          model name of vehicle
     * @param isManufactured: Whether the vehicle is manufactured or not
     */
    public Vehicle(String serialNum, int year, String model, boolean isManufactured, Condition condition) {
        this.serialNum = serialNum;
        this.year = year;
        this.model = model;
        this.isManufactured = isManufactured;
        this.condition = condition;
    }

    /**
     * Returns the vehicle's serial number
     *
     * @return serial number of vehicle
     */
    public String getSerialNum() {
        return serialNum;
    }

    /**
     * Returns the vehicle's condition
     *
     * @return vehicle condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Returns the vehicle's model year.
     *
     * @return model year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the vehicle's model name.
     *
     * @return model name
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns whether the vehicle is manufactured or not
     *
     * @return isManufactured
     */
    public boolean isManufactured() {
        return isManufactured;
    }
}
