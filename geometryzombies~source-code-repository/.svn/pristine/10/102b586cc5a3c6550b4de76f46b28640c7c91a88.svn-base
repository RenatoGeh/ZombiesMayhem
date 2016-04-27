package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * @deprecated
 * 
 * Use Animation whenever you need to create a Particle Effect or Explosion.
 * Animation is supposed to be a fake-one-use-only-gif-image. 
 * Because .gifs have a cycle, an Animation must have a length to indicate until when the effect stops.
 * 
 * <b>NOTE:</b> Animation DOES NOT cause any in-game effect apart from a visual context. You will have to implement it yourself.
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class Animation {
    /**
     * Length of the animation.
     */
    private int length;
    /**
     * Delay of the animation until triggered. If zero, no delay.
     */
    private int delay = 0;
    /**
     * The actual gif image that is representing the animation.
     */
    private Image gifImage;
    /**
     * Whether the animation has ended of not.
     */
    public boolean hasEnded = false;
    /**
     * The position where the animation is going to be triggered.
     */
    private Vector2D position;
    
    /**
     * The countdown timer for the length and delay of the animation.
     */
    private Timer countdown;
    
    /**
     * Constructor without a delay. 
     * It creates an animation with an animated gif image, position where it's going to be triggered,
     * and a length (integer and in milliseconds), indicating until when the animation has ended.
     * 
     * <b>NOTE:</b> This constructor does not use the delay function. To set it use the alternative constructor.
     * 
     * @param gifImage
     * @param position
     * @param length 
     */
    public Animation(Image gifImage, Vector2D position, int length) {
        this.gifImage = gifImage;
        this.length = length;
        this.position = position;
        
        countdown = new Timer(length, countdownListener);
        countdown.start();
        countdown.setRepeats(false);
    }
    
    /**
     * Constructor with delay.
     * It creates an animation with a given delay (in milliseconds and as an integer),
     * a gif image representing the animation, a position where the action is being triggered and
     * a length (in milliseconds and integer) indicating until when the animation is running.
     * 
     * @param delay
     * @param gifImage
     * @param position
     * @param length 
     */
    public Animation(int delay, Image gifImage, Vector2D position, int length) {
        this.gifImage = gifImage;
        this.length = length;
        this.position = position;
        
        this.delay = delay;
        countdown = new Timer(this.delay, countdownListener);
        countdown.start();
        countdown.setRepeats(false);
    }
    
    /**
     * Paints the animation with the given component (normally the JFrame or JPanel) and a graphics context.
     * It also automatically establishes whether the animation has ended or not and when to paint when a delay is set.
     * 
     * @param c
     * @param g2 
     */
    public void paint(Component c, Graphics2D g2) {
        if(!hasEnded && delay == 0) 
            g2.drawImage(gifImage, (int)position.getX(), (int)position.getY(), c);
    }
    
    /**
     * Countdown listener. Simply indicates when the delay or length has reached its destiny.
     */
    private ActionListener countdownListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.err.println("Action is being performed. Like a boss.");
            
            if(delay != 0) {
                countdown.setDelay(length);
                delay = 0;
            }
            else {
                hasEnded = true;
            }
        }
    };
}