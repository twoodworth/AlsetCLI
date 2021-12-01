package card;

/**
 * Stores data for a credit/debit card
 */
public class Card {
    /**
     * 16-digit Card number
     */
    private final String num;

    /**
     * Card Verification Value
     */
    private final String cvv;

    /**
     * Month that the card expires
     */
    private final int expMonth;

    /**
     * Year that the card expires
     */
    private final int expYear;

    /**
     * Zip code of card
     */
    private final String zip;

    /**
     * Type of card (debit/credit)
     */
    private final String type;

    /**
     * First name on card
     */
    private final String first;

    /**
     * Middle name on card
     */
    private final String middle;

    /**
     * Last name on card
     */
    private final String last;

    /**
     * Constructs a new card.
     *
     * @param num:      card number
     * @param cvv:      card verification value
     * @param expMonth: expiration month
     * @param expYear:  expiration year
     * @param zip:      zip code
     * @param type:     card type
     * @param first:    First name
     * @param middle:   Middle name
     * @param last:     Last name
     */
    public Card(String num, String cvv, int expMonth, int expYear, String zip, String type, String first, String middle, String last) {
        this.num = num;
        this.cvv = cvv;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.zip = zip;
        this.type = type;
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    /**
     * Gets the card number
     *
     * @return card number
     */
    public String getNum() {
        return num;
    }

    /**
     * Gets the first name
     *
     * @return first name
     */
    public String getFirst() {
        return first;
    }

    /**
     * Gets the middle name
     *
     * @return middle name
     */
    public String getMiddle() {
        return middle;
    }

    /**
     * Gets the last name
     *
     * @return last name
     */
    public String getLast() {
        return last;
    }

    /**
     * Returns the card number with the first 12 digits censored as 'X'
     *
     * @return censored card number
     */
    public String getNumCensored() {
        return "XXXXXXXXXXXX" + num.substring(12, 16);
    }

    /**
     * Gets the CVV
     *
     * @return CVV
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * Gets the expiration month
     *
     * @return expiration month
     */
    public int getExpMonth() {
        return expMonth;
    }

    /**
     * Gets the expiration year
     *
     * @return expiration year
     */
    public int getExpYear() {
        return expYear;
    }

    /**
     * Gets the zip code
     *
     * @return zip code
     */
    public String getZip() {
        return zip;
    }

    /**
     * Gets the card type
     *
     * @return type
     */
    public String getType() {
        return type;
    }
}


