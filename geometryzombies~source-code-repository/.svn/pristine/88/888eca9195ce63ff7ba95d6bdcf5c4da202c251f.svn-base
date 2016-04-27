package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Yan
 */
public class Shot extends Body{
    ProjectileType type;
    boolean getStuck = false, independent = false;
    Sprite s = null;
    float damage;
    
    int[] targets = {Body.ID_ZOMBIE};
    
    /**
     * Serialization constructor.
     */
    public Shot() {super();}
    
    public Shot(ProjectileType type,float Vx,float Vy,Vector2D position) {
        super(type.getProjectileMass(),position.getX(),position.getY(),Vx,Vy);
        this.type = type;
        this.interactible = true;
        this.id = Body.ID_PROJECTILE;
        damage = type.getProjectileDamage();
        getBounds();
        update();
        if(GameFrame.paintArea.z==3) return;
//        GameFrame.paintArea.focusBody = this;
//        GameFrame.velocity = 0.1f;
//        GameFrame.paintArea.z = 3;
    }
    
    public Shot(ProjectileType type, float Vx, float Vy, Vector2D position, int... targets) {
        this(type, Vx, Vy, position);
        this.targets = targets;
    }
    
    protected void update() {
        if(!checkStuff()) return;
        lookAt(position.add(Vx, Vy));
    }
    
    @Override
    public void load() {
        super.load();
        type.shots.add(this);
    }
    
    protected boolean checkStuff() {
        if(getStuck && hasBounced) { 
            Vx = Vy = mass = 0;
            interactible = boundsAffected = frameBoundsAffected = false;
            return false;
        }
        if(position.getX()> GameFrame.translateX + GameFrame.FRAME_WIDTH + 30 || position.getX() < GameFrame.translateX - 30 ) {
            destroy();
            return false;
        }
        return true;
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        update();
        g2.drawImage(type.getShotImage(), (int) position.getX(), (int) position.getY(), c);
    }
    @Override
    public void destroy() {
        super.destroy();
        type.shots.remove(this);
//        GameFrame.paintArea.focusBody = null;
//        GameFrame.velocity = 1f;
//        GameFrame.paintArea.z = 1;
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        update();
        if(s==null)
            type.paint(g2, c, at, position);
        else
            s.paint(g2, c, at, position);
    }
    
    public void requireIndependent() {
        if(independent) return;
        independent = true;
        if(type.shotSprite==null)return;
        s = type.requireIndependent();
    }

    @Override
    public float minX() {
        return position.getX();
    }

    @Override
    public float maxX() {
        return position.getX() + type.getShotWidth();
    }

    @Override
    public float minY() {
        return position.getY();
    }

    @Override
    public float maxY() {
        return position.getY() + type.getShotHeight();
    }

    @Override
    public void setMaxX(float x) {
        position.setX(x - type.getShotWidth());
    }

    @Override
    public void setMaxY(float y) {
        position.setY(y - type.getShotHeight());
    }

    @Override
    public void setMinX(float x) {
        position.setX(x);
    }

    @Override
    public void setMinY(float y) {
        position.setY(y);
    }
    
    public void setDamage(float damage) {this.damage = damage;}
    public float getDamage() {return this.damage;}

    @Override
    protected boolean manageCollision(Body b) {
        for(int t : targets) {            
            if(b.id == t){
                ((Person) b).damage(damage);
                System.err.println("Damage: " + damage);
                if(damage == 35) System.err.println("Critical Hit!");
                destroy();
                Sprite h = type.getHitAnimation(b);
                if(h==null) return true;
                Paintables.register(h);
                return true;
            }
        }
        return false;
    }

    @Override
    public Body copy() {
        return type.getShot(Vx, Vy, position.copy(),targets);
    }
}
