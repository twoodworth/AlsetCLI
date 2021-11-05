package vehicle;

public class Vehicle {

    private final String serial_num;
    private final int year;
    private final String model;
    private boolean isManufactured;

    public Vehicle(String serial_num, int year, String model, boolean isManufactured) {
        this.serial_num = serial_num;
        this.year = year;
        this.model = model;
        this.isManufactured = isManufactured;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public int getYear() {
        return year;
    }

    public String getModel() {
        return model;
    }

    public boolean isManufactured() {
        return isManufactured;
    }
}
