package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.Paintables;
import geometryzombiesmayhem.Player;
import geometryzombiesmayhem.SpriteAnimation;
import geometryzombiesmayhem.TimedSentence;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.ZM;
import geometryzombiesmayhem.Zombie;
import geometryzombiesmayhem.deprecated.Animation;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *@deprecated
 * Base class for the Projectile sub-classes. Extend this.
 * 
 * @version 0.0.2
 * @author Renato Lui Geh
 */
public class Projectile extends Body{
    protected float dam = 40;
    private boolean shot = false;
    protected Image bodyImage;
    private ImageObserver io = new ImageObserver() {
        
        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return true;
        }
    };
    
    /**
     * Default Projectile constructor. 
     * Creates a new bullet with an image, initial speed, mass, damage per shot and a parent gun.
     * 
     * @see Vector2D
     * @see Gun
     * 
     * @param shotImage
     * @param speed
     * @param mass 
     * @param dam
     * @param parent
     */
    public Projectile(Image shotImage, float mass, float dam) {
        super(mass, 0, 0,0,0);
        
        this.bodyImage = shotImage;
        
        this.id = Body.ID_PROJECTILE;
    }
    
    /**
     * Use this constructor to use a gun-less projectile (such as a grenade)
     * or just for debugging.
     * 
     * Creates a new projectile with an image, mass and position where it is initially at.
     * 
     * @param shotImage
     * @param mass
     * @param position 
     */
    public Projectile(Image shotImage, float mass, Vector2D position) {
        super(mass, position.getX(), position.getY(), 0, 0);
        
        this.position = position;
        this.bodyImage = shotImage;
        
        this.id = Body.ID_PROJECTILE;
    }
    
    public Projectile(Image shotImage, float mass) {
        super(mass, 0, 0, 0, 0);
        
        this.bodyImage = shotImage;
        
        this.id = Body.ID_PROJECTILE;
    }
    
    /**
     * Shoots the bullet. Vy should normally be 0.
     * @param Vx The X-Axis velocity
     * @param Vy The Y-Axis velocity
     */
    public void shoot(float Vx,float Vy) {
        position.multLocal(Vector2D.ZERO.copy());
        this.Vx = Vx;
        this.Vy = Vy;
        shot = true;
    }
    
    /**
     * Shoots without the need of a parent.
     * 
     * <b>IMPORTANT: Without a parent, you have to manually position it</b>
     * 
     * @param Vx
     * @param Vy 
     */
    public void shootNoGun(float Vx, float Vy) {
        this.Vx = Vx;
        this.Vy = Vy;
        shot = true;
    }
    
    /**
     * Shoots without the need of a parent.
     * 
     * <b>IMPORTANT: Without a parent, you have to manually position it</b>
     * 
     * @param traj
     */
    public void shootNoGun(Vector2D traj) {
        this.Vx = traj.getX();
        this.Vy = traj.getY();
        shot = true;
    }
    
    public boolean setImage(Image shotImage) {
        boolean foundError = false;
        try {
            this.bodyImage = shotImage;
        }
        catch(Exception exc) {
            foundError = true;
            exc.printStackTrace();
        }
        return foundError;
    }
    public Image getImage() {
        return bodyImage;
    }
    
