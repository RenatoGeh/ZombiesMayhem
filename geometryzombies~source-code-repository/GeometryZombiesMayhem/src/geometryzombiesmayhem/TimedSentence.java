package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

/**
 * Sentence with a Timer.
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class TimedSentence extends Sentence {
    private boolean isAnimated = false;
    private float speed = .5f;
    
    public TimedSentence(Vector2D position, String s, float whitespace, float spacing, int delay) {
        super(position, s, whitespace, spacing);
        
        initTimer(delay);
    }
    
    public TimedSentence(Vector2D position, String s, float whitespace, float spacing, float scale, int delay) {
        super(position, s, whitespace, spacing, scale);
        
        initTimer(delay);
    }
    
    public TimedSentence(Vector2D position, String s, float whitespace, float spacing, float scale, int delay, boolean isAnimated, float speed) {
        super(position, s, whitespace, spacing, scale);
        
        initTimer(delay);
        
        this.speed = speed;
        this.isAnimated = isAnimated;
    }
    
    public TimedSentence(Vector2D position, String s, float whitespace, float spacing, float scale, int delay, boolean isAnimated, float speed, Color c) {
        super(position, s, whitespace, spacing, scale, c);
        
        initTimer(delay);
        this.speed = speed;
        this.isAnimated = isAnimated;
    }
    
    private void initTimer(int delay) {
        AwesomeTimer.addAction(delayAction, delay, false, TimeUnit.MILLISECONDS);
    }
    
    public boolean isAnimated() {return isAnimated;}
    
    private void performAnimation() {
        for(Word w : words) 
            for(Letter l : w.getLetters()) 
                l.setPosition(l.getPosition().subtract(0, speed));
        
        this.position.subtractLocal(0, speed);
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        for(Word w : words)
            w.paint(g2, c);
        
        if(isAnimated)
            performAnimation();
    }
    
    private void performAction() {
//        AwesomeTimer.remove(delayAction); NO NEED TO REMOVE IT, AWESOMETIMER DOES IT BY ITSELF
        Paintables.register(new ImageFader().new Sentence(this, .05f, speed));
        isActive = false;
    }
    
    private AwesomeAction delayAction = new AwesomeAction() {
        @Override
        public void run() {
            performAction();
        }
    };
}