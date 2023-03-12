import bagel.util.Rectangle;

import java.util.ArrayList;


public abstract class Enemy extends Character {
    private double startX;
    private double startY;

    public Enemy() {}

    /**
     * Method that implements Enemy movement
     * @param xMove
     * @param yMove
     */
    @Override
    public abstract void move(double xMove, double yMove);

    /**
     * Method that checks if Enemy collides with block/bomb
     * @param blocks
     * @param bombs
     */
    @Override
    public abstract void checkCollisions(ArrayList<Block> blocks, ArrayList<Bomb> bombs);

    /**
     * Method that check if Enemy is being attacked by sailor
     * @param sailor
     */
    public abstract void checkAttack(Sailor sailor);

    /**
     * Method that change Enemy movement direction
     */
    public abstract void changeDirection();

    /**
     * Method that return ready to attack status of Enemy
     * @return boolean value indicating Enemy's attack state
     */
    public abstract boolean isReadyToAttack();

    /**
     * Method that activates attack cool down for enemy
     */
    public abstract void setAttackCoolDown();

    /**
     * Method to get the center x point of Enemy's image
     * @return Center x location of enemy
     */
    public abstract double getPirateXCenter();

    /**
     * Method to get the center y point of Enemy's image
     * @return Center y location of enemy
     */
    public abstract double getPirateYCenter();

    /**
     * Method to generate attack zone for enemy
     */
    public abstract void createAttackZone();

    /**
     * Method to get the Bounding Box of Enemy's Attack Zone
     * @return Bounding Box of Enemy's attack area
     */
    public abstract Rectangle getPirateAttackArea();

    /**
     * Method that renders the current health as a percentage on screen
     */
    public abstract void renderPirateHealthPoints();

}


