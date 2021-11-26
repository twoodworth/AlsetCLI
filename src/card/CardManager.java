package card;

public class CardManager {
    private static Card selected = null;

    public static void setSelected(Card card) {
        selected = card;
    }

    public static Card getSelected() {
        return selected;
    }
}
