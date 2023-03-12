import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class contains method and attributes particular to level 0. It implements pipe generation and background
 * drawing for level 0.
 */
public class Level1 extends Level {
    private final Image BACKGROUND_IMAGE; // Holds the background image for level 1.
    private final static String INSTRUCTION_MESSAGE_LEVEL01 = "FIND THE TREASURE";
    private final static String WIN_MESSAGE_LEVEL1 = "CONGRATULATIONS!";
    private final static int FONT_Y_POS = 402;
    private final static int INSTRUCTION_OFFSET = 70;
    private final int fontSize = 55; // Font size used for the game.
    private final Font FONT = new Font("res/wheaton.otf", fontSize); // Holds the font used for the game.;
    private final static String WORLD_FILE = "res/level1.csv";
    private final static int PAUSE_FRAME = 180;
    private int currentPause = 0;

    private static int BOTTOM_BOUNDARY;
    private static int TOP_BOUNDARY;
    private static int LEFT_BOUNDARY;
    private static int RIGHT_BOUNDARY;

    /**
     * Constructor initializes the final variables and lives for the level.
     */
    public Level1() {
        this.BACKGROUND_IMAGE = new Image("res/background1.png");
        readCSV(WORLD_FILE);
    }

    /**
     * Update method is called every frame and generates new pipes and updates the state of those pipes.
     */
    public void update(Input input) {
        if (!getLevelStarted()) {
            setLevelWon(false);
            drawStartScreen();
            drawLevelInstruction();
            sailor.setFullHealthPoints();
        }

        // Checks if the level is in play.
        if (getLevelStarted() && !getLevelOver() && !getLevelWon()) {
            Level.setBottomBoundary(BOTTOM_BOUNDARY);
            Level.setLeftBoundary(LEFT_BOUNDARY);
            Level.setRightBoundary(RIGHT_BOUNDARY);
            Level.setTopBoundary(TOP_BOUNDARY);
            BACKGROUND_IMAGE.drawFromTopLeft(0, 0); // Draws the background image for the level
            for (Bomb bomb : bombs) {
                bomb.update();
                if (bomb.isExploded()) {
                    explodedBombs.add(bomb);
                }
            }
            for (Bomb explodedBomb : explodedBombs) {
                bombs.remove(explodedBomb);
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
            if (!sword.isPickedUp()) {
                sword.update(sailor);
            } else if (!sword.isDrawn()){
                sword.setSwordYLocation(sailor.getItemPickedUp());
                sword.setDrawn(true);
                sword.drawIcon();
            } else {
                sword.drawIcon();
            }
            if (!elixir.isPickedUp()) {
                elixir.update(sailor);
            } else if (!elixir.isDrawn()){
                elixir.setElixirYLocation(sailor.getItemPickedUp());
                elixir.setDrawn(true);
                elixir.drawIcon();
            } else {
                elixir.drawIcon();
            }
            if (!potion.isPickedUp()) {
                potion.update(sailor);
            } else if (!potion.isDrawn()){
                potion.setPotionYLocation(sailor.getItemPickedUp());
                potion.setDrawn(true);
                potion.drawIcon();
            } else {
                potion.drawIcon();
            }
            treasure.update(sailor);
            for (Pirate pirate : pirates) {
                pirate.update(blocks, sailor, bombs);
                if (pirate.isDead(pirate.getHealthPoints())) {
                    deadPirates.add(pirate);
                }
            }
            for (Pirate deadPirate : deadPirates) {
                pirates.remove(deadPirate);
            }
            if (!blackBeard.isDead(blackBeard.getHealthPoints())) {
                blackBeard.update(blocks, sailor, bombs);
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
        if (getLevelWon()) {
            drawLevelComplete();
        }
        if (getLevelOver()) {
            drawGameOver();
        }

    }

    public void drawLevelComplete(){
        FONT.drawString(WIN_MESSAGE_LEVEL1, (Window.getWidth()/2.0 -
                (FONT.getWidth(WIN_MESSAGE_LEVEL1)/2.0)), FONT_Y_POS);

    }

    public void drawLevelInstruction(){
        FONT.drawString(INSTRUCTION_MESSAGE_LEVEL01, (Window.getWidth()/2.0 -
                (FONT.getWidth(INSTRUCTION_MESSAGE_LEVEL01)/2.0)), FONT_Y_POS + 2 * INSTRUCTION_OFFSET);

    }

    public void hasWon() {
        if (treasure.isTreasureReached()) {
            setLevelWon(true);
        }
    }

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
                    bombs.add(new Bomb(Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                } else if (sections[0].equals("Pirate")) {
                    pirates.add(new Pirate(Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                } else if (sections[0].equals("Blackbeard")) {
                    blackBeard = new BlackBeard(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
                if (sections[0].equals("TopLeft")) {
                    LEFT_BOUNDARY = Integer.parseInt(sections[1]);
                    TOP_BOUNDARY = Integer.parseInt(sections[2]);
                } else if (sections[0].equals("BottomRight")) {
                    RIGHT_BOUNDARY = Integer.parseInt(sections[1]);
                    BOTTOM_BOUNDARY = Integer.parseInt(sections[2]);
                }
                if (sections[0].equals("Sword")) {
                    sword = new Sword(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                } else if (sections[0].equals("Elixir")) {
                    elixir = new Elixir(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                } else if (sections[0].equals("Potion")) {
                    potion = new Potion(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                } else if (sections[0].equals("Treasure")) {
                    treasure = new Treasure(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
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
        if (sailor.getSailorHitBox().intersects(blackBeard.getPirateAttackArea()) && blackBeard.isReadyToAttack()) {
            bullets.add(new Projectile(sailor.getSailorXCenter(), sailor.getSailorYCenter(),
                    blackBeard.getPirateXCenter(), blackBeard.getPirateYCenter(), true));
            blackBeard.setAttackCoolDown();
        }
    }

}
