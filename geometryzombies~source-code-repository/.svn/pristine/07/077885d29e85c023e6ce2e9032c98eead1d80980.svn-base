package geometryzombiesmayhem;

import geometryzombiesmayhem.circuitry.Emittable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Body that can be affected by forces.
 * 
 * @author Yan Soares Couto
 * 
 * @version 0.0.2
 */
public abstract class Body implements WarpAffected, Finishable, Emittable, IDable, Serializable {
    /**
     *  Mass of the body
     */
    protected float mass;
    /**
     * Sum of forces on the X-Axis
     */
    protected float Fx;
    /**
     * Sum of forces on the Y-Axis
     */
    protected float Fy;
    /**
     * Acceleration on the X-Axis, calculated using <b>Fx</b> and <b>TempFx</b>
     */
    protected float Ax;
    /**
     * Temporary force on the X-Axis, only lasts one tick
     */
    protected float TempFx;
    /**
     * Temporary force on the Y-Axis, only lasts one tick
     */
    protected float TempFy;
    /**
     * Acceleration on the Y-Axis, calculated using <b>Fy</b> and <b>TempFy</b>
     */
    protected float Ay;
    /**
     * Velocity on the X-Axis, changes the position of the Body every tick
     */
    public float Vx;
    /**
     * Velocity on the Y-Axis, changes the position of the Body every tick
     */
    public float Vy;
    /**
     * position
     */
    protected Vector2D position;
    /**
     * The bounciness of the object. (How much it maintains of its Mechanic energy when it hits something) 
     * It is advised that it should be between 0 and 1.
     */
    protected float bounciness;
    /**
     * Value of the maximum velocity for the x axis.
     */
    protected float maxVelX;
    /**
     * Value of the maximum velocity for the y axis.
     */
    protected float maxVelY;
    protected byte checkPriority = 1;
    protected byte managePriority = 1;
    protected static float floorBounciness = .125F,leftWallBounciness = -.5F, rightWallBounciness = -.5f ,roofBounciness= -.5f;
    
//    /**
//     * Value of the friction level.
//     */
//    protected static float friction = 0.1f;
//    
//    protected boolean hasFriction = true;
    
    protected float angle = 0;
    
    protected RotatableRectangle bounds = null;
    
    protected boolean frameBoundsAffected;
    protected int hadBounds = 0;
    
    private static final int[] emptyCheck = new int[0];
    
    protected int[] doNotCheckCollision = emptyCheck;
    
    protected AffineTransform at;
    
    protected boolean neverDown = true;
    
    protected boolean interactible = false;
    
    public boolean boundsAffected = true;
    
    public Bound.Simple currentBound = null;
    
    public float warpV = 1,warpGX = 0,warpGY = 0;
    public float warpVc = 1;
    
    protected int id;
    
    protected boolean hasBounced = false;
    
    private double deltaX= 0,deltaY= 0;
    
    private static final List<Body> bodies = new ArrayList<>();
    private static final Set<Body> toRemove = new HashSet<>();
    
    public final long BID;
    private static long last = 0;
    
    //Static methods
    
    public static List<Body> getBodies() {return Body.bodies;}
    
    public static void register(Body b) {
        b.load();
        synchronized(bodies) {
        bodies.add(b);
        }
    }
        
    public static void remove(Body b) {
        if(b==null) return;
        toRemove.add(b);
        b.interactible = false;
    }
    
    public static void removeAll() {
        synchronized(bodies){
            bodies.clear();
        }
    }
    
    public static synchronized void removeAll(Collection<? extends Body> b) {b.removeAll(b);}
    
