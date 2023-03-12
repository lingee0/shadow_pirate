import bagel.*;

/**
 * SWEN20003 Project 2, Semester 1, 2022
 *
 * @author Linggar Nareswara Andaru
 */
public class ShadowPirate extends AbstractGame{
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";

    private  Level0 LEVEL0; // Holds the level 0 object
    private  Level1 LEVEL1; // Holds the level 1 object

    private final int LEVEL_UP_FRAMES = 180; // Number of frames level up screen will stay for

    private boolean levelUp;
    private int frameCounter = 0;

    public ShadowPirate(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        this.LEVEL0 = new Level0();
        this.LEVEL1 = new Level1();
    }

    /**
     * Entry point for program
     */
    public static void main(String[] args){
        ShadowPirate game = new ShadowPirate();
        game.run();
    }


    /**
     * Performs a state update. Pressing escape key,
     * allows game to exit.
     */
    @Override
    public void update(Input input){
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if(!levelUp) {
            level0Update(input);
        }
        else if (levelUp) {
            frameCounter++;
            if (frameCounter <= LEVEL_UP_FRAMES) {
                LEVEL0.drawLevelComplete();
            } else {
                level1Update(input);
            }
        }

    }

    /**
     * Performs a state update for LEVEL0. Pressing space key,
     * allows level to start.
     */
    public void level0Update(Input input) {
        LEVEL0.update(input);
        if (input.wasPressed(Keys.SPACE)) {
            LEVEL0.setLevelStarted(true);
        }
        levelUp = LEVEL0.getLevelWon();

    }

    /**
     * Performs a state update for LEVEL1. Pressing space key,
     * allows level to start.
     */
    public void level1Update(Input input) {
        LEVEL1.update(input);
        if (input.wasPressed(Keys.SPACE)) {
            LEVEL1.setLevelStarted(true);
        }
    }

}