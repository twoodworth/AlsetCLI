package database;

import card.Card;
import connection.ConnectionManager;
import constants.Key;
import constants.Statement;
import constants.Strings;
import io.IOManager;
import location.Address;
import location.GarageData;
import location.ServiceLocation;
import location.ServiceManager;
import menu.MenuManager;
import user.User;
import vehicle.Condition;
import vehicle.Model;
import vehicle.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.VALID_LOGIN_DATA);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            valid = rs.next() && rs.getString("password").equals(pwd);
            s.close();
            return valid;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.SERVICE_LOCATION_LOGIN);
            s.setString(1, pwd);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                ServiceLocation location = new ServiceLocation(
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
                s.close();
                return location;
            } else {
                s.close();
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.EMAIL_EXISTS);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            valid = rs.next();
            s.close();
            return valid;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.UPDATE_PASSWORD);
            s.setString(1, pass);
            s.setString(2, email);
            s.execute();
            s.close();
            ConnectionManager.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_CUSTOMER_NAME);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                String[] name = new String[3];
                name[0] = rs.getString("first");
                name[1] = rs.getString("middle");
                name[2] = rs.getString("last");
                s.close();
                return name;
            } else {
                s.close();
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Returns the condition of a given vehicle
     *
     * @param serialNum: serial number of vehicle
     * @return condition of vehicle
     */
    public static Condition getCondition(String serialNum) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_VEHICLE_CONDITION);
            s.setString(1, serialNum);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                Condition c = new Condition(
                        rs.getLong("mileage"),
                        rs.getLong("last_inspection"),
                        Boolean.parseBoolean(rs.getString("has_damage"))
                );
                s.close();
                return c;
            }
            s.close();
            return null;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_USER_VEHICLES);
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
            s.close();
            return vehicles;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Creates a vehicle object using the vehicle's serial number
     *
     * @param serialNum: Serial number of vehicle
     * @return object representing vehicle
     */
    public static Vehicle getVehicle(String serialNum) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_VEHICLE);
            s.setString(1, serialNum);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                Vehicle v = new Vehicle(
                        serialNum,
                        rs.getInt("year"),
                        rs.getString("name"),
                        Boolean.parseBoolean(rs.getString("is_manufactured")),
                        getCondition(serialNum)
                );
                s.close();
                return v;
            } else {
                s.close();
                MenuManager.setNextMessage(Strings.DB_ERROR);
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Gets a set of models that a given service location is capable of repairing.
     *
     * @param location: Service location
     * @return hash set of repairable models
     */
    public static HashSet<Model> getRepairableModels(ServiceLocation location) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_REPAIRABLE_MODELS);
            s.setString(1, location.getId());
            ResultSet rs = s.executeQuery();
            HashSet<Model> models = new HashSet<>();
            while (rs.next()) {
                models.add(new Model(rs.getInt("year"), rs.getString("name")));
            }
            s.close();
            return models;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }

    }

    public static Set<String> getBuyableModels() {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_BUYABLE_MODELS);
            ResultSet rs = s.executeQuery();
            HashSet<String> models = new HashSet<>();
            while (rs.next()) models.add(rs.getString("name"));
            s.close();
            return models;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return new HashSet<>();
        }
    }


    public static Set<Integer> getBuyableYears(String model) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_BUYABLE_YEARS);
            s.setString(1, model);
            ResultSet rs = s.executeQuery();
            HashSet<Integer> years = new HashSet<>();
            while (rs.next()) years.add(rs.getInt("year"));
            s.close();
            return years;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return new HashSet<>();
        }
    }

    public static List<String> getTransactionList(String email) {
        try {
            // add vehicle purchases
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_PURCHASE_HISTORY);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            HashMap<Long, String> transactions = new HashMap<>();
            while (rs.next()) {
                long timestamp = rs.getLong("timestamp") * 1000L;
                int year = rs.getInt("year");
                String name = rs.getString("name");
                String sn = rs.getString("serial_num");
                long price = rs.getLong("sales_price");
                String cardNum = rs.getString("card_num");
                String transaction =
                        "[" + new Date(timestamp).toString() + "] "
                                + year + " Model " + name + " (SN: " + sn + ") purchased for $"
                                + price + " (Card: XXXXXXXXXXXX" + cardNum.substring(12, 16) + ")";
                transactions.put(timestamp, transaction);
            }
            s.close();

            // add repairs
            s =
                    ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statement.GET_REPAIR_HISTORY);
            s.setString(1, email);
            rs = s.executeQuery();
            while (rs.next()) {
                long timestamp = rs.getLong("start_time") * 1000L;
                int year = rs.getInt("year");
                String name = rs.getString("name");
                String sn = rs.getString("serial_num");
                long price = rs.getLong("price");
                String type = rs.getString("repair_type");
                String cardNum = rs.getString("card_num");
                String transaction =
                        "[" + new Date(timestamp).toString() + "] "
                                + type + " of "
                                + year + " Model " + name + " (SN: " + sn + ") for $"
                                + price + " (Card: XXXXXXXXXXXX" + cardNum.substring(12, 16) + ")";
                transactions.put(timestamp, transaction);
            }
            ArrayList<String> strings = new ArrayList<>();
            for (Long l : transactions.keySet().stream().sorted(Long::compareTo).collect(Collectors.toList())) {
                strings.add(transactions.get(l));
            }
            s.close();
            return strings;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_REPAIRABLE_LOCATIONS);
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
            s.close();
            return locations;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
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
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_OPTIONS);
            s.setString(1, serialNum);
            ResultSet rs = s.executeQuery();
            HashSet<String> options = new HashSet<>();

            while (rs.next()) {
                options.add(rs.getString("option_name"));
            }
            s.close();
            return options;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Returns a set of garage data belonging to vehicles of unfinished services.
     *
     * @param location: Service location to collect data from
     * @return data of unfinished vehicles at location
     */
    public static HashSet<GarageData> getUnfinishedVehicleGarageData(ServiceLocation location) {
        // get garage data
        HashSet<GarageData> data = getGarageData(location);
        if (data == null) return null;

        // remove finished vehicle data
        data.removeIf(GarageData::isReady);

        // return data
        return data;
    }

    /**
     * Returns a set of garage data belonging to vehicles of finished services.
     *
     * @param location: Service location to collect data from
     * @return data of finished vehicles at location
     */
    public static HashSet<GarageData> getFinishedVehicleGarageData(ServiceLocation location) {
        // get garage data
        HashSet<GarageData> data = getGarageData(location);
        if (data == null) return null;

        // remove unfinished vehicles from set
        data.removeIf(gd -> !gd.isReady());

        // return data
        return data;
    }

    /**
     * Returns a set of garage data. Each garage data object contains information
     * about a vehicle and relevant service/repair information
     *
     * @param location: Service location
     * @return hash set of garage data
     */
    public static HashSet<GarageData> getGarageData(ServiceLocation location) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_GARAGE_VEHICLES);
            s.setString(1, location.getId());
            ResultSet rs = s.executeQuery();
            HashSet<GarageData> data = new HashSet<>();
            while (rs.next()) {
                String email = rs.getString("email");
                if (email == null) {
                    data.add(
                            new GarageData(
                                    null,
                                    rs.getString("serial_num"),
                                    null,
                                    null,
                                    rs.getString("repair_type"),
                                    null,
                                    rs.getBoolean("ready")
                            )
                    );
                } else {
                    data.add(
                            new GarageData(
                                    email,
                                    rs.getString("serial_num"),
                                    rs.getLong("start_time"),
                                    rs.getLong("end_time"),
                                    rs.getString("repair_type"),
                                    rs.getLong("price"),
                                    rs.getBoolean("ready")
                            )
                    );
                }
            }
            s.close();
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Determines if a given vehicle is ready for pickup from
     * a service location.
     *
     * @param vehicle: Vehicle
     * @return true if vehicle is ready for pickup, otherwise false
     */
    public static boolean isReadyForPickup(Vehicle vehicle) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_PICKUP_ROW);
            s.setString(1, vehicle.getSerialNum());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                boolean ready = Boolean.parseBoolean(rs.getString("ready"));
                s.close();
                return ready;
            } else {
                s.close();
                throw new IllegalArgumentException("Vehicle is not at a service location.");
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Determines the service location that a vehicle is currently
     * being serviced at.
     *
     * @param vehicle: Vehicle
     * @return service location that vehicle is being serviced at
     */
    public static ServiceLocation getServiceLocation(Vehicle vehicle) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_SERVICE_LOCATION_OF_VEHICLE);
            s.setString(1, vehicle.getSerialNum());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                ServiceLocation location = new ServiceLocation(
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
                s.close();
                return location;
            } else {
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Determines if a given vehicle is currently being held
     * at a service location's garage.
     *
     * @param vehicle: Vehicle
     * @return true if at service location garage, otherwise false
     */
    public static boolean isAtGarage(Vehicle vehicle) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_VEHICLE_GARAGE);
            s.setString(1, vehicle.getSerialNum());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                s.close();
                return true;
            } else {
                s.close();
                return false;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return true;
        }
    }

    /**
     * Adds a given vehicle to a service location's garage
     *
     * @param location: Service location
     * @param vehicle:  Vehicle
     * @param type:     Type of servicing
     * @param email:    Email of vehicle owner
     * @param price:    Price of service provided
     * @param length:   Duration of servicing
     * @return true if successfully added, otherwise false
     */
    public static boolean addGarageVehicle(ServiceLocation location, Vehicle vehicle, String type, String email, long price, long length) {
        try {
            // add to repair
            PreparedStatement s1 = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statement.START_REPAIR);
            long start = new Date().getTime() / 1000L;
            s1.setLong(1, start);
            s1.setLong(2, start + length);
            s1.setString(3, type);
            s1.setLong(4, price);
            s1.execute();
            s1.close();

            // add to repairs
            PreparedStatement s2 = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statement.START_VEHICLE_REPAIR);
            s2.setString(1, email);
            s2.setString(2, vehicle.getSerialNum());
            s2.setLong(3, start);
            s2.setLong(4, start + length);
            s2.setString(5, type);
            s2.setLong(6, price);
            s2.execute();
            s2.close();

            // add to pickup
            PreparedStatement s3 = ConnectionManager
                    .getCurrentConnection()
                    .prepareStatement(Statement.ADD_GARAGE_VEHICLE);
            s3.setString(1, location.getId());
            s3.setString(2, email);
            s3.setString(3, vehicle.getSerialNum());
            s3.setString(4, "False");
            s3.execute();
            s3.close();
            ConnectionManager.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Returns the email of a vehicle's owner
     *
     * @param vehicle: Vehicle
     * @return owner's email
     */
    public static String getEmail(Vehicle vehicle) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_VEHICLE_OWNER_EMAIL);
            s.setString(1, vehicle.getSerialNum());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                s.close();
                return email;
            } else {
                s.close();
                return null;
            }
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    /**
     * Gets a set of cards under a given customer's name
     *
     * @param email: Email of customer
     * @return set of cards
     */
    public static HashSet<Card> getCards(String email) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_CARDS);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            HashSet<Card> cards = new HashSet<>();
            while (rs.next()) {
                cards.add(
                        new Card(
                                rs.getString("card_num"),
                                rs.getString("cvv"),
                                rs.getInt("exp_month"),
                                rs.getInt("exp_year"),
                                rs.getString("zip"),
                                rs.getString("card_type"),
                                rs.getString("first"),
                                rs.getString("middle"),
                                rs.getString("last")
                        )
                );
            }
            s.close();
            return cards;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    public static boolean addNewCard(Card card) {
        try {
            // Check if name is in database
            Connection current = ConnectionManager.getCurrentConnection();
            PreparedStatement s1 = current.prepareStatement(Statement.GET_NAME);
            s1.setString(1, card.getFirst());
            s1.setString(2, card.getMiddle());
            s1.setString(3, card.getLast());
            ResultSet rs1 = s1.executeQuery();
            boolean hasName = rs1.next();
            s1.close();

            // add name if not in database
            if (!hasName) {
                PreparedStatement s1b = current.prepareStatement(Statement.INSERT_NAME);
                s1b.setString(1, card.getFirst());
                s1b.setString(2, card.getMiddle());
                s1b.setString(3, card.getLast());
                s1b.execute();
                s1b.close();
            }

            // Check if card is in the database
            PreparedStatement s2 = current.prepareStatement(Statement.GET_CARD);
            s2.setString(1, card.getNum());
            ResultSet rs2 = s2.executeQuery();
            boolean hasCard = rs2.next();

            // add card if not in database
            if (!hasCard) {
                PreparedStatement s2b = current.prepareStatement(Statement.INSERT_CARD);
                s2b.setString(1, card.getNum());
                s2b.setString(2, card.getCvv());
                s2b.setInt(3, card.getExp_month());
                s2b.setInt(4, card.getEx_year());
                s2b.setString(5, card.getZip());
                s2b.setString(6, card.getType());
                s2b.execute();
                s2b.close();
            }

            // add card_holder relationship if not in database
            if (!hasCard || !hasName) {
                PreparedStatement s3 = current.prepareStatement(Statement.INSERT_CARD_HOLDER);
                s3.setString(1, card.getNum());
                s3.setString(2, card.getFirst());
                s3.setString(3, card.getMiddle());
                s3.setString(4, card.getLast());
                s3.execute();
                s3.close();
            }

            ConnectionManager.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Updates the database to express that a vehicle has finished
     * being manufactured, and has arrived at a service location.
     *
     * @param serialNum: serial number of vehicle
     * @return true if successful, otherwise false.
     */
    public static boolean finishManufactured(String serialNum) {
        try {
            Connection current = ConnectionManager.getCurrentConnection();
            // update pickup status
            PreparedStatement s1 = current.prepareStatement(Statement.UPDATE_PICKUP_STATUS);
            s1.setString(1, "True");
            s1.setString(2, serialNum);
            s1.execute();
            s1.close();

            // update isManufactured
            PreparedStatement s2 = current.prepareStatement(Statement.UPDATE_IS_MANUFACTURED);
            s2.setString(1, "True");
            s2.setString(2, serialNum);
            s2.execute();
            s2.close();

            // insert condition
            long time = new Date().getTime() / 1000L;
            PreparedStatement s3 = current.prepareStatement(Statement.INSERT_CONDITION);
            s3.setLong(1, 0);
            s3.setLong(2, time);
            s3.setString(3, "False");
            s3.execute();
            s3.close();

            // insert vehicle condition
            PreparedStatement s4 = current.prepareStatement(Statement.INSERT_VEHICLE_CONDITION);
            s4.setString(1, serialNum);
            s4.setLong(2, 0);
            s4.setLong(3, time);
            s4.setString(4, "False");
            s4.execute();
            s4.close();

            ConnectionManager.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Updates the database to express that an inspection has been completed on a given
     * vehicle.
     *
     * @param vehicle:          Vehicle inspected
     * @return true if successful, otherwise false
     */
    public static boolean finishInspection(Vehicle vehicle) {
        try {
            boolean needsMaintenance = IOManager.getBooleanInput("Is this vehicle in need of maintenance?");

            String hasDamage;
            if (needsMaintenance) hasDamage = "True";
            else hasDamage = "False";

            String sn = vehicle.getSerialNum();
            Connection current = ConnectionManager.getCurrentConnection();
            // update pickup status
            PreparedStatement s1 = current.prepareStatement(Statement.UPDATE_PICKUP_STATUS);
            s1.setString(1, "True");
            s1.setString(2, sn);
            s1.execute();
            s1.close();

            // insert new condition
            long time = new Date().getTime() / 1000L;
            Condition condition = vehicle.getCondition();
            long oldTime = condition.getLastInspection();
            long mileage = condition.getMileage();
            PreparedStatement s3 = current.prepareStatement(Statement.INSERT_CONDITION);
            s3.setLong(1, mileage);
            s3.setLong(2, time);
            s3.setString(3, hasDamage);
            s3.execute();
            s3.close();

            // update vehicle condition
            PreparedStatement s4 = current.prepareStatement(Statement.UPDATE_VEHICLE_CONDITION);
            s4.setLong(1, vehicle.getCondition().getMileage());
            s4.setLong(2, time);
            s4.setString(3, hasDamage);
            s4.setString(4, sn);
            s4.execute();
            s4.close();

            // remove old condition
            PreparedStatement s5 = current.prepareStatement(Statement.DELETE_CONDITION);
            s5.setLong(1, mileage);
            s5.setLong(2, oldTime);
            s5.execute();
            s5.close();

            ConnectionManager.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    /**
     * Updates the database to express that a vehicle servicing has been
     * completed.
     *
     * @param vehicle: Vehicle serviced
     * @return true if successful, otherwise false
     */
    public static boolean finishServicing(Vehicle vehicle) {
        try {

            String sn = vehicle.getSerialNum();
            Connection current = ConnectionManager.getCurrentConnection();

            // update pickup status
            PreparedStatement s1 = current.prepareStatement(Statement.UPDATE_PICKUP_STATUS);
            s1.setString(1, "True");
            s1.setString(2, sn);
            s1.execute();
            s1.close();

            // insert new condition (if needed)
            Condition condition = vehicle.getCondition();
            boolean oldDamage = condition.hasDamage();
            if (oldDamage) {
                PreparedStatement s2 = current.prepareStatement(Statement.INSERT_CONDITION);
                s2.setLong(1, condition.getMileage());
                s2.setLong(2, condition.getLastInspection());
                s2.setString(3, "False");
                s2.execute();
                s2.close();

                // update vehicle condition
                PreparedStatement s3 = current.prepareStatement(Statement.INSERT_VEHICLE_CONDITION);
                s3.setString(1, sn);
                s3.setLong(2, condition.getMileage());
                s3.setLong(3, condition.getLastInspection());
                s3.setString(4, "False");
                s3.execute();
                s3.close();

                // remove old vehicle condition
                PreparedStatement s4 = current.prepareStatement(Statement.DELETE_CONDITION_HAS_DAMAGE);
                s4.setLong(1, condition.getMileage());
                s4.setLong(2, condition.getLastInspection());
                s4.setString(3, "True");
                s4.execute();
                s4.close();
            }

            ConnectionManager.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    public static boolean removeGarageVehicle(GarageData gd, Card card) {
        try {
            boolean isPurchase = gd.getReason().equals("Vehicle Purchase");
            Connection current = ConnectionManager.getCurrentConnection();
            if (!isPurchase) {
                // add credit card to repairs row
                PreparedStatement s = current.prepareStatement(Statement.ADD_REPAIRS_CARD_NUM);
                s.setString(1, card.getNum());
                s.setString(2, gd.getSerialNum());
                s.setLong(3, gd.getStartTime());
                s.execute();
                s.close();
            }

            // remove vehicle from pickup
            PreparedStatement s = current.prepareStatement(Statement.DELETE_PICKUP);
            s.setString(1, gd.getSerialNum());
            s.execute();
            s.close();
            current.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    public static HashSet<ServiceLocation> getServiceLocations() {
        try {

            // remove vehicle from pickup
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_SERVICE_LOCATIONS);
            ResultSet rs = s.executeQuery();
            HashSet<ServiceLocation> locations = new HashSet<>();
            while (rs.next()) {
                locations.add(
                        new ServiceLocation(
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
                        )
                );
            }
            s.close();
            return locations;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return new HashSet<>();
        }
    }

    public static Set<String> getBuyableOptions(Model model) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_BUYABLE_OPTIONS);
            s.setString(1, model.getName());
            s.setInt(2, model.getYear());
            ResultSet rs = s.executeQuery();
            HashSet<String> options = new HashSet<>();
            while (rs.next()) options.add(rs.getString("option_name"));
            s.close();
            return options;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return new HashSet<>();
        }
    }

    public static Long getModelCost(Model model) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_MODEL_COST);
            s.setInt(1, model.getYear());
            s.setString(2, model.getName());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                long l = rs.getLong("model_price");
                s.close();
                return l;
            }
            s.close();
            return null;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    public static Long getOptionCost(String option) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_OPTION_COST);
            s.setString(1, option);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                long l = rs.getLong("option_price");
                s.close();
                return l;
            }
            return null;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    public static String getNewSN() {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_MAX_SN);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                StringBuilder sn = new StringBuilder(rs.getString("sn"));
                long l = Long.parseLong(sn.toString()) + 1;
                sn = new StringBuilder(String.valueOf(l));
                while (sn.length() < 9) sn.insert(0, "0");
                s.close();
                return sn.toString();
            }
            return null;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return null;
        }
    }

    public static boolean purchaseVehicle(Model model, HashSet<String> options, ServiceLocation location, Card card, Long price, User user) {
        try {

            // generate new serial number
            String sn = getNewSN();
            if (sn == null) return false;

            // get timestamp
            long timestamp = new Date().getTime() / 1000L;

            // get connection
            Connection current = ConnectionManager.getCurrentConnection();

            // insert vehicle into database
            PreparedStatement s = current.prepareStatement(Statement.ADD_VEHICLE);
            s.setString(1, sn);
            s.setString(2, "False");
            s.execute();
            s.close();

            // add vehicle model relation to database
            s = current.prepareStatement(Statement.ADD_VEHICLE_MODEL);
            s.setString(1, sn);
            s.setInt(2, model.getYear());
            s.setString(3, model.getName());
            s.execute();
            s.close();

            // add custom option relations to database (sn, option_name)
            for (String option : options) {
                s = current.prepareStatement(Statement.ADD_VEHICLE_OPTION);
                s.setString(1, option);
                s.setString(2, sn);
                s.execute();
                s.close();
            }

            // add owner relation (email, sn)
            s = current.prepareStatement(Statement.ADD_OWNER);
            s.setString(1, user.getEmail());
            s.setString(2, sn);
            s.execute();
            s.close();

            // add transaction to database (timestamp, price)
            s = current.prepareStatement(Statement.ADD_TRANSACTION);
            s.setLong(1, timestamp);
            s.setLong(2, price);
            s.execute();
            s.close();

            // add purchase to database (email, sn, timestamp, price, card)
            s = current.prepareStatement(Statement.ADD_PURCHASE);
            s.setString(1, user.getEmail());
            s.setString(2, sn);
            s.setLong(3, timestamp);
            s.setLong(4, price);
            s.setString(5, card.getNum());
            s.execute();
            s.close();

            // add vehicle to pickup
            s = current.prepareStatement(Statement.ADD_GARAGE_VEHICLE);
            s.setString(1, location.getId());
            s.setString(2, user.getEmail());
            s.setString(3, sn);
            s.setString(4, "False");
            s.execute();
            s.close();

            // commit & return
            current.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    public static boolean orderShowroomVehicle(Model model, HashSet<String> options, ServiceLocation location) {
        try {

            // generate new serial number
            String sn = getNewSN();
            if (sn == null) return false;

            // get connection
            Connection current = ConnectionManager.getCurrentConnection();

            // insert vehicle into database
            PreparedStatement s = current.prepareStatement(Statement.ADD_VEHICLE);
            s.setString(1, sn);
            s.setString(2, "False");
            s.execute();
            s.close();

            // add vehicle model relation to database
            s = current.prepareStatement(Statement.ADD_VEHICLE_MODEL);
            s.setString(1, sn);
            s.setInt(2, model.getYear());
            s.setString(3, model.getName());
            s.execute();
            s.close();

            // add custom option relations to database (sn, option_name)
            for (String option : options) {
                s = current.prepareStatement(Statement.ADD_VEHICLE_OPTION);
                s.setString(1, option);
                s.setString(2, sn);
                s.execute();
                s.close();
            }

            // add vehicle to showroom
            s = current.prepareStatement(Statement.ADD_SHOWROOM_VEHICLE);
            s.setString(1, location.getId());
            s.setString(2, sn);
            s.execute();
            s.close();

            // commit & return
            current.commit();
            return true;
        } catch (SQLException e) {
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    public static HashSet<Vehicle> getShowroomVehicles(ServiceLocation loc) {
        try {
            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_SHOWROOM_VEHICLES);
            s.setString(1, loc.getId());
            ResultSet rs = s.executeQuery();
            HashSet<Vehicle> vehicles = new HashSet<>();
            while (rs.next()) {
                String sn = rs.getString("serial_num");
                Vehicle v = DBManager.getVehicle(sn);
                if (v != null) vehicles.add(v);
            }
            s.close();
            return vehicles;
        } catch (SQLException e) {
            e.printStackTrace();
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return new HashSet<>();
        }
    }

    public static HashSet<Vehicle> getManufacturedShowroomVehicles(ServiceLocation loc) {
        HashSet<Vehicle> vehicles = getShowroomVehicles(loc);
        vehicles.removeIf(v -> !v.isManufactured());
        return vehicles;
    }

    public static HashSet<Vehicle> getOrderedVehicles() {
        try {

            PreparedStatement s =
                    ConnectionManager
                            .getCurrentConnection()
                            .prepareStatement(Statement.GET_SHOWROOM_VEHICLES);
            s.setString(1, ServiceManager.getCurrent().getId());
            ResultSet rs = s.executeQuery();
            HashSet<Vehicle> vehicles = new HashSet<>();
            while (rs.next()) {
                String sn = rs.getString("serial_num");
                Vehicle v = DBManager.getVehicle(sn);
                if (v != null && !v.isManufactured()) vehicles.add(v);
            }
            s.close();
            return vehicles;
        } catch (SQLException e) {
            e.printStackTrace();
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return new HashSet<>();
        }
    }

    public static boolean addShowroomVehicle(Vehicle v) {
        try {
            Connection current = ConnectionManager.getCurrentConnection();
            // update isManufactured
            PreparedStatement s2 = current.prepareStatement(Statement.UPDATE_IS_MANUFACTURED);
            s2.setString(1, "True");
            s2.setString(2, v.getSerialNum());
            s2.execute();
            s2.close();

            // insert condition
            long time = new Date().getTime() / 1000L;
            PreparedStatement s3 = current.prepareStatement(Statement.INSERT_CONDITION);
            s3.setLong(1, 0);
            s3.setLong(2, time);
            s3.setString(3, "False");
            s3.execute();
            s3.close();

            // insert vehicle condition
            PreparedStatement s4 = current.prepareStatement(Statement.INSERT_VEHICLE_CONDITION);
            s4.setString(1, v.getSerialNum());
            s4.setLong(2, 0);
            s4.setLong(3, time);
            s4.setString(4, "False");
            s4.execute();
            s4.close();

            // commit and return
            current.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }
    }

    public static boolean removeShowroomVehicle(Vehicle v, Long price) {
        // remove from showroom
        // add to listing
        try {
            // remove from showroom
            Connection current = ConnectionManager.getCurrentConnection();
            PreparedStatement s = current.prepareStatement(Statement.DELETE_SHOWROOM_VEHICLE);
            s.setString(1, v.getSerialNum());
            s.execute();
            s.close();

            // add price listing
            s = current.prepareStatement(Statement.ADD_LISTING);
            s.setString(1, v.getSerialNum());
            s.setString(2, ServiceManager.getCurrent().getId());
            s.setLong(3, price);
            s.execute();
            s.close();

            // commit & return
            current.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            MenuManager.showMenu(Key.EDGAR1_LOGIN_MENU, Strings.DB_ERROR);
            return false;
        }

    }
}
