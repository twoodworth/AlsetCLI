package constants;

public class Statement {

    // select
    public static String VALID_LOGIN_DATA = "SELECT password FROM customer WHERE email=?";
    public static String SERVICE_LOCATION_LOGIN = "SELECT * FROM service_location NATURAL JOIN service_address WHERE password=?";
    public static String EMAIL_EXISTS = "SELECT * FROM customer WHERE email=?";
    public static String GET_CUSTOMER_NAME = "SELECT first, middle, last FROM customer_name WHERE email=?";
    public static String GET_VEHICLE_CONDITION = "SELECT mileage, last_inspection, has_damage FROM vehicle_condition WHERE serial_num=?";
    public static String GET_USER_VEHICLES = "SELECT serial_num, year, name, is_manufactured FROM owner NATURAL JOIN vehicle_model NATURAL JOIN vehicle WHERE email=?";
    public static String GET_VEHICLE = "SELECT serial_num, year, name, is_manufactured FROM vehicle_model NATURAL JOIN vehicle WHERE serial_num=?";
    public static String GET_REPAIRABLE_LOCATIONS = "SELECT location_id, location_name, planet, country, state, city, street, zip FROM service_location NATURAL JOIN service_address NATURAL JOIN repairable WHERE year=? AND name=?";
    public static String GET_OPTIONS = "SELECT option_name FROM vehicle_options WHERE serial_num=?";
    public static String GET_PICKUP_ROW = "SELECT * FROM pickup WHERE serial_num=?";
    public static String GET_SERVICE_LOCATION_OF_VEHICLE = "SELECT location_id, location_name, planet, country, state, city, street, zip FROM pickup NATURAL JOIN service_location NATURAL JOIN service_address WHERE serial_num=?";
    public static String GET_REPAIRABLE_MODELS = "SELECT year, name FROM repairable WHERE location_id=?";
    public static String GET_GARAGE_VEHICLES = "SELECT repairs.email, repairs.serial_num, start_time, end_time, repair_type, price, ready FROM repairs FULL OUTER JOIN pickup ON repairs.serial_num = pickup.serial_num AND repairs.email = pickup.email WHERE location_id=? AND (start_time = (SELECT MAX(start_time) FROM repairs WHERE serial_num = pickup.serial_num))";
    public static String GET_VEHICLE_GARAGE = "SELECT repairs.email, repairs.serial_num, start_time, end_time, repair_type, price, ready FROM repairs FULL OUTER JOIN pickup ON repairs.serial_num = pickup.serial_num AND repairs.email = pickup.email WHERE repairs.serial_num=? AND (start_time = (SELECT MAX(start_time) FROM repairs WHERE serial_num = pickup.serial_num))";

    // update
    public static String UPDATE_PASSWORD = "UPDATE customer SET password=? WHERE email=?";

    // insert
    public static String START_REPAIR = "INSERT INTO repair (start_time, end_time, repair_type, price) VALUES (?, ?, ?, ?)";
    public static String START_VEHICLE_REPAIR = "INSERT INTO repairs (email, serial_num, start_time, end_time, repair_type, price) VALUES (?, ?, ?, ?, ?, ?)";
    public static String ADD_GARAGE_VEHICLE = "INSERT INTO pickup (location_id, email, serial_num, ready) VALUES (?, ?, ?, ?)";
}