//    public void checkImpacts(Body b) {
//        if(b.id != Body.ID_PLAYER) {
//            if(!b.equals(this)) {
//                if(b.position != null) {
//                        Rectangle2D bodyBounds = new Rectangle2D.Float(b.position.getX(), b.position.getY(),
//                                (b.maxX() - b.minX()), (b.maxY() - b.minY()));
//                        if(this.getBounds().intersects(bodyBounds)) {
//                            b.addTempXForce(this.Fx);
//                            b.addTempYForce(this.Fy);
//                            System.err.println("Ya nailed dat bitch!");
//                            //this.addTempXForce(-Fx);
//                        }
//                }
//            }
//        }
//    }
    
    public boolean intersects(Player enemy) {
        if(getBounds().intersects(enemy.getBounds().getBounds2D())) {
            enemy.damage(dam);
            return true;
        }
    
        else return false;
    }
    
    public boolean intersects(Zombie zomb) {
        if(getBounds().intersects(zomb.getBounds().getBounds2D())) {
            zomb.damage(dam);
            return true;
        }
        else return false;
    }
    
    public boolean setDamage(float dam) {
        boolean foundError = false;
        try {
            this.dam = dam;
        }
        catch(Exception exc) {
            foundError = true;
            exc.printStackTrace();
        }
        return foundError;
    }
    public float getDamage() {
        return dam;
    }
    
    public boolean isShot() {
        return shot;
    }
    
    public Vector2D getSize() {
        return new Vector2D(bodyImage.getWidth(null), bodyImage.getHeight(null));
    }
    
    /**
     * <b>DO NOT USE. DEBUGGING ONLY</b>
     * 
     * <p>Bullets are already moved through physics. Give an initial velocity through the constructor.</p>
     * 
     * @param speed
     * @return position
     */
    public Vector2D move(Vector2D speed) {
        return position.addLocal(speed);
    }
    
    /**
     * DEBUG ONLY!
     * 
     * @param shot 
     */
    public void setShot(boolean shot) {
        this.shot = shot;
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(shot){
            g2.drawImage(bodyImage, (int) position.getX(), (int) position.getY(), c);
        }
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        if(shot)
            g2.drawImage(bodyImage, at, c);
    }

    @Override
    public float minX() {
        return position.getX();
    }

    @Override
    public float maxX() {
        return position.getX() + bodyImage.getWidth(io);
    }

    @Override
    public float minY() {
        return position.getY();
    }

    @Override
    public float maxY() {
        return position.getY() + bodyImage.getHeight(io);
    }

    @Override
    public void setMaxX(float x) {
        position.setX(x - bodyImage.getWidth(io));
    }

    @Override
    public void setMaxY(float y) {
        position.setY(y - bodyImage.getHeight(io));
    }

    @Override
    public void setMinX(float x) {
        position.setX(x);
    }

    @Override
    public void setMinY(float y) {
        position.setY(y);
    }
    
    public static Vector2D findTrajectory(float distance, float angle) {
        float ySpeed = (float)((distance * Math.sin(angle))/6);
        float xSpeed = (float)((distance * Math.cos(angle))/6);
        return new Vector2D(-xSpeed, -ySpeed);
    }
    
    public static Arc2D.Float traceTrajectory(Player p, Vector2D position, float distance, float angle) {
        Vector2D velocity = findTrajectory(distance, angle);
        Arc2D.Float arc;
        
        float theta = (float)Math.toDegrees((float)Math.atan(velocity.getY()/
                                               velocity.getX()));
        float alpha = 90-theta;
        
        
        float h = (float)(-Math.pow(velocity.getY(), 2)/2*ZM.gravity);
        float w = (velocity.getX()*h)/velocity.getY();
        
        float x;
        
        if(w < 0)
            x = position.getX() - Math.abs(w);
        else
            x = position.getX();
        
        float y = position.getY() - Math.abs(h);
        
        //System.err.println("Returning an arc of: " + "[" + w + "], [" + h + "] with an angle of " + theta);
        //System.err.println("x: " + x + " , y: " + y);
        
        arc = new Arc2D.Float(x, y, Math.abs(w), Math.abs(h), alpha, 180 - 2*alpha, Arc2D.OPEN);
        
    
        float aux = (float)(arc.getCenterY() * 2);
        
        arc.y = position.getY() - (Math.abs(h) - Math.abs(aux));
        
        System.err.println("h': " + aux + "\n" + "h: " + h + "\n"
                + "h'': " + (Math.abs(h) - Math.abs(aux)));
        
        return arc;
    }
    
    public static void refreshArc(Shape arc, Vector2D position, float distance, float angle) {
        Vector2D velocity = findTrajectory(distance, angle);
        
        float angularExtent = (float)Math.toDegrees((float)Math.atan(velocity.getY()/
                                               velocity.getX()));
        
        float h = (float)(-Math.pow(velocity.getY(), 2)/2*ZM.gravity);
        float w = (velocity.getX()*h)/velocity.getY();
      
        float x = position.getX() - w;
        float y = position.getY() - h;
        
        System.err.println("Returning an arc of: " + "[" + w + "], [" + h + "] with an angle of " + angularExtent);
        
        arc = new Arc2D.Float(x, y, w, h, 45, 45, Arc2D.OPEN);
    }
    
    public static void correctRotation(Projectile p, Vector2D direction) {
        p.lookAt(direction);
    }
    
    /**
     * This allows easy implementation of unique features regarding
     * the different characteristics of each projectiles animation, such
     * as the animation of a grenade exploding.
     * 
     * @param e 
     * @deprecated
     */
    public void checkUniqueAnimation(Set<Animation> e) {} 
    
    public SpriteAnimation getUniqueAnimation() {return null;}
    public SpriteAnimation getHitAnimation() {return null;}
    public void updateSprite() {};
    
    @Override
    protected boolean manageCollision(Body b) {
        return false;
    }
    
    private static Set<Projectile> projectiles = new HashSet<>();
    
    public static void register(Projectile p) {projectiles.add(p);}
    public static void remove(Projectile p) {projectiles.remove(p);}
    
    public static void manageProjectiles() {
        for(Iterator<Projectile> it = projectiles.iterator();it.hasNext();) {
            Projectile proj = it.next();
            proj.updateSprite();
            SpriteAnimation anim = proj.getUniqueAnimation();
            if(anim != null)
                Paintables.register(anim);
            
            if((int)proj.Vx != 0 || (int)proj.Vy != 0) {
                for(Zombie z : Zombie.getList()) {
                    if(proj.intersects(z)) {                        
                        SpriteAnimation hit = proj.getHitAnimation();
                        if(hit != null)
                            Paintables.register(hit);
                        
//                        Paintables.register(new TimedSentence(z.position.copy(), Integer.toString((int)proj.dam), 0, 0, .2f, 1000, true, .5f));
                        
                        Body.remove(proj);
                        it.remove();
                        
                        break;
                    } else if(/*proj.getPosition().getX() > FRAME_WIDTH || */proj.getPosition().getX() < 0) {
                        Body.remove(proj);
                        it.remove();

                        break;
                    }
                }
            }
        }
    }

    @Override
    public Body copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
