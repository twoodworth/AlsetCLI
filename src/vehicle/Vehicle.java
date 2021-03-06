package vehicle;

import java.util.Objects;

/**
 * Represents a Vehicle, and contains vehicle data.
 */
public class Vehicle {

    /**
     * The vehicle's serial number.
     */
    private final String serialNum;

    /**
     * The vehicle's model name
     */
    private final Model model;

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
        this.model = new Model(year, model);
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
        return model.getYear();
    }

    /**
     * Returns the vehicle's model name.
     *
     * @return model name
     */
    public String getModelName() {
        return model.getName();
    }

    /**
     * Returns the Vehicle's model
     *
     * @return vehicle model
     */
    public Model getModel() {
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

    /**
     * Checks if this is equal to another object
     *
     * @param o: Other object
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return isManufactured == vehicle.isManufactured &&
                serialNum.equals(vehicle.serialNum) &&
                model.equals(vehicle.model) &&
                condition.equals(vehicle.condition);
    }

    /**
     * Generates hash code for this
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(serialNum, model, isManufactured, condition);
    }
}
