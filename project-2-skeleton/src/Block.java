import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Block extends GameObject{
    private final static Image BLOCK = new Image("res/block.png");
    private final int x;
    private final int y;

    public Block(int startX, int startY){
        this.x = startX;
        this.y = startY;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        BLOCK.drawFromTopLeft(x, y);
    }

    /**
     * @return the hit box of the block
     */
    public Rectangle getBoundingBox(){
        return BLOCK.getBoundingBoxAt(new Point(x + (BLOCK.getWidth()/2.0),
                y + (BLOCK.getHeight()/2)));
    }

    /**
     * Method to x location of a block
     * @return x location of a block
     */
    public int getX() {
        return x;
    }

    /**
     * Method to y location of a block
     * @return y location of a block
     */
    public int getY() {
        return y;
    }
}