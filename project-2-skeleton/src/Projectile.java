import bagel.*;
import bagel.util.Rectangle;


public class Projectile extends GameObject{
    private static final Image PIRATE_PROJECTILE = new Image("res/pirate/pirateProjectile.png");
    private static final Image BLACKBEARD_PROJECTILE = new Image("res/blackbeard/blackbeardProjectile.png");
    private static final double PIRATE_BULLET_SPEED = 0.4;
    private static final int PIRATE_BULLET_DAMAGE = 10;
    private boolean blackbeardProjectile = false;
    private double rad;
    private  DrawOptions bulletRotation;
    private double xDistance;
    private double yDistance;
    private double bulletPositionX;
    private double bulletPositionY;
    private double xMove;
    private double yMove;
    private Rectangle bulletHitBox;
    private boolean hitStatus = false;

    public Projectile(double sailorLocationX, double sailorLocationY, double pirateLocationX,double pirateLocationY
            , boolean blackbeard) {
        bulletPositionX = pirateLocationX;
        bulletPositionY = pirateLocationY;
        xDistance = sailorLocationX - pirateLocationX;
        yDistance = sailorLocationY - pirateLocationY;
        calculateBulletMovement();
        bulletRotation = new DrawOptions().setRotation(rad);
        if (blackbeard) {
            blackbeardProjectile = true;
        }
    }

    /**
     * Method that perform state update of projectile
     */
    public void update() {
        if (!blackbeardProjectile) {
            move(xMove, yMove);
        } else {
            move(2*xMove, 2*yMove);
        }
        bulletHitBox = new Rectangle(bulletPositionX, bulletPositionY, 1, 1);
    }

    /**
     * Method that render projectile to the screen
     */
    public void drawProjectile() {
        if (!blackbeardProjectile) {
            PIRATE_PROJECTILE.draw(bulletPositionX, bulletPositionY, bulletRotation);
        } else {
            BLACKBEARD_PROJECTILE.draw(bulletPositionX, bulletPositionY, bulletRotation);
        }
    }

    /**
     * Method to calculate projectile trajectory
     */
    private void calculateBulletMovement() {
        rad = Math.atan(yDistance/xDistance);
        double hypotenuse = Math.sqrt((yDistance*yDistance) + (xDistance*xDistance));
        xMove = (xDistance * PIRATE_BULLET_SPEED)/hypotenuse;
        yMove = (yDistance * PIRATE_BULLET_SPEED)/hypotenuse;
    }

    /**
     * Method to retrieve hitStatus (whether a projectile hit sailor)
     * @return hitStatus
     */
    public boolean getHitStatus() {
        return hitStatus;
    }

    /**
     * Method to set projectile hitStatus to true
     */
    public void setHitStatus() {
        this.hitStatus = true;
    }

    /**
     * Method that get a boolean whether the projectile belong to Blackberad
     * @return blackbeardProjectile
     */
    public boolean isBlackbeardProjectile() {
        return blackbeardProjectile;
    }

    /**
     * Method to get projectile damage
     * @return bullet damage
     */
    public int getPIRATE_BULLET_DAMAGE() {
        if (!blackbeardProjectile) {
            return PIRATE_BULLET_DAMAGE;
        } else {
            return 2*PIRATE_BULLET_DAMAGE;
        }
    }

    /**
     * Method that implement projectile movement
     * @param xMove
     * @param yMove
     */
    public void move(double xMove, double yMove){
        bulletPositionX += xMove;
        bulletPositionY += yMove;
    }

    /**
     * Method to get projectile hit box
     * @return bulletHitBox
     */
    public Rectangle getBulletHitBox() {
        return bulletHitBox;
    }

    /**
     * Method to check if projectile out of bound
     * @return boolean value corresponding to projectile state
     */
    public boolean checkOutOfBound() {
        return (bulletPositionY > Level.getBottomBoundary()) || (bulletPositionY < Level.getTopBoundary()) ||
                (bulletPositionX < Level.getLeftBoundary()) || (bulletPositionX > Level.getRightBoundary());
    }

}
