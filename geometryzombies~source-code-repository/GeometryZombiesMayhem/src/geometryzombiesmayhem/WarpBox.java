package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Yan
 */
public class WarpBox extends Body{
    static final Set<WarpBox> boxes = new HashSet<>();
    
    boolean active;
    float velocity, gravityX,gravityY;
    boolean hasVelocity, hasGravity;
    
    /**
     * Serialization constructor.
     */
    protected WarpBox() {super();};
    
    protected WarpBox(RotatableRectangle bound) {
        super(0,bound.getX(),bound.getY(),0,0);
        bounds = bound;
        this.id = Body.ID_WARP;
        this.doNotCheckCollision = new int[1];
        this.doNotCheckCollision[0] = Body.ID_WARP;
        this.managePriority = 3;
        this.interactible = true;
        
        boxes.add(this);
    }
    
    WarpBox(RotatableRectangle bound, float velocity) {
        this(bound);
        this.velocity = velocity;
        this.hasVelocity = true;
        this.active = this.hasGravity = false;
    }
    
    WarpBox(RotatableRectangle bound, float gX, float gY) {
        this(bound);
        gravityX = gX;
        gravityY = gY;
        this.hasGravity = true;
        this.hasVelocity = this.active = false;
    }
    
    WarpBox(RotatableRectangle bound, float v, float gX, float gY) {
        this(bound);
        gravityX = gX;
        gravityY = gY;
        velocity = v;
        this.hasGravity = this.hasVelocity = true;
        this.active = false;
    }
    
    public void activate() {
        this.active = true;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        boxes.remove(this);
    }
    
    @Override
    public RotatableRectangle getBounds() {
        return bounds;
    }

    @Override
    protected boolean manageCollision(Body b) {
        if(!active) return true;
        if(b instanceof Shot) {
            Shot s = (Shot)b;
            if(!s.independent) s.requireIndependent();
        }
        if(hasVelocity) b.warpV *= velocity;
        if(hasGravity) {
            b.addTempXForce(b.mass*gravityX);
            b.addTempYForce(b.Fy);
            b.addTempYForce(-ZM.convertNewtons(b.mass*gravityY));
            
        }
        return true;
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        
    }

    @Override
    public float minX() {
        return bounds.getX();
    }

    @Override
    public float maxX() {
        return (float) bounds.getMaxX();
    }

    @Override
    public float minY() {
        return bounds.getY();
    }

    @Override
    public float maxY() {
        return (float) bounds.getMaxY();
    }

    @Override
    public void setMaxX(float x) {
        bounds.setPos(x - (bounds.getMaxX()-bounds.getMinX()),bounds.getY());
    }

    @Override
    public void setMaxY(float y) {
        bounds.setPos(bounds.getX(), y - (bounds.getMaxY()-bounds.getMinY()));
    }

    @Override
    public void setMinX(float x) {
        bounds.setPos(x, bounds.getY());
    }

    @Override
    public void setMinY(float y) {
        bounds.setPos(bounds.getX(), y);
    }

    @Override
    public Body copy() {
        if(hasVelocity && !hasGravity) return new WarpBox(bounds,velocity);
        if(!hasVelocity && hasGravity) return new WarpBox(bounds,gravityX,gravityY);
        return new WarpBox(bounds.copy(),velocity,gravityX,gravityY);
    }
}
