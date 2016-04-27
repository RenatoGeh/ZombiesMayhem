package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.Sprite;
import geometryzombiesmayhem.SpriteAnimation;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.deprecated.Projectile;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

@Deprecated
public class Laser extends Projectile {    
    private Sprite sprite;
    private boolean hasSprite = false;
       
    public Laser(Image shotImage, Vector2D position) {
        this(shotImage, 50, position);
    }
    
    public Laser(Image shotImage, float damage, Vector2D position) {
        super(shotImage, 0, position);
        
        this.setBounciness(0);
        
        this.setDamage(damage);
        
        this.neverDown = false;
        this.frameBoundsAffected = false;
    }
    
    public Laser(Sprite sprite, float damage, Vector2D position) {
        this(sprite.getCurrentFrame(), damage, position);
        
        this.sprite = sprite;
        
        hasSprite = true;
    }
    
    public Laser(float damage, Vector2D position) {
        this(DEFAULT_LASER_SPRITE, damage, position);
    }
    
    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        
        if(hasSprite) {
            bodyImage = sprite.getCurrentFrame();
            sprite.refreshPosition(this.position);
        }
        
        g2.drawImage(bodyImage, at, c);
    }
    
    @Override
    public void updateSprite() {
        sprite.setSpeed(GameFrame.velocity);
    }
    
    @Override
    public SpriteAnimation getHitAnimation() {
        SpriteAnimation hitAnim = new SpriteAnimation("resources/animations/projectiles/hit_laser.png", 
            this.position, 44, 55, 0, 0, 100, 8, 1, 1, 0);
        
//        hitAnim.refreshPosition(this.position.add(
//                (float)(this.getBounds().getWidth() - hitAnim.frameWidth), 
//                (float)(this.getBounds().getHeight() - hitAnim.frameHeight)));
        
//        hitAnim.refreshPosition(this.position.subtract(
//                hitAnim.frameLength, 
//                hitAnim.frameHeight/4));
        
        return hitAnim;
    }
    
    private static final Sprite DEFAULT_LASER_SPRITE = new Sprite("resources/items/ammo/laser_fx.png",
                            Vector2D.ZERO.copy(), 33, 15, 0, 0, 50, 10, 1);
}