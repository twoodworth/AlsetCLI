package constants;

public class Statements {

    public static String VALID_LOGIN_DATA = "SELECT password FROM customer WHERE email=?";
    public static String EMAIL_EXISTS = "SELECT * FROM customer WHERE email=?";
    public static String UPDATE_PASSWORD = "UPDATE customer SET password=? WHERE email=?";
    public static String GET_CUSTOMER_NAME = "SELECT first, middle, last FROM customer_name WHERE email=?";
    public static String GET_VEHICLE_CONDITION = "SELECT mileage, last_inspection, has_damage FROM vehicle_condition WHERE serial_num=?";
    public static String GET_USER_VEHICLES = "SELECT serial_num, year, name, is_manufactured FROM owner NATURAL JOIN vehicle_model NATURAL JOIN vehicle WHERE email=?";
    public static String GET_REPAIRABLE_LOCATIONS = "SELECT location_id, location_name, planet, country, state, city, street, zip FROM service_location NATURAL JOIN service_address NATURAL JOIN repairable WHERE year=? AND name=?";
}
