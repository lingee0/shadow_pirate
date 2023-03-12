import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;


public class Sword extends BuffItem {
    private static final Image SWORD = new Image("res/items/sword.png");
    private static final Image SWORD_ICON = new Image ("res/items/swordIcon.png");
    private static Image currentImage;
    private static boolean pickedUp = false;
    private static boolean drawn = false;
    private static final int EXTRA_DAMAGE = 15;
    private static int x;
    private static int y;
    private static final int swordXLocation = 3;
    private static int swordYLocation = 40;

    public Sword(int initialX, int initialY) {
        super(initialX, initialY);
        x = initialX;
        y = initialY;
        currentImage = SWORD;
    }

    /**
     * Method that perform state update for sword
     * @param sailor
     */
    public void update(Sailor sailor) {
        currentImage.drawFromTopLeft(x, y);
        if (checkCollision(sailor, currentImage, x, y)) {
            sailor.setDamagePoints(EXTRA_DAMAGE);
            pickedUp = true;
        }
    }

    /**
     * Method to render the icon for the sword when it has been picked up
     */
    public void drawIcon() {
        SWORD_ICON.drawFromTopLeft(swordXLocation, swordYLocation);
    }

    /**
     * Method to set sword icon y location
     * @param multiplier
     */
    public static void setSwordYLocation(int multiplier) {
        swordYLocation = (int) ((multiplier - 1) * SWORD_ICON.getHeight() + swordYLocation);
    }

    /**
     * Method to set sword image status
     * @param drawn
     */
    public static void setDrawn(boolean drawn) {
        Sword.drawn = drawn;
    }

    /**
     * Method to check whether the sword image has been drawn
     * @return boolean value corresponding to whether sword image has been drawn
     */
    public static boolean isDrawn() {
        return drawn;
    }

    /**
     * Method to check whether sword has been picked up
     * @return boolean value corresponding to sword picked up status
     */
    public static boolean isPickedUp() {
        return pickedUp;
    }
}
