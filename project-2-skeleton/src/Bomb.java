import bagel.Image;

public class Bomb extends Block{
    private final static Image BOMB = new Image("res/bomb.png");
    private final static Image EXPLODE = new Image("res/explosion.png");
    private static Image currentImage;
    private boolean triggered = false;
    private boolean exploded = false;
    private final static int DAMAGE_POINTS = 10;
    private final int x;
    private final int y;
    private int frameCount = 0;
    private static final int BOMB_TIMER = 30;
    private boolean inflictDamage = false;


    public Bomb(int startX, int startY) {
        super(startX, startY);
        x = startX;
        y = startY;
        currentImage = BOMB;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        if (!triggered) {
            BOMB.drawFromTopLeft(x, y);
        } else {
            EXPLODE.drawFromTopLeft(x, y);
        }
        if (triggered) {
            frameCount++;
        }
        if (frameCount == BOMB_TIMER) {
            exploded = true;
        }
    }

    /**
     * Method that check whether bomb has inflicted damage
     * @return boolean value indicating whether bomb has inflicted damage
     */
    public boolean isInflictDamage() {
        return inflictDamage;
    }

    /**
     * Method that set inflict Damage status to true
     */
    public void setInflictDamage() {
        this.inflictDamage = true;
    }

    /**
     * Method that set the value of triggered to true
     */
    public void triggerExplosion() {
        triggered = true;
    }

    /**
     * Method that check whether bomb has exploded
     * @return boolean value indicating whether bomb has exploded
     */
    public boolean isExploded() {
        return exploded;
    }

    /**
     * Method that get bomb damage points
     * @return int value of bomb's damage points
     */
    public int getDAMAGE_POINTS() {
        return DAMAGE_POINTS;
    }
}
