package card;

/**
 * Used for managing credit cards
 */
public class CardManager {
    /**
     * Current card being managed
     */
    private static Card selected = null;

    /**
     * Sets the current card
     *
     * @param card: Card to manage
     */
    public static void setSelected(Card card) {
        selected = card;
    }

    /**
     * Gets the current card
     *
     * @return Current card
     */
    public static Card getSelected() {
        return selected;
    }
}
