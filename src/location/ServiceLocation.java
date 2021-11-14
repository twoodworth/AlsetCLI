package location;

public class ServiceLocation {
    private final String id;
    private final String name;
    private final Address address;

    public ServiceLocation(String id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
