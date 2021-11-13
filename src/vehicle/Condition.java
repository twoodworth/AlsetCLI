package vehicle;

import java.util.Date;

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
}




