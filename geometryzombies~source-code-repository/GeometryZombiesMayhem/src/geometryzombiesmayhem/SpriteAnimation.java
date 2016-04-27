package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Animation version of <code>Sprite</code>
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class SpriteAnimation extends Sprite {
    private int times = 1;
    private int checkTimes = 0;
    private AffineTransform at;
    
    public SpriteAnimation(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, int times, int delay) {
        
        super(spritesheet, position, frameWidth, frameHeight, initPosX, initPosY,
                frameLength, cols, lines);
        
        this.times = times;
    }
    
    public SpriteAnimation(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, int times, int delay, Vector2D direction) {
        this(spritesheet, position, frameWidth, frameHeight, initPosX, initPosY, 
                frameLength, cols, lines, times, delay);
        
        float angle = lookAt(direction, position);
        
        at = new AffineTransform();
        at.scale(-1, -1);
        at.translate(-position.getX(), -position.getY());
        
        if(angle < Math.PI/2) {
            at.translate(-(frameWidth - position.getX()), 0);
            at.rotate(Math.PI - angle, 
                    (frameWidth - position.getX())/2, (frameHeight - position.getY())/2);
        } else {
            at.rotate(angle, (frameWidth - position.getX())/2, (frameHeight - position.getY())/2);
        }
    }
    
    private float lookAt(Vector2D direction, Vector2D position) {
        Vector2D d = position.distanceVector(direction);
        float angle = (float) Math.atan(d.getY() / d.getX());

        if(d.getX() < 0)
            angle += Math.PI;

        return angle;
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {  
        if(at == null)
            g2.drawImage(this.getCurrentFrame(), posX, posY, c);
        else
            g2.drawImage(this.getCurrentFrame(), at, c);
    }
    
    @Override
    protected void handleFrame() {
        if(frameIndex >= totalFrames-1) {
            if(!repeats) {
                stop();          
                isActive = false;
            } else {
                frameIndex = 0;
            }
        }

        update();
        
        if(frameIndex >= totalFrames-1)
            checkTimes++;
        if(checkTimes >= times) 
            isActive = false;
    }
    
    @Override
    protected void handleReverseFrame() {
        if(frameIndex < 0) {
            if(!repeats) {
                stop();
                isActive = false;
            } else {
                frameIndex = (lines*cols)-1;
            }
        }
        
        reverseUpdate();
        
        if(frameIndex < 0)
            checkTimes++;
        if(checkTimes >= times)
            isActive = false;
    }
}