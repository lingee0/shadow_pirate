import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;


public abstract class BuffItem extends GameObject {
    private static int x;
    private static int y;
    private static Rectangle itemHitBox;

    public BuffItem(int initialX, int initialY){
        x = initialX;
        y = initialY;
    }

    /**
     * Method to check if the sailor collided with the buff item
     * @param sailor
     * @param currentImage
     * @param xPosition
     * @param yPosition
     * @return boolean state whether the sailor has collided with the buff item
     */
    public static boolean checkCollision(Sailor sailor, Image currentImage, int xPosition, int yPosition) {
        Rectangle sailorBoundingBox = sailor.getSailorHitBox();
        itemHitBox = currentImage.getBoundingBoxAt(new Point(xPosition + (currentImage.getWidth()/2.0),
                yPosition + (currentImage.getHeight()/2)));
        if (sailorBoundingBox.intersects(itemHitBox)) {
            return true;
        }
        return false;
    }

    /**
     * Method to get the x Location of the item
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Method to get the y location of the item
     * @return y
     */
    public int getY() {
        return y;
    }
}
