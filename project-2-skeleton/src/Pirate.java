import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.Drawing;
import java.util.ArrayList;
import java.util.Random;


public class Pirate extends Enemy{
    private final static Image PIRATE_RIGHT = new Image("res/pirate/pirateRight.png");
    private final static Image PIRATE_LEFT = new Image("res/pirate/pirateLeft.png");
    private final static Image PIRATE_HIT_LEFT = new Image("res/pirate/pirateHitLeft.png");
    private final static Image PIRATE_HIT_RIGHT = new Image("res/pirate/pirateHitRight.png");

    private final static int MAX_HEALTH_POINTS = 45;

    private double healthX;
    private double healthY;
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static int FONT_SIZE = 15;
    private final static Font FONT = new Font("res/wheaton.otf", FONT_SIZE);
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);

    private int healthPoints;
    private double x;
    private double y;
    private Image currentImage;
    private Rectangle pirateHitBox;
    private Rectangle pirateAttackArea;
    private Drawing attackDrawing = new Drawing();
    private Random rand;
    private Random speed;
    private final static double SPEED_UPPER_BOUND = 0.7;
    private final static double SPEED_LOWER_BOUND = 0.2;
    private final static int DIRECTION = 4;
    private int direction;
    private double moveSize;
    private boolean damaged = false;
    private static final int INVINCIBLE_FRAME = 90;
    private int damagedFrame = 0;
    private boolean facingLeft;
    private static final int ATTACK_RANGE = 100;
    private boolean readyToAttack = true;
    private int attackFrame = 0;
    private static final int ATTACK_COOL_DOWN = 180;

    public Pirate(double startX, double startY){
        this.x = startX;
        this.y = startY;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = PIRATE_RIGHT;
        COLOUR.setBlendColour(GREEN);
        rand = new Random();
        direction = rand.nextInt(DIRECTION);
        speed = new Random();
        moveSize = SPEED_LOWER_BOUND + (SPEED_UPPER_BOUND - SPEED_LOWER_BOUND) * speed.nextDouble();
    }

    /**
     * Method that performs state update
     */
    public void update(ArrayList<Block> blocks, Sailor sailor, ArrayList<Bomb> bombs){
        if (damaged) {
            damagedFrame++;
            if (facingLeft) {
                currentImage = PIRATE_HIT_LEFT;
            } else {
                currentImage = PIRATE_HIT_RIGHT;
            }
        } else {
            if (facingLeft) {
                currentImage = PIRATE_LEFT;
            } else {
                currentImage = PIRATE_RIGHT;
            }
        }
        if (direction == 0){
            move(0, -moveSize);
        } else if (direction == 1){
            move(0, moveSize);
        } else if (direction == 2){
            move(-moveSize,0);
            facingLeft = true;
        } else if (direction == 3){
            move(moveSize,0);
            facingLeft = false;
        }
        if (damagedFrame == INVINCIBLE_FRAME) {
            damaged = false;
            damagedFrame = 0;
        }
        checkCollisions(blocks, bombs);
        checkAttack(sailor);
        createAttackZone();
        if (!isReadyToAttack()) {
            attackFrame++;
        }
        if (attackFrame == ATTACK_COOL_DOWN) {
            readyToAttack = true;
            attackFrame = 0;
        }
        currentImage.drawFromTopLeft(x, y);
        healthX = pirateHitBox.left();
        healthY = pirateHitBox.top() - 6;
        renderPirateHealthPoints();
    }

    /**
     * Method that return ready to attack status of Blackbeard
     * @return boolean value indicating Pirate's attack state
     */
    @Override
    public boolean isReadyToAttack() {
        return readyToAttack;
    }

    /**
     * Method that activates attack cool down for pirate
     */
    @Override
    public void setAttackCoolDown() {
        readyToAttack = false;
    }

    /**
     * Method to get Pirate's health points
     * @return Pirate's healthPoints
     */
    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Method that implements Blackbeard movement
     * @param xMove
     * @param yMove
     */
    @Override
    public void move(double xMove, double yMove){
        x += xMove;
        y += yMove;
    }

    /**
     * Method that check if Blackbeard is being attacked by sailor
     * @param sailor
     */
    @Override
    public void checkAttack(Sailor sailor){
        if (sailor.getSailorHitBox().intersects(pirateHitBox) && sailor.isAttackState() && !damaged) {
               healthPoints = healthPoints - sailor.getDAMAGE_POINTS();
               System.out.println("Sailor inflicts " + sailor.getDAMAGE_POINTS() + " damage points on Pirate. " +
                        "Pirate's current health: " + healthPoints + "/" + MAX_HEALTH_POINTS);
               damaged = true;
            }
    }

    /**
     * Method that checks if Pirate collides with block/bomb
     * @param blocks
     * @param bombs
     */
    @Override
    public void checkCollisions(ArrayList<Block> blocks, ArrayList<Bomb> bombs){
        pirateHitBox = currentImage.getBoundingBoxAt(new Point(x + (currentImage.getWidth()/2.0),
                y + (currentImage.getHeight()/2)));
        for (Block current : blocks) {
            Rectangle blockBox = current.getBoundingBox();
            if (pirateHitBox.intersects(blockBox) || checkOutOfBound(x, y)) {
                changeDirection();
                break;
            }
        }
        for (Bomb current : bombs) {
            Rectangle blockBox = current.getBoundingBox();
            if (pirateHitBox.intersects(blockBox) || checkOutOfBound(x, y)) {
                changeDirection();
                break;
            }
        }
    }

    /**
     * Method to get the center x point of Blackbeard's image
     * @return Center x location of Pirate
     */
    @Override
    public double getPirateXCenter() {
        return (x + (currentImage.getWidth()/2.0));
    }

    /**
     * Method to get the center y point of Blackbeard's image
     * @return Center y location of Pirate
     */
    @Override
    public double getPirateYCenter() {
        return (y + (currentImage.getHeight()/2.0));
    }

    /**
     * Method to generate attack zone for Pirate
     */
    @Override
    public void createAttackZone() {
        pirateAttackArea = new Rectangle((x + (currentImage.getWidth()/2.0)) - (ATTACK_RANGE),
                (y + (currentImage.getHeight()/2)) - (ATTACK_RANGE),ATTACK_RANGE*2, ATTACK_RANGE*2);
       }

    /**
     * Method to get the Bounding Box of Pirate's Attack Zone
     * @return Bounding Box of Pirate's attack area
     */
    @Override
    public Rectangle getPirateAttackArea() {
        return pirateAttackArea;
    }

    /**
     * Method that change Pirate movement direction
     */
    @Override
    public void changeDirection() {
        if (direction==0) {
            direction = 1;
        } else if (direction==1) {
            direction = 0;
        } else if (direction==2) {
            direction = 3;
        } else if (direction==3) {
            direction = 2;
        }
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    @Override
    public void renderPirateHealthPoints(){
        double percentageHP = ((double) healthPoints/MAX_HEALTH_POINTS) * 100;
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        } else {
            COLOUR.setBlendColour(GREEN);
        }
        FONT.drawString(Math.round(percentageHP) + "%", healthX, healthY, COLOUR);
    }

}