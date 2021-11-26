package location;

/**
 * Contains address data.
 */
public class Address {
    /**
     * Planet of address
     */
    private final String planet;

    /**
     * Country of address
     */
    private final String country;

    /**
     * state of address
     */
    private final String state;

    /**
     * city of address
     */
    private final String city;

    /**
     * street and street number of address
     */
    private final String street;

    /**
     * zip code of address
     */
    private final String zip;

    /**
     * apartment number of address
     */
    private final String apartment;

    /**
     * Constructs a new address
     *
     * @param planet:    Planet
     * @param country:   Country
     * @param state:     State
     * @param city:      City
     * @param street:    Street and street number
     * @param zip:       Zip code
     * @param apartment: Apartment number, or N/A if not applicable
     */
    public Address(String planet, String country, String state, String city, String street, String zip, String apartment) {
        this.planet = planet;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.apartment = apartment;
    }

    /**
     * Gets the address planet
     *
     * @return planet
     */
    public String getPlanet() {
        return planet;
    }

    /**
     * Gets the address country
     *
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the address state
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the address city
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the address street
     *
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Gets the address zip code
     *
     * @return zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * Gets the address apartment number
     *
     * @return apartment
     */
    public String getApartment() {
        return apartment;
    }
}
