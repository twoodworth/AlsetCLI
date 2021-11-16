package vehicle;

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
}
