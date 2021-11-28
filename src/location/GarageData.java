package location;

import java.util.Date;

/**
 * Contains data for a vehicle being serviced/held at
 * a service location garage
 */
public class GarageData {
    /**
     * Vehicle owner
     */
    private final String owner;

    /**
     * Vehicle serial number
     */
    private final String serialNum;

    /**
     * Repair start time
     */
    private final Long startTime;

    /**
     * Repair end time
     */
    private final Long endTime;

    /**
     * Reason for vehicle being at garage
     */
    private final String reason;

    /**
     * Price of repair
     */
    private final Long repairPrice;

    /**
     * If the vehicle is ready for pickup
     */
    private final boolean isReady;

    /**
     * Constructs a new GarageData
     *
     * @param owner:       Onwer
     * @param serialNum:   Serial Number
     * @param startTime:   Repair start time
     * @param endTime:     Repair end time
     * @param reason:      Repair type
     * @param repairPrice: Repair type
     * @param isReady:     Ready for pickup
     */
    public GarageData(String owner, String serialNum, Long startTime, Long endTime, String reason, Long repairPrice, boolean isReady) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.repairPrice = repairPrice;
        this.isReady = isReady;
        this.owner = owner;
        this.serialNum = serialNum;
    }

    /**
     * Returns the vehicle owner's email
     *
     * @return owner's email
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Returns the vehicle serial number
     *
     * @return serial number
     */
    public String getSerialNum() {
        return serialNum;
    }

    /**
     * Returns the repair start time
     *
     * @return start time
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * Returns the start time as a formatted string
     *
     * @return formatted start time
     */
    public String getStartTimeFormatted() {
        Date date = new java.util.Date(startTime * 1000);
        return date.toString();
    }

    /**
     * Gets the end time
     * @return end time
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * Returns the end time as a formatted string
     * @return end time formatted
     */
    public String getEndTimeFormatted() {
        Date date = new java.util.Date(endTime * 1000);
        return date.toString();
    }

    /**
     * Returns the service reason
     * @return service reason
     */
    public String getReason() {
        if (reason == null) return "Vehicle Purchase";
        return reason;
    }

    /**
     * Gets the repair price
     * @return repair price
     */
    public long getRepairPrice() {
        return repairPrice;
    }

    /**
     * Gets if the vehicle is ready for pickup
     * @return ready for pickup
     */
    public boolean isReady() {
        return isReady;
    }
}
