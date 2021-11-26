package constants;

/**
 * Contains String constants of prepared statements. These prepared statements are to
 * be used by DBManager#java in order for the program to interact with the Alset database.
 */
public class Statement {

    // SELECT STATEMENTS
    /**
     * Used for obtaining a customer's password given their email.
     */
    public static String VALID_LOGIN_DATA =
            "SELECT password " +
                    "FROM customer " +
                    "WHERE email=?";

    /**
     * Used for obtaining data belonging to the service location with the given password.
     */
    public static String SERVICE_LOCATION_LOGIN =
            "SELECT * " +
                    "FROM service_location " +
                        "NATURAL JOIN service_address " +
                    "WHERE password=?";

    /**
     * Used for checking if a given email address exists within the database.
     */
    public static String EMAIL_EXISTS =
            "SELECT * " +
                    "FROM customer " +
                    "WHERE email=?";

    /**
     * Used for fetching the first, middle, and last name of a customer with the given email.
     */
    public static String GET_CUSTOMER_NAME =
            "SELECT first, middle, last " +
                    "FROM customer_name " +
                    "WHERE email=?";

    /**
     * Used for fetching the vehicle condition of a vehicle with a given serial number.
     */
    public static String GET_VEHICLE_CONDITION =
            "SELECT mileage, last_inspection, has_damage " +
                    "FROM vehicle_condition " +
                    "WHERE serial_num=?";

    /**
     * Used for obtaining a set of all vehicles owned by a given customer.
     */
    public static String GET_USER_VEHICLES =
            "SELECT serial_num, year, name, is_manufactured " +
                    "FROM owner " +
                        "NATURAL JOIN vehicle_model " +
                        "NATURAL JOIN vehicle " +
                    "WHERE email=?";

    /**
     * Used for obtaining all data of a vehicle given its serial number.
     */
    public static String GET_VEHICLE =
            "SELECT serial_num, year, name, is_manufactured " +
                    "FROM vehicle_model " +
                        "NATURAL JOIN vehicle " +
                    "WHERE serial_num=?";

    /**
     * Used for getting a set of service locations which can repair a given a model/year.
     */
    public static String GET_REPAIRABLE_LOCATIONS =
            "SELECT location_id, location_name, planet, country, state, city, street, zip " +
                    "FROM service_location " +
                        "NATURAL JOIN service_address " +
                        "NATURAL JOIN repairable " +
                    "WHERE year=? " +
                        "AND name=?";

    /**
     * Used for fetching all custom options belonging to a given vehicle.
     */
    public static String GET_OPTIONS =
            "SELECT option_name " +
                    "FROM vehicle_options " +
                    "WHERE serial_num=?";

    /**
     * Used for determining if a given vehicle is currently being held at a service location for maintenance.
     */
    public static String GET_PICKUP_ROW =
            "SELECT * " +
                    "FROM pickup " +
                    "WHERE serial_num=?";

    /**
     * Used for determining the service location that a given vehicle is currently being serviced at.
     */
    public static String GET_SERVICE_LOCATION_OF_VEHICLE =
            "SELECT location_id, location_name, planet, country, state, city, street, zip " +
                    "FROM pickup " +
                        "NATURAL JOIN service_location " +
                        "NATURAL JOIN service_address " +
                    "WHERE serial_num=?";

    /**
     * Used for determining all the models/years that a service location can repair
     */
    public static String GET_REPAIRABLE_MODELS =
            "SELECT year, name " +
                    "FROM repairable " +
                    "WHERE location_id=?";

    /**
     * Used for getting a set of all vehicles currently being held in a service location's garage,
     * along with relevant service/repair information.
     */
    public static String GET_GARAGE_VEHICLES =
            "SELECT repairs.email, repairs.serial_num, start_time, end_time, repair_type, price, ready " +
                    "FROM repairs " +
                        "FULL OUTER JOIN pickup " +
                        "ON repairs.serial_num = pickup.serial_num " +
                            "AND repairs.email = pickup.email " +
                    "WHERE location_id=? " +
                        "AND (start_time = (" +
                            "SELECT MAX(start_time) " +
                            "FROM repairs " +
                            "WHERE serial_num = pickup.serial_num))";


    /**
     * Used for getting the garage that a given vehicle is currently being serviced at, along with relevant
     * information about the service/repair
     */
    public static String GET_VEHICLE_GARAGE =
            "SELECT repairs.email, repairs.serial_num, start_time, end_time, repair_type, price, ready " +
                    "FROM repairs " +
                    "FULL OUTER JOIN pickup " +
                    "ON repairs.serial_num = pickup.serial_num " +
                    "AND repairs.email = pickup.email " +
                    "WHERE repairs.serial_num=? " +
                    "AND (start_time = (" +
                    "SELECT MAX(start_time) " +
                    "FROM repairs " +
                    "WHERE serial_num = pickup.serial_num))";

