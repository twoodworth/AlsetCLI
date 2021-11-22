package location;

import java.util.Date;

public class GarageData {
    private String owner;
    private String serialNum;
    private long startTime;
    private long endTime;
    private String reason;
    private double repairPrice;
    private boolean isReady;

    public GarageData(String owner, String serialNum, long startTime, long endTime, String reason, double repairPrice, boolean isReady) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.repairPrice = repairPrice;
        this.isReady = isReady;
        this.owner = owner;
        this.serialNum = serialNum;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getStartTimeFormatted() {
        Date date = new java.util.Date(startTime * 1000);
        return date.toString();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getEndTimeFormatted() {
        Date date = new java.util.Date(endTime * 1000);
        return date.toString();
    }


    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(double repairPrice) {
        this.repairPrice = repairPrice;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }
}
