package menu;

/**
 * 
 */
public class MenuManager {

    private static MenuManager instance = null;

    public static MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }
        return instance;
    }

    private MenuManager() {

    }
}
