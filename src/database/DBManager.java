package database;

import connection.ConnectionManager;
import constants.Keys;
import constants.Statements;
import constants.Strings;
import io.IOManager;
import location.Address;
import location.GarageData;
import location.ServiceLocation;
import menu.MenuManager;
import product.Condition;
import product.Model;
import product.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;

/**
 * Manages interactions with the Alset database
 */
public class DBManager {

    /**
     * Private constructor of DatabaseManager
     */
    private DBManager() {
    }

    /**
     * Checks if the given email and password are correct and correspond to an Alset account
     *
     * @param email: Email to check
     * @param pwd:   Password to check
     * @return true if the credentials are valid, otherwise false.
     */
    public static boolean validLoginData(String email, String pwd) {
        try {
            boolean valid;
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.VALID_LOGIN_DATA);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            valid = rs.next() && rs.getString("password").equals(pwd);
            rs.close();
            return valid;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Returns the service location which has the given address,
     * or null if none exist.
     *
     * @param pwd: pwd of service location
     * @return service location
     */
    public static ServiceLocation getServiceLocation(String pwd) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.SERVICE_LOCATION_LOGIN);
            s.setString(1, pwd);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                return new ServiceLocation(
                        rs.getString("location_id"),
                        rs.getString("location_name"),
                        new Address(
                                rs.getString("planet"),
                                rs.getString("country"),
                                rs.getString("state"),
                                rs.getString("city"),
                                rs.getString("street"),
                                rs.getString("zip"),
                                null
                        )

                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Checks if there is an account belonging to the given email.
     *
     * @param email: Email to check
     * @return true if an account is found, otherwise false.
     */
    public static boolean emailExists(String email) {
        try {
            boolean valid;
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.EMAIL_EXISTS);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            valid = rs.next();
            rs.close();
            return valid;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Checks if there is an account belonging to the given email.
     *
     * @param email: Email to check
     * @param pass:  New password
     * @return true if password was successfully updated, otherwise false.
     */
    public static boolean updatePassword(String email, String pass) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.UPDATE_PASSWORD);
            s.setString(1, pass);
            s.setString(2, email);
            s.execute();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Returns the name of a given customer.
     *
     * @param email: Email of customer
     * @return array of customer's first, middle, and last names
     */
    public static String[] getName(String email) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_CUSTOMER_NAME);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                String[] name = new String[3];
                name[0] = rs.getString("first");
                name[1] = rs.getString("middle");
                name[2] = rs.getString("last");
                return name;
            } else {
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    public static Condition getCondition(String serialNum) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_VEHICLE_CONDITION);
            s.setString(1, serialNum);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                return new Condition(
                        rs.getLong("mileage"),
                        rs.getLong("last_inspection"),
                        Boolean.parseBoolean(rs.getString("has_damage"))
                );
            }
            return null;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Returns the list of vehicles currently owned by the logged-in user.
     *
     * @param email: email of user
     * @return cars owned by the user
     */
    public static HashSet<Vehicle> getVehicles(String email) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_USER_VEHICLES);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            HashSet<Vehicle> vehicles = new HashSet<>();

            while (rs.next()) {
                String sn = rs.getString("serial_num");
                Vehicle vehicle = new Vehicle(
                        sn,
                        rs.getInt("year"),
                        rs.getString("name"),
                        Boolean.parseBoolean(rs.getString("is_manufactured")),
                        getCondition(sn)
                );
                vehicles.add(vehicle);
            }
            return vehicles;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    public static Vehicle getVehicle(String serialNum) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_VEHICLE);
            s.setString(1, serialNum);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        serialNum,
                        rs.getInt("year"),
                        rs.getString("name"),
                        Boolean.parseBoolean(rs.getString("is_manufactured")),
                        getCondition(serialNum)
                );
            } else {
                MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    public static HashSet<Model> getRepairableModels(ServiceLocation location) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_REPAIRABLE_MODELS);
            s.setString(1, location.getId());
            ResultSet rs = s.executeQuery();
            HashSet<Model> models = new HashSet<>();
            while (rs.next()) {
                models.add(new Model(rs.getInt("year"), rs.getString("name")));
            }
            return models;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }

    }

    /**
     * Returns a set of all service locations that are able to
     * repair a given vehicle.
     *
     * @param vehicle: vehicle to repair
     * @return set of locations
     */
    public static HashSet<ServiceLocation> getRepairableLocations(Vehicle vehicle) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_REPAIRABLE_LOCATIONS);
            s.setInt(1, vehicle.getYear());
            s.setString(2, vehicle.getModelName());
            ResultSet rs = s.executeQuery();
            HashSet<ServiceLocation> locations = new HashSet<>();

            while (rs.next()) {
                String id = rs.getString("location_id");
                String name = rs.getString("location_name");
                Address address = new Address(
                        rs.getString("planet"),
                        rs.getString("country"),
                        rs.getString("state"),
                        rs.getString("city"),
                        rs.getString("street"),
                        rs.getString("zip"),
                        null
                );
                locations.add(new ServiceLocation(id, name, address));
            }
            return locations;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Returns all the additional options of a vehicle
     *
     * @param serialNum: Serial number of vehicle
     * @return set of names of additional options
     */
    public static HashSet<String> getOptions(String serialNum) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_OPTIONS);
            s.setString(1, serialNum);
            ResultSet rs = s.executeQuery();
            HashSet<String> options = new HashSet<>();

            while (rs.next()) {
                options.add(rs.getString("option_name"));
            }
            return options;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    public static boolean isAtServiceLocation(Vehicle vehicle) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_PICKUP_ROW);
            s.setString(1, vehicle.getSerialNum());
            return s.executeQuery().next();
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return false;
        }
    }

    public static HashSet<GarageData> getGarageData(ServiceLocation location) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_GARAGE_VEHICLES);
            s.setString(1, location.getId());
            ResultSet rs = s.executeQuery();
            HashSet<GarageData> data = new HashSet<>();
            while (rs.next()) {
                data.add(
                        new GarageData(
                                rs.getString("email"),
                                rs.getString("serial_num"),
                                rs.getLong("start_time"),
                                rs.getLong("end_time"),
                                rs.getString("repair_type"),
                                rs.getDouble("price"),
                                rs.getBoolean("ready")
                        )
                );
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    public static boolean isReadyForPickup(Vehicle vehicle) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_PICKUP_ROW);
            s.setString(1, vehicle.getSerialNum());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                return Boolean.parseBoolean(rs.getString("ready"));
            } else {
                throw new IllegalArgumentException("Vehicle is not at a service location.");
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return false;
        }
    }

    public static ServiceLocation getServiceLocation(Vehicle vehicle) {
        try {
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.GET_SERVICE_LOCATION_OF_VEHICLE);
            s.setString(1, vehicle.getSerialNum());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                return new ServiceLocation(
                        rs.getString("location_id"),
                        rs.getString("location_name"),
                        new Address(
                                rs.getString("planet"),
                                rs.getString("country"),
                                rs.getString("state"),
                                rs.getString("city"),
                                rs.getString("street"),
                                rs.getString("zip"),
                                null
                        )

                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return null;
        }
    }

    public static boolean addGarageVehicle(ServiceLocation location, Vehicle vehicle, String type, String email) {
        try {
            // add to repair
            PreparedStatement s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.START_REPAIR);
            long start = new Date().getTime();
            s.setLong(1, start);
            s.setLong(2, 0L);
            s.setString(3, type);
            s.setLong(4, 0);
            s.executeQuery();
            s.close();

            // add to repairs
            s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.START_VEHICLE_REPAIR);
            s.setString(1, email);
            s.setString(2, vehicle.getSerialNum());
            s.setLong(3, start);
            s.setLong(4, 0);
            s.setString(5, type);
            s.setLong(6, 0);
            s.executeQuery();
            s.close();

            // add to pickup
            s = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statements.ADD_GARAGE_VEHICLE);
            s.setString(1, location.getId());
            s.setString(2, email);
            s.setString(3, vehicle.getSerialNum());
            s.setString(4, "False");
            s.executeQuery();
            s.close();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Keys.EDGAR1_MENU_KEY, Strings.DB_ERROR);
            return false;
        }
    }
}
