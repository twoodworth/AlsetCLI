package product;

import java.util.Objects;

public class Model {
    private final int year;
    private final String name;

    public Model(int year, String name) {
        this.year = year;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return year == model.year &&
                Objects.equals(name, model.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, name);
    }
}
