import java.util.ArrayList;


public abstract class Character extends GameObject implements Movable {
    private double x;
    private double y;
    private boolean outOfBound = false;
    private int healthPoints;

    public Character() {

    }

    /**
     * Method that checks if character collides with block/bomb
     * @param blocks
     * @param bombs
     */
    @Override
    public abstract void checkCollisions(ArrayList<Block> blocks, ArrayList<Bomb> bombs);

    /**
     * Method that implements character movement
     * @param xMove
     * @param yMove
     */
    @Override
    public abstract void move(double xMove, double yMove);

    /**
     * Method that check if a character is out of bound
     * @param x
     * @param y
     * @return
     */
    public boolean checkOutOfBound(double x, double y) {
        outOfBound = false;
        if (x < Level.getLeftBoundary() || x > Level.getRightBoundary() ||
                y < Level.getTopBoundary() || y > Level.getBottomBoundary()) {
            setOutOfBound();
        }
        return isOutOfBound();
    }

    /**
     * Method that set character's out of bound status to true
     */
    public void setOutOfBound() {
        this.outOfBound = true;
    }

    /**
     * Method that get character's out of bound status
     * @return boolean value corresponding to character's out of bound status
     */
    public boolean isOutOfBound() {
        return outOfBound;
    }

    /**
     * Method to get character's x location
     * @return x location of character
     */
    public double getX() {
        return x;
    }

    /**
     * Method to get character's y location
     * @return y location of character
     */
    public double getY() {
        return y;
    }

    /**
     * Method to get character's current health points
     * @return int value corresponding to character's health points
     */
    public abstract int getHealthPoints();

    /**
     * Method to check whether a character has died (HP hits 0)
     * @param currentHP
     * @return boolean value corresponding to character's state (based on its health points)
     */
    public boolean isDead(int currentHP){
        return currentHP <= 0;
    }
}