    /**
     * Used for getting the email of the owner of a given vehicle.
     */
    public static String GET_VEHICLE_OWNER_EMAIL =
            "SELECT email " +
                    "FROM owner " +
                    "WHERE serial_num=?";

    /**
     * Used for getting all the cards registered to a given vehicle owner.
     */
    public static String GET_CARDS =
            "SELECT first, middle, last, card_num, cvv, exp_month, exp_year, zip, card_type " +
                    "FROM customer " +
                    "NATURAL JOIN customer_name " +
                    "NATURAL JOIN card_holder " +
                    "NATURAL JOIN card " +
                    "WHERE email=?";

    /**
     * Used for checking if a name already exists in the database
     */
    public static String GET_NAME =
            "SELECT * " +
                    "FROM name " +
                    "WHERE first=? " +
                    "AND middle=? " +
                    "AND last=?";

    /**
     * Used for checking if a card already exists in the database
     */
    public static String GET_CARD =
            "SELECT * " +
                    "FROM card " +
                    "WHERE card_num=?";

    // UPDATE STATEMENTS
    /**
     * Used for updating a given customer's password.
     */
    public static String UPDATE_PASSWORD =
            "UPDATE customer " +
                    "SET password=? " +
                    "WHERE email=?";

    /**
     * Used for updating the pickup status of a given vehicle that is being
     * serviced.
     */
    public static String UPDATE_PICKUP_STATUS =
            "UPDATE pickup " +
                    "SET ready=? " +
                    "WHERE serial_num=?";

    /**
     * Used for updating the is_manufactured status of a given vehicle
     */
    public static String UPDATE_IS_MANUFACTURED =
            "UPDATE vehicle " +
                    "SET is_manufactured=? " +
                    "WHERE serial_num=?";

    /**
     * Used for updating the condition of a given vehicle.
     */
    public static String UPDATE_VEHICLE_CONDITION =
            "UPDATE vehicle_condition " +
                    "SET mileage=?, last_inspection=?, has_damage=? " +
                    "WHERE serial_num=?";

    /**
     * Used for adding a credit card number to a repair row after a repair transaction has been
     * completed.
     */
    public static String ADD_REPAIRS_CARD_NUM =
            "UPDATE repairs " +
                    "SET card_num=? " +
                    "WHERE serial_num=? " +
                    "AND start_time=?";

    // INSERT STATEMENTS
    /**
     * Used for adding a new row into repair
     */
    public static String START_REPAIR =
            "INSERT INTO repair (start_time, end_time, repair_type, price) " +
                    "VALUES (?, ?, ?, ?)";

    /**
     * Used for adding a new row into repairs.
     */
    public static String START_VEHICLE_REPAIR =
            "INSERT INTO repairs (email, serial_num, start_time, end_time, repair_type, price) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * Used for adding a new vehicle into a service location's garage.
     */
    public static String ADD_GARAGE_VEHICLE =
            "INSERT INTO pickup (location_id, email, serial_num, ready) " +
                    "VALUES (?, ?, ?, ?)";

    /**
     * Used for inserting a new row into condition
     */
    public static String INSERT_CONDITION =
            "INSERT INTO condition (mileage, last_inspection, has_damage) " +
                    "VALUES (?, ?, ?)";

    /**
     * Used for inserting a new row into vehicle_condition
     */
    public static String INSERT_VEHICLE_CONDITION =
            "INSERT INTO vehicle_condition (serial_num, mileage, last_inspection, has_damage) " +
                    "VALUES (?, ?, ?, ?)";

    /**
     * Used for inserting a new row into name
     */
    public static String INSERT_NAME =
            "INSERT INTO name (first, middle, last) " +
                    "VALUES (?, ?, ?)";

    /**
     * Used for inserting a new row into card
     */
    public static String INSERT_CARD =
            "INSERT INTO card (card_num, cvv, exp_month, exp_year, zip, card_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * Used for inserting a new row into card_holder
     */
    public static String INSERT_CARD_HOLDER =
            "INSERT INTO card_holder (card_num, first, middle, last) " +
                    "VALUES (?, ?, ?, ?)";


    // DELETE STATEMENTS
    /**
     * Used for deleting a row from condition, given the vehicle's mileage and last inspection timestamp.
     */
    public static String DELETE_CONDITION =
            "DELETE FROM condition " +
                    "WHERE mileage=? " +
                    "AND last_inspection=?";

    /**
     * Used for deleting a row from condition, given the vehicle's mileage, last inspection timestamp, and has_damage value
     */
    public static String DELETE_CONDITION_HAS_DAMAGE =
            "DELETE FROM condition " +
                    "WHERE mileage=? " +
                    "AND last_inspection=? " +
                    "AND has_damage=?";

    /**
     * Used from removing a row from pickup
     * after a customer has picked up their vehicle.
     */
    public static String DELETE_PICKUP =
            "DELETE FROM pickup " +
                    "WHERE serial_num=?";
}
