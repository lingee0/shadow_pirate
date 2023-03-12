import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;


public class Potion extends BuffItem {
    private static final Image POTION = new Image("res/items/potion.png");
    private static final Image POTION_ICON = new Image ("res/items/potionIcon.png");
    private static Image currentImage;
    private static boolean pickedUp = false;
    private static boolean drawn = false;
    private static final int EXTRA_HP = 25;
    private static int x;
    private static int y;
    private static final int potionXLocation = 3;
    private static int potionYLocation = 40;

    public Potion(int initialX, int initialY) {
        super(initialX, initialY);
        x = initialX;
        y = initialY;
        currentImage = POTION;
    }

    /**
     * Method that perform state update for potion
     * @param sailor
     */
    public void update(Sailor sailor) {
        currentImage.drawFromTopLeft(x, y);
        if (BuffItem.checkCollision(sailor, currentImage, x, y)) {
            sailor.setHealthPoints(EXTRA_HP, false);
            pickedUp = true;
        }
    }

    /**
     * Method to render the icon for the potion when it has been picked up
     */
    public void drawIcon() {
        POTION_ICON.drawFromTopLeft(potionXLocation, potionYLocation);
    }

    /**
     * Method to set potion icon y location
     * @param multiplier
     */
    public static void setPotionYLocation(int multiplier) {
        potionYLocation = (int) ((multiplier - 1) * POTION_ICON.getHeight() + potionYLocation);
    }

    /**
     * Method to set potion image status
     * @param drawn
     */
    public static void setDrawn(boolean drawn) {
        Potion.drawn = drawn;
    }

    /**
     * Method to check whether the potion image has been drawn
     * @return boolean value corresponding to whether potion image has been drawn
     */
    public static boolean isDrawn() {
        return drawn;
    }

    /**
     * Method to check whether potion has been picked up
     * @return boolean value corresponding to potion picked up status
     */
    public static boolean isPickedUp() {
        return pickedUp;
    }
}
