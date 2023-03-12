import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class contains method and attributes particular to level 0. It implements pipe generation and background
 * drawing for level 0.
 */
public class Level0 extends Level {
    private final Image BACKGROUND_IMAGE; // Holds the background image for level 0.
    private final int ladderX = 990; // Holds the winning score for the level.
    private final int ladderY = 630; // Holds the maximum lives available in the level.
    private final static String INSTRUCTION_MESSAGE_LEVEL01 = "USE ARROW KEYS TO FIND LADDER";
    private final static String WIN_MESSAGE_LEVEL0 = "LEVEL COMPLETE!";
    private final static int FONT_Y_POS = 402;
    private final static int INSTRUCTION_OFFSET = 70;
    private final int fontSize = 55; // Font size used for the game.
    private final Font FONT = new Font("res/wheaton.otf", fontSize); // Holds the font used for the game.;
    private final static String WORLD_FILE = "res/level0.csv";
    private static int BOTTOM_BOUNDARY;
    private static int TOP_BOUNDARY;
    private static int LEFT_BOUNDARY;
    private static int RIGHT_BOUNDARY;

    /**
     * Constructor initializes the final variables and lives for the level.
     */
    public Level0() {
        this.BACKGROUND_IMAGE = new Image("res/background0.png");
        readCSV(WORLD_FILE);
    }

    /**
     * Update method is called every frame and generates new pipes and updates the state of those pipes.
     */
    public void update(Input input) {
        if (!getLevelStarted()) {
            drawStartScreen();
            drawLevelInstruction();
            sailor.setFullHealthPoints();
        }

        // Checks if the level is in play.
        if (getLevelStarted() && !getLevelOver()) {
            Level.setBottomBoundary(BOTTOM_BOUNDARY);
            Level.setLeftBoundary(LEFT_BOUNDARY);
            Level.setRightBoundary(RIGHT_BOUNDARY);
            Level.setTopBoundary(TOP_BOUNDARY);
            BACKGROUND_IMAGE.drawFromTopLeft(0, 0); // Draws the background image for the level
            for (Block block : blocks) {
                block.update();
            }
            for (Projectile bullet : bullets) {
                bullet.update();
                if (bullet.getHitStatus() || bullet.checkOutOfBound()) {
                    hitBullets.add(bullet);
                }
            }
            for (Projectile hitBullet : hitBullets) {
                bullets.remove(hitBullet);
            }
            sailor.update(input, blocks, bullets, bombs);
            for (Pirate pirate : pirates) {
                pirate.update(blocks, sailor, bombs);
                if (pirate.isDead(pirate.getHealthPoints())) {
                    deadPirates.add(pirate);
                }
            }
            for (Pirate deadPirate : deadPirates) {
                pirates.remove(deadPirate);
            }
            if (sailor.isDead(sailor.getHealthPoints())) {
                setLevelOver();
            }
            for (Projectile bullet : bullets) {
                bullet.drawProjectile();
            }
            checkForPirateAttackRange();
            hasWon();
        }
            // Checks if level is won.
        if (getLevelWon() || input.wasPressed(Keys.W)) {
            setLevelWon(true);
            setLevel0Cleared();
        }
        if (getLevelOver()) {
            drawGameOver();
        }

    }

    public void drawLevelComplete(){
        FONT.drawString(WIN_MESSAGE_LEVEL0, (Window.getWidth()/2.0 -
                (FONT.getWidth(WIN_MESSAGE_LEVEL0)/2.0)), FONT_Y_POS);

    }

    public void drawLevelInstruction(){
        FONT.drawString(INSTRUCTION_MESSAGE_LEVEL01, (Window.getWidth()/2.0 -
                (FONT.getWidth(INSTRUCTION_MESSAGE_LEVEL01)/2.0)), FONT_Y_POS + 2 * INSTRUCTION_OFFSET);

    }

    public void hasWon() {
        if(sailor.getX() + (sailor.getCurrentImage().getWidth()/2.0) >= ladderX &&
                sailor.getY() + (sailor.getCurrentImage().getHeight()/2) >= ladderY) {
            setLevelWon(true);
        }
    }

    @Override
    public void readCSV(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            if ((line = reader.readLine()) != null) {
                String[] sections = line.split(",");
                if (sections[0].equals("Sailor")) {
                    sailor = new Sailor(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
            }

            while ((line = reader.readLine()) != null) {
                String[] sections = line.split(",");
                if (sections[0].equals("Block")) {
                    blocks.add(new Block(Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                } else if (sections[0].equals("Pirate")) {
                    pirates.add(new Pirate(Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                }
                if (sections[0].equals("TopLeft")) {
                    LEFT_BOUNDARY = Integer.parseInt(sections[1]);
                    TOP_BOUNDARY = Integer.parseInt(sections[2]);
                } else if (sections[0].equals("BottomRight")) {
                    RIGHT_BOUNDARY = Integer.parseInt(sections[1]);
                    BOTTOM_BOUNDARY = Integer.parseInt(sections[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void checkForPirateAttackRange() {
        for (Pirate pirate : pirates) {
            if (sailor.getSailorHitBox().intersects(pirate.getPirateAttackArea()) && pirate.isReadyToAttack()) {
                bullets.add(new Projectile(sailor.getSailorXCenter(), sailor.getSailorYCenter(),
                        pirate.getPirateXCenter(), pirate.getPirateYCenter(), false));
                pirate.setAttackCoolDown();
            }
        }
    }
}
