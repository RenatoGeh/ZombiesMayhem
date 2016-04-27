package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.FXGenerator;
import geometryzombiesmayhem.Player;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.Zombie;
import geometryzombiesmayhem.deprecated.Projectile;
import geometryzombiesmayhem.deprecated.Animation;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

@Deprecated
public class Explosive extends Projectile {
    
    protected Rectangle2D.Float explosionRange;
    protected boolean hasExploded = false;
    
    public Explosive(Image bodyImage, float mass, Vector2D splashArea, Vector2D position, float dam) {
        super(bodyImage, mass, position);
        
        explosionRange = new Rectangle2D.Float(position.getX() - splashArea.getX()/2, 
                                    position.getY() - splashArea.getY()/2,
                                    splashArea.getX(), splashArea.getY());
        
        this.dam = dam;
    }
    
    public Explosive(Image bodyImage, float mass, Vector2D position, float dam) {
        super(bodyImage, mass, position);
        
        explosionRange = new Rectangle2D.Float(position.getX() - 25,
                                    position.getY() - 25,
                                    50, 50);
        
        this.dam = dam;
    }
    
    public Explosive(Image bodyImage, float mass, Vector2D position) {
        super(bodyImage, mass, position);
        
        explosionRange = new Rectangle2D.Float(position.getX() - 25,
                                    position.getY() - 25,
                                    50, 50);
        
        this.dam = 50;
    }
    
    public void debugExplosionRange(Graphics2D g2) {
        g2.draw(explosionRange);
        g2.fill(explosionRange);
    }
    
    public void refreshExplosionRange() {
        explosionRange.setRect(position.getX() - explosionRange.getWidth()/2, 
                position.getY() - explosionRange.getHeight()/2,
                explosionRange.getWidth(), explosionRange.getHeight());
    }
    
    public float getSplashDamage(Zombie zomb) {
//        return (float)
//                (dam*((explosionRange.getHeight() + explosionRange.getWidth())/
//                (explosionRange.createIntersection(zomb.getBounds()).getWidth() +
//                explosionRange.createIntersection(zomb.getBounds()).getHeight())));
        return 0;
    }
    
    public float getSplashDamage(Player p) {
//        return (float)
//                (dam*((explosionRange.getHeight() + explosionRange.getWidth())/
//                (explosionRange.createIntersection(p.getBounds()).getWidth() +
//                explosionRange.createIntersection(p.getBounds()).getHeight())));
        return 0;
    }
    
    @Override
    public boolean intersects(Zombie zomb) {
//        if(explosionRange.intersects(zomb.getBounds()) && !hasExploded) {
//            zomb.damage(getSplashDamage(zomb));
//            hasExploded = true;
//            return true;
//        }
//        else 
            return false;
    }
    
    @Override
    public void paint(Component c, Graphics2D g2) {
        if(!hasExploded) {
            refreshExplosionRange();
            //debugExplosionRange(g2);
            g2.drawImage(bodyImage, (int)position.getX(), (int)position.getY(), c);
        }
        else {
        }
    }
    
    @Override
    public void checkUniqueAnimation(java.util.Set<Animation> e) {
        if(hasExploded) 
            FXGenerator.generateAnimation(FXGenerator.TYPE_AMBIENT_EXPLOSION, position.copy().subtract(0, 150), e);
    }
    
    public boolean hasExploded() {
        return hasExploded;
    }
    
}