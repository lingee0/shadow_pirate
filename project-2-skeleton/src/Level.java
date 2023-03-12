import bagel.*;
import java.util.ArrayList;

/**
 * The abstract class Level holds the common attributes and methods used by the two levels
 * of the game.
 */
public abstract class Level {
    private final int fontSize = 55; // Font size used for the game.
    private final Font FONT = new Font("res/wheaton.otf", fontSize); // Holds the font used for the game.
    private final String START_MESSAGE = "PRESS SPACE TO START";
    private final String ATTACK_MESSAGE = "PRESS S TO ATTACK";
    private final static String GAME_OVER_MESSAGE = "GAME OVER";
    private final static int FONT_Y_POS = 402;
    private final static int INSTRUCTION_OFFSET = 70;
    protected static boolean level0Cleared = false; // Indicates if the first level has been completed
    protected boolean levelStarted = false; // Indicates if the level is started.
    protected boolean levelOver = false; // Indicates if the level is lost.
    protected boolean levelWon = false; // Indicates if the level is won.

    protected Sailor sailor;
    protected BlackBeard blackBeard;
    protected ArrayList<Block> blocks = new ArrayList<>();
    protected ArrayList<Bomb> bombs = new ArrayList<>();
    protected ArrayList<Bomb> explodedBombs = new ArrayList<>();
    protected ArrayList<Pirate> pirates = new ArrayList<>();
    protected ArrayList<Pirate> deadPirates = new ArrayList<>();
    protected ArrayList<Projectile> bullets = new ArrayList<>();
    protected ArrayList<Projectile> hitBullets = new ArrayList<>();
    protected Sword sword;
    protected Elixir elixir;
    protected Potion potion;
    protected Treasure treasure;

    private static Integer TOP_BOUNDARY;
    private static Integer LEFT_BOUNDARY;
    private static Integer BOTTOM_BOUNDARY;
    private static Integer RIGHT_BOUNDARY;


    /**
     * Constructed initializes the attributes with their initial values.
     */
    public Level() {
        setLevelStarted(false);
    }

    /**
     * Read in location for game objects.
     * The code is obtained from project 1 solution, written by Tharun.
     */
    public abstract void readCSV(String fileName);

    /**
     * sets the value of level of level0Cleared to true
     */
    public void setLevel0Cleared() {
        this.level0Cleared = true;
    }

    /**
     * @return level0Cleared indicates if the the first level has been completed
     */
    public static boolean isLevel0Cleared() {
        return level0Cleared;
    }

    /**
     * sets the value of level of TOP_BOUNDARY based on the value in csv file
     */
    public static void setTopBoundary(int topBoundary) {
        TOP_BOUNDARY = topBoundary;
    }

    /**
     * sets the value of level of LEFT_BOUNDARY based on the value in csv file
     */
    public static void setLeftBoundary(int leftBoundary) {
        LEFT_BOUNDARY = leftBoundary;
    }

    /**
     * sets the value of level of BOTTOM_BOUNDARY based on the value in csv file
     */
    public static void setBottomBoundary(int bottomBoundary) {
        BOTTOM_BOUNDARY = bottomBoundary;
    }

    /**
     * sets the value of level of RIGHT_BOUNDARY based on the value in csv file
     */
    public static void setRightBoundary(int rightBoundary) {
        RIGHT_BOUNDARY = rightBoundary;
    }

    /**
     * @return the value of TOP_BOUNDARY
     */
    public static int getTopBoundary() {
        return TOP_BOUNDARY;
    }

    /**
     * @return the value of LEFT_BOUNDARY
     */
    public static int getLeftBoundary() {
        return LEFT_BOUNDARY;
    }

    /**
     * @return the value of BOTTOM_BOUNDARY
     */
    public static int getBottomBoundary() {
        return BOTTOM_BOUNDARY;
    }

    /**
     * @return the value of RIGHT_BOUNDARY
     */
    public static int getRightBoundary() {
        return RIGHT_BOUNDARY;
    }

    /**
     * @return levelStarted indicates if the current level has been started.
     */
    public boolean getLevelStarted() { return levelStarted; }

    /**
     * sets the value of level started to true.
     */
    public void setLevelStarted(boolean state) { levelStarted = state; }

    /**
     * @return levelOver indicates if the current level is lost.
     */
    public boolean getLevelOver() { return levelOver; }

    /**
     * Sets levelWon which indicates level completion.
     */
    public void setLevelWon(boolean state) { levelWon = state; }

    /**
     * @return levelWon indicates whether the current level is won.
     */
    public boolean getLevelWon() { return levelWon; }

    /**
     * sets the value of level over to false. Indicating game over
     */
    public void setLevelOver() { levelOver = true; }


    /**
     * Render start and attack instruction
     */
    public void drawStartScreen() {
        FONT.drawString(START_MESSAGE, (Window.getWidth() / 2.0 - (FONT.getWidth(START_MESSAGE) / 2.0)),
                FONT_Y_POS);
        FONT.drawString(ATTACK_MESSAGE, (Window.getWidth() / 2.0 - (FONT.getWidth(ATTACK_MESSAGE) / 2.0)),
                (FONT_Y_POS + INSTRUCTION_OFFSET));
    }

    /**
     * Check if sailor goes within Enemy Attack Range
     */
    public abstract void checkForPirateAttackRange();

    /**
     * Render game over screen when lost condition is met
     */
    public void drawGameOver() {
        FONT.drawString(GAME_OVER_MESSAGE, (Window.getWidth() / 2.0 - (FONT.getWidth(GAME_OVER_MESSAGE) / 2.0)),
                (FONT_Y_POS));
    }

}