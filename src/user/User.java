package user;

/**
 * Represents a user, and stores data related to the user.
 */
public class User {
    /**
     * email of user
     */
    private final String email;
    /**
     * First name of user
     */
    private String first;
    /**
     * Middle name of user
     */
    private String middle;
    /**
     * Last name of user
     */
    private String last;
    /**
     * password of user
     */
    private String password;

    /**
     * Constructs a new user using their first name, middle name, last name, email, and password
     *
     * @param first:    First name of user
     * @param middle:   Middle name of user
     * @param last:     Last name of user
     * @param email:    Email of user
     * @param password: Password of user
     */
    public User(String first, String middle, String last, String email, String password) {
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.email = email;
        this.password = password;
    }

    /**
     * Sets the user's first name
     *
     * @param first: First name of user
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * Sets the user's middle name
     *
     * @param middle: Middle name of user
     */
    public void setMiddle(String middle) {
        this.middle = middle;
    }

    /**
     * Sets the user's last name
     *
     * @param last: Last name of user
     */
    public void setLast(String last) {
        this.last = last;
    }

    /**
     * Sets the user's password
     *
     * @param password: Password of user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's first name
     *
     * @return first name of user
     */
    public String getFirst() {
        return first;
    }

    /**
     * Returns the user's middle name
     *
     * @return middle name of user
     */
    public String getMiddle() {
        return middle;
    }

    /**
     * Returns the user's last name
     *
     * @return last name of user
     */
    public String getLast() {
        return last;
    }

    /**
     * Returns the user's email
     *
     * @return email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's password
     *
     * @return password of user
     */
    public String getPassword() {
        return password;
    }
}
