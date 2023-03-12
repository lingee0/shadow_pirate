import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;


public class Sailor extends Character{
    private final static Image SAILOR_LEFT = new Image("res/sailor/sailorLeft.png");
    private final static Image SAILOR_RIGHT = new Image("res/sailor/sailorRight.png");
    private final static Image SAILOR_LEFT_ATTACK = new Image("res/sailor/sailorHitLeft.png");
    private final static Image SAILOR_RIGHT_ATTACK = new Image("res/sailor/sailorHitRight.png");
    private final static int MOVE_SIZE = 1;
    private static int MAX_HEALTH_POINTS = 100;
    private static int DAMAGE_POINTS = 15;
    private final static int MAX_ATTACK_FRAME = 60;
    private final static int ATTACK_COOL_DOWN = 120;
    private int attackFrame = 0;
    private int coolDownFrame = 0;

    private final static int HEALTH_X = 10;
    private final static int HEALTH_Y = 25;
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static int FONT_SIZE = 30;
    private final static Font FONT = new Font("res/wheaton.otf", FONT_SIZE);
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);

    private static int healthPoints;
    private double oldX;
    private double oldY;
    private double x;
    private double y;
    private Image currentImage;
    private Rectangle sailorHitBox;
    private boolean attackState = false;
    private boolean coolDownState = false;
    private boolean facingLeft = false;
    private static int itemPickedUp = 0;

    public Sailor(int startX, int startY){
        oldX = startX;
        oldY = startY;
        x = startX;
        y = startY;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = SAILOR_RIGHT;
        COLOUR.setBlendColour(GREEN);
    }

    /**
     * Method that performs state update
     */
    public void update(Input input, ArrayList<Block> blocks, ArrayList<Projectile> bullets, ArrayList<Bomb> bombs){
        if (attackState) {
            if (facingLeft) {
                currentImage = SAILOR_LEFT_ATTACK;
            } else {
                currentImage = SAILOR_RIGHT_ATTACK;
            }
            if (input.isDown(Keys.UP)){
                setOldPoints();
                move(0, -MOVE_SIZE);
            }else if (input.isDown(Keys.DOWN)){
                setOldPoints();
                move(0, MOVE_SIZE);

            }else if (input.isDown(Keys.LEFT)){
                setOldPoints();
                move(-MOVE_SIZE,0);
                facingLeft = true;

            }else if (input.isDown(Keys.RIGHT)){
                setOldPoints();
                move(MOVE_SIZE,0);
                currentImage = SAILOR_RIGHT_ATTACK;
                facingLeft = false;
            }
            attackFrame++;
        } else {
            if (facingLeft) {
                currentImage = SAILOR_LEFT;
            } else {
                currentImage = SAILOR_RIGHT;
            }
            if (input.isDown(Keys.UP)){
                setOldPoints();
                move(0, -MOVE_SIZE);
            }else if (input.isDown(Keys.DOWN)){
                setOldPoints();
                move(0, MOVE_SIZE);

            }else if (input.isDown(Keys.LEFT)){
                setOldPoints();
                move(-MOVE_SIZE,0);
                currentImage = SAILOR_LEFT;
                facingLeft = true;

            }else if (input.isDown(Keys.RIGHT)){
                setOldPoints();
                move(MOVE_SIZE,0);
                currentImage = SAILOR_RIGHT;
                facingLeft = false;
            }
        }
        if (!coolDownState) {
            if (input.wasPressed(Keys.S)) {
                attackState = true;
            }
        } else {
            if (coolDownFrame == ATTACK_COOL_DOWN) {
                coolDownState = false;
                coolDownFrame = 0;
            } else {
                coolDownFrame++;
            }
        }
        if (attackFrame == MAX_ATTACK_FRAME) {
            coolDownState = true;
            attackState = false;
            attackFrame = 0;
        }

        checkCollisions(blocks, bombs);
        checkShot(bullets);
        currentImage.drawFromTopLeft(x,y);
        renderHealthPoints();
    }

    /**
     * Method that checks for collisions between sailor and blocks/bomb
     * @param blocks
     */
    @Override
    public void checkCollisions(ArrayList<Block> blocks, ArrayList<Bomb> bombs){
        // check collisions and print log
        sailorHitBox = currentImage.getBoundingBoxAt(new Point(x + (currentImage.getWidth()/2.0),
                y + (currentImage.getHeight()/2)));
        if (!Level.isLevel0Cleared()) {
            for (Block current : blocks) {
                Rectangle blockBox = current.getBoundingBox();
                if (sailorHitBox.intersects(blockBox) || checkOutOfBound(x, y)) {
                    moveBack();
                }
            }
        } else {
            for (Bomb current : bombs) {
                Rectangle blockBox = current.getBoundingBox();
                if (sailorHitBox.intersects(blockBox)) {
                    current.triggerExplosion();
                    moveBack();
                    if (!current.isInflictDamage()) {
                        healthPoints = healthPoints - current.getDAMAGE_POINTS();
                        System.out.println("Bomb inflicts " + current.getDAMAGE_POINTS() +
                                " damage points on Sailor. " + "Sailor's current health: " + healthPoints + "/" +
                                MAX_HEALTH_POINTS);
                        current.setInflictDamage();
                    }
                }
                if (checkOutOfBound(x,y)) {
                    moveBack();;
                }
            }
        }
    }

    /**
     * Method that checks for collisions between sailor and projectiles
     * @param projectiles
     */
    private void checkShot(ArrayList<Projectile> projectiles){
        sailorHitBox = currentImage.getBoundingBoxAt(new Point(x + (currentImage.getWidth()/2.0),
                y + (currentImage.getHeight()/2)));
        for (Projectile current : projectiles) {
            Rectangle bulletBox = current.getBulletHitBox();
            if (sailorHitBox.intersects(bulletBox)) {
                healthPoints = healthPoints - current.getPIRATE_BULLET_DAMAGE();
                if (!current.isBlackbeardProjectile()) {
                    System.out.println("Pirate inflicts " + current.getPIRATE_BULLET_DAMAGE() +
                            " damage points on Sailor. " + "Sailor's current health: " + healthPoints + "/" +
                            MAX_HEALTH_POINTS);
                } else {
                    System.out.println("Blackbeard inflicts " + current.getPIRATE_BULLET_DAMAGE() +
                            " damage points on Sailor. " + "Sailor's current health: " + healthPoints + "/" +
                            MAX_HEALTH_POINTS);
                }
                current.setHitStatus();
            }
        }
    }

    /**
     * Method that checks if sailor pickup item that manipulate its damage points
     * @param addition
     */
    public static void setDamagePoints(int addition) {
        DAMAGE_POINTS = addition + DAMAGE_POINTS;
        itemPickedUp += 1;
        System.out.println("Sailor finds Sword. Sailor's damage points increased to " + DAMAGE_POINTS);
    }

    /**
     * Method that checks if sailor pickup item that manipulate its health points
     * @param addition
     * @param max
     */
    public static void setHealthPoints(int addition, boolean max) {
        itemPickedUp += 1;
        if (max) {
            MAX_HEALTH_POINTS = MAX_HEALTH_POINTS + addition;
            setFullHealthPoints();
            System.out.println("Sailor finds Elixir. Sailor's current health " + healthPoints + "/" +
                    MAX_HEALTH_POINTS);
        } else {
            if (healthPoints + addition > MAX_HEALTH_POINTS) {
                setFullHealthPoints();
            } else {
                healthPoints = healthPoints + addition;
            }
            System.out.println("Sailor finds Potion. Sailor's current health " + healthPoints + "/" +
                    MAX_HEALTH_POINTS);
        }
    }

    /**
     * Method to check how many item sailor has picked up
     * @return int corresponding to the number of item has been picked up
     */
    public static int getItemPickedUp() {
        return itemPickedUp;
    }

    /**
     * Method to get sailor's hit box
     * @return Rectangle of sailor's hit box
     */
    public Rectangle getSailorHitBox() {
        return sailorHitBox;
    }

    /**
     * Method to get sailor's current attack state
     * @return boolean value corresponding to sailor's attack state
     */
    public boolean isAttackState() {
        return attackState;
    }

    /**
     * Method that stores the old coordinates of the sailor
     */
    private void setOldPoints(){
        oldX = x;
        oldY = y;
    }

    /**
     * Method that moves the sailor back to its previous position
     */
    private void moveBack(){
        this.x = oldX;
        this.y = oldY;
    }

    /**
     * Method to get sailor's current health points
     * @return int value corresponding to sailor's current health points
     */
    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Method to get the center x point of sailor's image
     * @return Center x location of sailor
     */
    public double getSailorXCenter() {
        return (x + (currentImage.getWidth()/2.0));
    }

    /**
     * Method to get the center y point of sailor's image
     * @return Center y location of sailor
     */
    public double getSailorYCenter() {
        return (y + (currentImage.getHeight()/2.0));
    }

    /**
     * Method to get sailor's current image
     * @return current image of sailor
     */
    public Image getCurrentImage() {
        return currentImage;
    }

    /**
     * Method to get the sailor's x location
     * @return  x location of sailor
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Method to get the sailor's y location
     * @return  y location of sailor
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    private void renderHealthPoints(){
        double percentageHP = ((double) healthPoints/MAX_HEALTH_POINTS) * 100;
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        } else {
            COLOUR.setBlendColour(GREEN);
        }
        FONT.drawString(Math.round(percentageHP) + "%", HEALTH_X, HEALTH_Y, COLOUR);
    }

    /**
     * Method to set sailor's health points to max health points
     */
    public static void setFullHealthPoints() {
        healthPoints = MAX_HEALTH_POINTS;
    }

    /**
     * Method that implements Blackbeard movement
     * @param xMove
     * @param yMove
     */
    @Override
    public void move(double xMove, double yMove) {
        x += xMove;
        y += yMove;
    }

    /**
     * Method to get sailor's damage points
     * @return sailor's damage points
     */
    public int getDAMAGE_POINTS() {
        return DAMAGE_POINTS;
    }

}