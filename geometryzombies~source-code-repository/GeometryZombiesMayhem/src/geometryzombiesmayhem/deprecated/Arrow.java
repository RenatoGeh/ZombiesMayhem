package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.deprecated.Throwable;
import geometryzombiesmayhem.deprecated.Projectile;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

/**
 * A base arrow class. Extends Projectile and implements Throwable.
 * It has a special method where it bows to where its falling, giving an impression of actually falling.
 * 
 * @author Renato Lui Geh
 * @version 0.0.4
 */
public class Arrow extends Projectile implements Throwable {
    private boolean isStuck = false;
    
    /**
     * Creates an arrow with a bodyImage, mass, position and x and y velocities.
     * <b>IMPORTANT: </b>Velocities do not working with the constructor. Use shoot instead.
     * 
     * @deprecated
     * @param bodyImage
     * @param mass
     * @param position
     * @param Vx
     * @param Vy 
     */
    public Arrow(Image bodyImage, float mass, Vector2D position, float Vx, float Vy) {
        super(bodyImage, mass, position);
        
        this.Vx = Vx;
        this.Vy = Vy;
        
        this.setDamage(30);
    }
    
    /**
     * Creates an arrow with a bodyImage, mass, position and x and y velocities compressed into a Vector2D.
     * <b>IMPORTANT: </b>Velocities do not working with the constructor. Use shoot instead.
     * 
     * @see Vector2D
     * @deprecated
     * @param bodyImage
     * @param mass
     * @param position
     * @param composedVel 
     */
    public Arrow(Image bodyImage, float mass, Vector2D position, Vector2D composedVel) {
        super(bodyImage, mass, position);
        
        this.Vx = composedVel.getX();
        this.Vy = composedVel.getY();
        
        this.setDamage(30);
    }
    
    /**
     * Creates an arrow with a body image, mass and an initial position.
     * Use shoot to shoot.
     * 
     * @param bodyImage
     * @param mass
     * @param position 
     */
    public Arrow(Image bodyImage, float mass, Vector2D position) {
        super(bodyImage, mass, position);
        
        this.setDamage(30);
    }
    
    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        this.traceRotation();
        g2.drawImage(bodyImage, at, c);
    }
    
    public boolean isStuck() {return isStuck;}
    
    @Override
    public void traceRotation() {
        //System.err.println("Through here he passed. - Yoda");
        if(!this.hasBounced)
            this.lookAt(new Vector2D(-this.position.getX() * Vx, -this.position.getY() * Vy));
        
        if(this.hasBounced) 
            this.isStuck = true;
        
        if(isStuck) {
            this.Vx = 0;
            this.Vy = 0;
        }
    }   
}