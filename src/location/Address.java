package location;

public class Address {
    private String planet;
    private String country;
    private String state;
    private String city;
    private String street;
    private String zip;
    private String apartment;

    public Address(String planet, String country, String state, String city, String street, String zip, String apartment) {
        this.planet = planet;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.apartment = apartment;
    }

    public String getPlanet() {
        return planet;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZip() {
        return zip;
    }

    public String getApartment() {
        return apartment;
    }
}
