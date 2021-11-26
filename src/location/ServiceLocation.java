package location;

/**
 * Represents a service location
 */
public class ServiceLocation {
    /**
     * Location ID
     */
    private final String id;

    /**
     * Location name
     */
    private final String name;

    /**
     * Location address
     */
    private final Address address;

    /**
     * Constructs a service location
     *
     * @param id:      ID
     * @param name:    name
     * @param address: address
     */
    public ServiceLocation(String id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    /**
     * Returns the location ID
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the location name
     *
     * @return location name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the location address
     *
     * @return location address
     */
    public Address getAddress() {
        return address;
    }
}