    public static void updateBodies(long deltaN) {
        if(!toRemove.isEmpty()) {
        synchronized(bodies) {
                bodies.removeAll(toRemove);
            }
            toRemove.clear();
        }
        synchronized(bodies) {
            for(int i = 0; i < bodies.size(); i++) {
                Body a = bodies.get(i);
                if(i==0) a.update(deltaN);
                if(!a.interactible) continue;
                for(int j = i + 1; j < bodies.size(); j++) {
                    Body b = bodies.get(j);
                    if(i==0) b.update(deltaN);
                    if(!b.interactible) continue;
                    boolean check = true;
                    for(int u = 0; u < Math.max(a.doNotCheckCollision.length, b.doNotCheckCollision.length);u++) {
                        if(u<a.doNotCheckCollision.length && a.doNotCheckCollision[u] == b.id) {
                            check = false;
                            break;
                        }
                        if(u<b.doNotCheckCollision.length && b.doNotCheckCollision[u] == a.id) {
                            check = false;
                            break;
                        }
                    }
                    if(!check) continue;
                    boolean collides;
                    if(b.checkPriority>a.checkPriority) collides = b.checkCollides(a);
                    else collides = a.checkCollides(b);
                    if(collides) {
                        if(a.managePriority >= b.managePriority) {
                            if(!a.manageCollision(b)) b.manageCollision(a);
                        }
                        else {
                            if(!b.manageCollision(a)) a.manageCollision(b);
                        }
                    }
                }
            }
            try{               
                for(Body b: bodies) {
                    b.calculatePosition((long) (deltaN*b.warpV));
                    b.warpVc = b.warpV;
                    b.warpV = 1f;
                    b.checkBoundsCollision();
                }
            }catch(ConcurrentModificationException co) {System.err.println("Slower there, boy.1");}
        }
        if(WarpBox.boxes.isEmpty()) return;
        Paintables.checkSprites(WarpBox.boxes);
    }
    
    
    public static void paintBodies(Graphics2D g2,Component c) {
        try{
            if(!toRemove.isEmpty()) {
                synchronized(bodies) {
                    bodies.removeAll(toRemove);
                }
                toRemove.clear();
            }
            synchronized(bodies) {
                for(Body b : bodies)
                if(b!=null) b.paint(c, g2);
            }
        }
        catch(ConcurrentModificationException co) {System.err.println("Slower there, boy.2");};
    }
        
    /**
     * Creates a new body for Serialization.
     * <b>Not to be used! This is for Serialization ONLY</b>
     */
    public Body() {this.BID = ++last;};
    
    /**
     * Creates a new body with set mass ,initial velocity and position, it is affected by gravity
     * @param mass Mass of the body
     * @param Vx Initial X-Axis velocity
     * @param Vy Initial Y-Axis velocity
     * @param x X-Axis initial position
     * @param y Y-Axis initial position
     */
    public Body(float mass,float x,float y,float Vx,float Vy) {
        this.BID = ++last;
        Fy = Fx = TempFy = TempFx = Ax = Ay = 0;
        this.mass = mass;
        this.Vx = Vx;
        this.Vy = Vy;
        position = new Vector2D(x,y);
        this.maxVelX = Integer.MAX_VALUE;
        this.maxVelY = Integer.MAX_VALUE;
        at = null;
        setBounciness(0.5F);
        frameBoundsAffected = true;
        addYForce(-ZM.convertNewtons(mass*ZM.gravity));
    }
    static double aux = -1/Math.sqrt(3);
    
    public void checkBoundsCollision() {
        if(boundsAffected && currentBound != null)  {
            hasBounced = true;
            if(Vy>0) Vy = 0;
            addTempYForce(Fy);
            double sin = currentBound.sin, cos = currentBound.cos;
            
            deltaX = deltaY = 0;
//            deltaX = sin*(maxY()-minY()); deltaY = (maxY()-minY())*(1 - cos);
            if(sin/cos<aux) {
                deltaX = ((currentBound.sin*(maxY()-minY()))/2) + (maxX()-minX())*(1 - (currentBound.cos/2));
//                System.err.println("using this bitch\n" + sin/cos);
            } //didn't work
            
            setMaxY((float) (currentBound.getY((deltaX + minX())) - 2*deltaY));
            
            this.checkBoundsInteractions();
            
            currentBound = null;
            hadBounds = 10;
            return;
        }
        if(frameBoundsAffected)
            manageBoundsCollision();
        
    }
    
    protected void checkBoundsInteractions() {}
    
    protected void update(long deltaN) {}
    
    public void load() {}
    
    @Override
    public void destroy() {
        Body.remove(this);
        interactible = false;
    }
    
    private void manageBoundsCollision() {
        if(maxX()>=GameFrame.FRAME_WIDTH && !neverDown){
            Vx = -1*Math.abs(Vx)*rightWallBounciness;
            addTempXForce(-Fx);
            setMaxX(GameFrame.FRAME_WIDTH);
        }
        else if(minX()<=0 && !neverDown){
            Vx = Math.abs(Vx)*leftWallBounciness;
            addTempXForce(-Fx);
            setMinX(0);
        }
        if(minY()<=0 && !neverDown){
            Vy = Math.abs(Vy)*roofBounciness;
            addTempYForce(Fy);
            setMinY(0);
        }
        else if(maxY()>=GameFrame.FRAME_HEIGHT){
            Vy = -1*Math.abs(Vy)*floorBounciness;
            addTempYForce(Fy);
            setMaxY(GameFrame.FRAME_HEIGHT);
            hasBounced = true;
        }
    }
    
