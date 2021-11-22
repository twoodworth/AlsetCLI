package product;

public class Log {
    private String owner;
    private String serial_num;
    private long timestamp;
    private String note;

    protected Log(String owner, String serial_num, long timestamp, String note) {
        this.owner = owner;
        this.serial_num = serial_num;
        this.timestamp = timestamp;
        this.note = note;
    }

    public String getOwner() {
        return owner;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getNote() {
        return note;
    }
}
