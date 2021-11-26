package card;

public class Card {
    private final String num;
    private final String cvv;
    private final int exp_month;
    private final int ex_year;
    private final String zip;
    private final String type;
    private final String first;
    private final String middle;
    private final String last;

    public Card(String num, String cvv, int exp_month, int ex_year, String zip, String type, String first, String middle, String last) {
        this.num = num;
        this.cvv = cvv;
        this.exp_month = exp_month;
        this.ex_year = ex_year;
        this.zip = zip;
        this.type = type;
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    public String getNum() {
        return num;
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

    public String getNumCensored() {
        return "XXXXXXXXXXXX" + num.substring(12, 16);
    }

    public String getCvv() {
        return cvv;
    }

    public int getExp_month() {
        return exp_month;
    }

    public int getEx_year() {
        return ex_year;
    }

    public String getZip() {
        return zip;
    }

    public String getType() {
        return type;
    }
}