    private void debugAll() {
        Class c = Body.class;
        Field[] fields = c.getDeclaredFields();
        System.err.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////");
        for(Field field:fields) {
            try {
                System.err.println(field.getName() + " = " + field.get(this));
            } catch (    IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Body.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Calculates the X-Axis Acceleration and the Y-Axis Acceleration based on the forces applied to the Body and applies it to the velocity
     */
    public void calculatePosition(long deltaN) {
        float c = ((float)deltaN)/TimeUnit.MILLISECONDS.toNanos(ZM.milliToTick);
        if(mass!=0F && mass!=-0F) {
            Ax = (Fx + TempFx)/mass;
            Ay = (Fy + TempFy)/mass;
            TempFx = TempFy = 0;
            Vx= Vx + Ax*c;
            Vy= Vy + Ay*c;
        }
        if(Vx>maxVelX) Vx = maxVelX;
        if(Vy>maxVelY) Vy = maxVelY;
        float vvx = (currentBound==null?0:currentBound.Vx);
        position.addLocal((Vx + vvx)*c, Vy*c);
    }
    
    /**
     * 
     * Convenience method for rotating the Player.
     * Returns the angle between the current direction and the given direction.
     * 
     * @see Vector2D
     * 
     * @param direction
     * @param current
     * @param graph
     * @return angle
     */
    public float lookAt(Vector2D direction) {
        if(at == null) 
            at = new AffineTransform();

        Vector2D d = position.distanceVector(direction);
        angle = (float) Math.atan(d.getY() / d.getX());

        if(d.getX() < 0)
            angle += Math.PI;
        
        if(bounds!=null) bounds.setAngle(angle);
        return angle;
    }
    
    public float lookAt(float angle) {
        if(bounds!=null) bounds.setAngle(angle);
        return this.angle = angle;
    }
    
    public void paint(Component c,Graphics2D g2) {
        if(GameFrame.onEditor) {
            g2.setColor(Color.GREEN);
            Shape s = this.getBounds();
            if(s==null) s = this.gBA();
            g2.draw(s);
            g2.setColor(Color.BLACK);
        }
////////////////        
        if(at==null)
            paintThis(c,g2);
        else {
            if(hadBounds>0){
            
                at.setToIdentity();
                at.scale(-1, -1);
                at.translate(-position.getX(), -position.getY());
//                at.translate(-(maxX() - minX()), 0);
//                at.translate(0, -(maxY() - minY()));
                if(angle<Math.PI/2) {
                    at.scale(-1, 1);
                    at.translate((maxX() - minX()), 0);
                    at.rotate(Math.PI-angle, 0, 0);//(maxX() - minX())/2, (maxY() - minY())/2);
                }
                else at.rotate(angle, 0, 0);//(maxX() - minX())/2, (maxY() - minY())/2);
                paintThis(c,g2,at);               
            }
            else {
                at.setToIdentity();
                at.scale(-1, -1);
                at.translate(-position.getX(), -position.getY());
                at.translate(-(maxX() - minX()), 0);
                at.translate(0, -(maxY() - minY()));
                if(angle<Math.PI/2) {
                    at.scale(-1, 1);
                    at.translate(-(maxX() - minX()), 0);
                    at.rotate(Math.PI-angle, (maxX() - minX())/2, (maxY() - minY())/2);
                }
                else at.rotate(angle, (maxX() - minX())/2, (maxY() - minY())/2);
                paintThis(c,g2,at);
        }
        if(hadBounds>0) hadBounds--;
        }
    }
    /**
     * Checks if this body collides with Body b
     * @param b
     * @return 
     */
    protected boolean checkCollides(Body b) {
        RotatableRectangle sa = getBounds(), sb = b.getBounds();
//        System.err.println(sa.getClass().getName() +"\\" + sb.getClass().getName());
        if(b.id == ID_BOUND) return b.checkCollides(this);
        return sb.intersects(sa);
    }
    /**
     * Manages the collision with object b
     * @param b
     * @return if the collision was managed
     */
    protected abstract boolean manageCollision(Body b);
    
    /**
     * Paints the Body in the following component
     * @param c Component
     * @param g Graphics used to paint
     */
    public abstract void paintThis(Component c,Graphics2D g2);
    /**
     * Paints the Body in the following component
     * @param c Component
     * @param g Graphics used to paint
     * @param at How to paint
     */
    public abstract void paintThis(Component c, Graphics2D g2,AffineTransform at);
        
    public abstract float minX();
    public abstract float maxX();
    public abstract float minY();
    public abstract float maxY();
    public abstract void setMaxX(float x);
    public abstract void setMaxY(float y);
    public abstract void setMinX(float x);
    public abstract void setMinY(float y);
    
    public float getWidth() {return maxX() - minX();}
    public float getHeight() {return maxY() - minY();}
    
    public float getAngle() {return angle;}
    
    /**
     * Returns the mass of the body
     * @return mass of the body 
     */
    public float getMass() { return mass; }
    
    /**
     * Adds the force in the X-Axis
     * @param f Force to be added
     */
    public void addXForce(float f) { Fx+=f; }
    
    /**
     * Adds the force in the Y-Axis
     * @param f Force to be added
     */
    public void addYForce(float f) { Fy-=f; } //inverted because positive goes down, instead of up
    
    /**
     * Removes the force in the X-Axis
     * @param f Force to be removed
     */
    public void removeXForce(float f) { Fx-=f; }
    
    /**
     * Removes the force in the Y-Axis
     * @param f Force to be removed
     */
    public void removeYForce(float f) { Fy+=f; }
    
    /**
     * Adds a force to the X-Axis that only lasts one tick
     * @param f Temporary force to  be added
     */
    public void addTempXForce(float f) { TempFx+=f; }
    
    /**
     * Adds a force to the Y-Axis that only lasts one tick
     * @param f Temporary force to  be added
     */
    public void addTempYForce(float f) { TempFy-=f; }
    /**
     * Sets the bounciness to the float given
     * @see bounciness
     * @param f the new bounciness
     * @return itself 
     */
    public Body setBounciness(float f) { bounciness = f; return this; }
    
    /**
     * Sets the position of the body
     * @param position
     */
    public void setPosition(Vector2D position) { this.position = position; }
    
    /**
     * Returns the player position 
     * @return the players position
     */
    public Vector2D getPosition() { return position; }
    
    public RotatableRectangle getBounds() { 
        if(bounds==null) return bounds = new RotatableRectangle(position.getX(), position.getY(), maxX()-minX(), maxY()-minY(),angle); 
        if(Math.abs(angle-bounds.angle)>.01) bounds.setAngle(angle);
        if(bounds.w!=-1 && bounds.h != -1) bounds.setPos(position.getX(), position.getY());
        else bounds.set(position.getX(), position.getY(), maxX()-minX(), maxY()-minY(),angle);
        return bounds;
    }
    
    public RotatableRectangle getRoundedBounds() {
        return getBounds();
    }
    
    public void setMass(float mass) {this.mass = mass;}
    
    public boolean contains(Point position) {return this.getBounds().contains(position);}
    
    public Shape gBA() { return null; }
    
    /**
     * Sets the maximum velocity for the x and y axis.
     * 
     * @param maxVelX
     * @param maxVelY
     * @return this
     */
    public Body setMaxVelocity(float maxVelX, float maxVelY) {
        this.maxVelX = maxVelX;
        this.maxVelY = maxVelY;
        
        return this;
    }
    
    /**
     * Gets the maximum velocity of the x and y axis in a Vector2D format.
     * 
     * @return Vector2D with maximum velocity of the x and y axis.
     */
    public Vector2D getMaxVelocity() {
        return new Vector2D(maxVelX, maxVelY);
    }
    
    @Override
    public float getWarpVelocity() {
        return warpVc;
    }
    

    @Override
    public void setWarpVelocity(float V) {
        warpV = V;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void emmit(Vector2D position) {
        this.setPosition(position);
        Body.register(this);
    }
    
    public abstract Body copy();

    @Override
    public Emittable getCopy() {
        return copy();
    }
    
    public String getClassName() {return this.getClass().getSimpleName();}
    public String getName() {return this.getClassName();}
    
    public void centerOn(Vector2D point) {this.position = point.subtract(this.getWidth()/2, this.getHeight()/2);}
    public void centerOn(float x, float y) {this.position.set(x-(this.getWidth()/2), y-(this.getHeight()/2));}
    
    public Vector2D getCenter() {return this.position.add(this.getWidth()/2, this.getHeight()/2);}
    
    public static final int ID_PLAYER = 100;
    public static final int ID_ITEM = 101;
    public static final int ID_PROJECTILE = 102;
    public static final int ID_ZOMBIE = 103;
    public static final int ID_BOUND = 104;
    public static final int ID_WARP = 105;
    public static final int ID_CIRCUITRY = 106;
    
    private static final long serialVersionUID = -65468654684354L;
}
