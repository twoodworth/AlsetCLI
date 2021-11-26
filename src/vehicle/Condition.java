package vehicle;

import java.util.Date;
import java.util.Objects;

/**
 * Contains information about a vehicle's condition
 */
public class Condition {
    /**
     * Mileage of vehicle
     */
    private final long mileage;

    /**
     * Timestamp of last inspection
     */
    private final long lastInspection;

    /**
     * If the vehicle has damage
     */
    private final boolean hasDamage;

    /**
     * Constructs a new Condition
     *
     * @param mileage:        Mileage
     * @param lastInspection: Last inspection timestamp
     * @param hasDamage:      If vehicle has damage
     */
    public Condition(long mileage, long lastInspection, boolean hasDamage) {
        this.mileage = mileage;
        this.lastInspection = lastInspection;
        this.hasDamage = hasDamage;
    }

    /**
     * Returns mileage of vehicle
     *
     * @return mileage
     */
    public long getMileage() {
        return mileage;
    }

    /**
     * Returns last inspection timestamp of vehicle
     *
     * @return last inspection
     */
    public long getLastInspection() {
        return lastInspection;
    }

    /**
     * Returns last inspection of timestamp as a formatted string
     *
     * @return formatted last inspection
     */
    public String getLastInspectionFormatted() {
        Date date = new java.util.Date(lastInspection * 1000);
        return date.toString();
    }

    /**
     * Returns if vehicle has damage
     *
     * @return has damage
     */
    public boolean hasDamage() {
        return hasDamage;
    }

    /**
     * Used for comparing this to other objects.
     *
     * @param o: Other object
     * @return true if this is equal to o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return mileage == condition.mileage &&
                lastInspection == condition.lastInspection &&
                hasDamage == condition.hasDamage;
    }

    /**
     * Provides hash code for this.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(mileage, lastInspection, hasDamage);
    }
}




