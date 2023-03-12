import bagel.Image;


public class Elixir extends BuffItem {
    private static final Image ELIXIR = new Image("res/items/elixir.png");
    private static final Image ELIXIR_ICON = new Image ("res/items/elixirIcon.png");
    private static Image currentImage;
    private static boolean pickedUp = false;
    private static boolean drawn = false;
    private static final int EXTRA_HP = 35;
    private static int x;
    private static int y;
    private static final int elixirXLocation = 3;
    private static int elixirYLocation = 40;

    public Elixir(int initialX, int initialY) {
        super(initialX, initialY);
        x = initialX;
        y = initialY;
        currentImage = ELIXIR;
    }

    /**
     * Method that perform state update for elixir
     * @param sailor
     */
    public void update(Sailor sailor) {
        currentImage.drawFromTopLeft(x, y);
        if (BuffItem.checkCollision(sailor, currentImage, x, y)) {
            sailor.setHealthPoints(EXTRA_HP, true);
            pickedUp = true;
        }
    }

    /**
     * Method to render the icon for the elixir when it has been picked up
     */
    public void drawIcon() {
        ELIXIR_ICON.drawFromTopLeft(elixirXLocation, elixirYLocation);
    }

    /**
     * Method to set elixir icon y location
     * @param multiplier
     */
    public static void setElixirYLocation(int multiplier) {
        elixirYLocation = (int) ((multiplier - 1) * ELIXIR_ICON.getHeight() + elixirYLocation);
    }

    /**
     * Method to set elixir image status
     * @param drawn
     */
    public static void setDrawn(boolean drawn) {
        Elixir.drawn = drawn;
    }

    /**
     * Method to check whether the elixir image has been drawn
     * @return boolean value corresponding to whether elixir image has been drawn
     */
    public static boolean isDrawn() {
        return drawn;
    }

    /**
     * Method to check whether elixir has been picked up
     * @return boolean value corresponding to elixir picked up status
     */
    public static boolean isPickedUp() {
        return pickedUp;
    }
}
