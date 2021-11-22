package product;

import java.util.Date;
import java.util.Objects;

public class Condition {
    private long mileage;
    private long lastInspection;
    private boolean hasDamage;

    public Condition(long mileage, long lastInspection, boolean hasDamage) {
        this.mileage = mileage;
        this.lastInspection = lastInspection;
        this.hasDamage = hasDamage;
    }

    public long getMileage() {
        return mileage;
    }

    public long getLastInspection() {
        return lastInspection;
    }

    public String getLastInspectionFormatted() {
        Date date = new java.util.Date(lastInspection * 1000);
        return date.toString();
    }

    public boolean hasDamage() {
        return hasDamage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return mileage == condition.mileage &&
                lastInspection == condition.lastInspection &&
                hasDamage == condition.hasDamage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mileage, lastInspection, hasDamage);
    }
}




