package vehicle;

import java.util.Objects;

/**
 * Represents a vehicle model
 */
public class Model {
    /**
     * Model of vehicle
     */
    private final int year;

    /**
     * Name of vehicle
     */
    private final String name;

    /**
     * Constructs a new model
     *
     * @param year: model year
     * @param name: model name
     */
    public Model(int year, String name) {
        this.year = year;
        this.name = name;
    }

    /**
     * Returns model name
     *
     * @return model name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns model year
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Checks if this is equal to another object
     *
     * @param o: Object to compare to
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return year == model.year &&
                Objects.equals(name, model.name);
    }

    /**
     * Provides hash code of this
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(year, name);
    }
}
