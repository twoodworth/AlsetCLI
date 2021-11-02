package user;

public class User {
    private String first;
    private String middle;
    private String last;
    private final String email;
    private String password;

    public User(String first, String middle, String last, String email, String password) {
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.email = email;
        this.password = password;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst() {
        return first;
    }

    public String getMiddle() {
        return middle;
    }

    public String getLast() {
        return last;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
