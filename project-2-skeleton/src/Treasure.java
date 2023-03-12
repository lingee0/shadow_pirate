import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Treasure extends GameObject{
    private static int x;
    private static int y;
    private static Rectangle treasureHitBox;
    private static final Image TREASURE = new Image("res/treasure.png");
    private static boolean treasureReached = false;

    public Treasure (int initialX, int initialY) {
        x = initialX;
        y = initialY;
    }

    /**
     * Method that perform state update for treasure
     * @param sailor
     */
    public void update(Sailor sailor) {
        treasureHitBox = TREASURE.getBoundingBoxAt(new Point(x, y));
        TREASURE.drawFromTopLeft(x, y);
        checkCollision(sailor);
    }

    /**
     * Method to check whether sailor collided with treasure
     * @param sailor
     */
    public static void checkCollision(Sailor sailor) {
        Rectangle sailorBoundingBox = sailor.getSailorHitBox();
        if (sailorBoundingBox.intersects(treasureHitBox)) {
            treasureReached = true;
        }
    }

    /**
     * Method to check whether the treasure has been reached by sailor
     * @return boolean value corresponding to the status whether the treasure
     * has been reached by sailor
     */
    public static boolean isTreasureReached() {
        return treasureReached;
    }
}
