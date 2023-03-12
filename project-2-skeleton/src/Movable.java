import java.util.ArrayList;

public interface Movable {
    /**
     * Method that implements movement
     * @param xMove
     * @param yMove
     */
    void move(double xMove, double yMove);

    /**
     * Method to check whether the moving object collide with other objects
     * @param blocks
     * @param bombs
     */
    void checkCollisions (ArrayList<Block> blocks, ArrayList<Bomb> bombs);

    /**
     * Method to check whether moving object is out of bound
     * @param x
     * @param y
     * @return
     */
    boolean checkOutOfBound(double x, double y);
}
