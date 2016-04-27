package geometryzombiesmayhem;

import java.util.concurrent.TimeUnit;

/**
 * A <code>SpriteAnimation</code> with an action event.
 * 
 * @author Renato Lui Geh
 */
public class ActionSpriteAnimation extends SpriteAnimation {
    private int delay;
    
    public ActionSpriteAnimation(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, int times, int delay, 
            int actionDelay, int type) {
        super(spritesheet, position, frameWidth, frameHeight, initPosX, 
                initPosY, frameLength, cols, lines, times, delay);
        
        this.delay = ((type==DELAY_RELATIVE)? (int)((delay)/actionDelay) : 
                ((type==DELAY_ABSOLUTE)? actionDelay : delay * frameLength));
        init();
    }
    
    private void init() {
        AwesomeTimer.addAction(delayAction, delay, this.repeats, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void startSprite() {
        frameIndex = 0;
        AwesomeTimer.addAction(frameAction, modifierLength, true, TimeUnit.MILLISECONDS);
        init();
    }
    
    @Override
    public void stop() {AwesomeTimer.remove(frameAction); AwesomeTimer.remove(delayAction);}
    
    /**
     * <p>The actions to be triggered.</p>
     * 
     * <p><b>Override</b> this method either with an anonymous class or
     * creating an extended class.</p>
     */
    public void trigger(){};
    
    private AwesomeAction delayAction = new AwesomeAction() {
        @Override
        public void run() {trigger();}
    };
    
    /**
     * Applies the absolute value in milliseconds to the delay timer.
     */
    public static final int DELAY_ABSOLUTE = 0;
    /**
     * Applies the relative value (total frame time divided by the given value) to the delay timer.
     */
    public static final int DELAY_RELATIVE = 1;
    /**
     * Applies the delay time to the given frame index.
     */
    public static final int DELAY_FRAME = 2;
}