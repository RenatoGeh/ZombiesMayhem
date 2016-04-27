package geometryzombiesmayhem;

import java.util.concurrent.TimeUnit;

/**
 * A <code>Sprite</code> with an action event.
 * 
 * @author Renato Lui Geh
 */
public abstract class ActionSprite extends Sprite {
    private double delay;
    private int type;

    public ActionSprite() {super();}
    
    public ActionSprite(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, boolean repeats, int delay, int type) {
        super(spritesheet, position, frameWidth, frameHeight, initPosX, initPosY,
                frameLength, cols, lines, repeats);
        
        this.delay = ((type==DELAY_RELATIVE)? (int)((cols*lines*frameLength)/delay) : 
                (type==DELAY_ABSOLUTE)? delay : frameLength * delay);
        
        this.type = type;
    }
    
    public ActionSprite(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, int delay, int type) {
        this(spritesheet, position, frameWidth, frameHeight, initPosX, initPosY,
                frameLength, cols, lines, true, delay, type);
    }
    
    public ActionSprite(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, boolean repeats, boolean starts, 
            int delay, int type) {
        super(spritesheet, position, frameWidth, frameHeight, initPosX, initPosY,
                frameLength, cols, lines, repeats, starts);
        
        this.delay = ((type==DELAY_RELATIVE)? (int)((cols*lines*frameLength)/delay) : 
                (type==DELAY_ABSOLUTE)? delay : frameLength * delay);
        
        this.type = type;
    }
    
    private void init() {
        if(delayAction==null) 
            delayAction();
        
        AwesomeTimer.addAction(delayAction, (int)delay, this.repeats, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void startSprite() {
        frameIndex = 0;
        isRunning = true;
        AwesomeTimer.addAction(frameAction, modifierLength, true, TimeUnit.MILLISECONDS);
        init();
    }
    
    @Override
    public void stop() {AwesomeTimer.remove(frameAction); AwesomeTimer.remove(delayAction); isRunning = false;}
    
    
    private void delayAction() {
        delayAction = new AwesomeAction() {
        @Override
        public void run() {
            if(frameIndex == 1)
                delayAction.timeToSet = (long)ActionSprite.this.delay * 1000000;
            else {
                long d = (((ActionSprite.this.lines*ActionSprite.this.cols)-frameIndex)*ActionSprite.this.frameLength)*1000000;
                trigger();
                delayAction.timeToSet = d;
            }
        }
    };
    }
    /**
     * <p>The actions to be triggered.</p>
     * 
     * <p><b>Override</b> this method either with an anonymous class or
     * creating an extended class.</p>
     */
    public abstract void trigger();
    
    private AwesomeAction delayAction;
    
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