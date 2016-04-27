package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.FXGenerator;
import geometryzombiesmayhem.Sprite;
import geometryzombiesmayhem.SpriteAnimation;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.Zombie;
import geometryzombiesmayhem.deprecated.Explosive;
import geometryzombiesmayhem.deprecated.Animation;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.Timer;

@Deprecated
public class Grenade extends Explosive implements Throwable {

    private Timer countdown;
    private boolean hasExpired = false;
    private boolean animExpired = false;
    private int explosionDelay = 100;
    
    private Sprite sprite;
    private boolean hasSprite = false;
    
    public Grenade(Image bodyImage, float mass, Vector2D position, int delay) {
        super(bodyImage, mass, position);
        
        countdown = new Timer(delay, countdownListener);
        countdown.setRepeats(false);
        countdown.start();
        
        this.dam = 100;
        
        explosionRange = new Rectangle2D.Float(position.getX() - 25, position.getY() - 25,
                                                150, 150);
    }
  
    public Grenade(Image bodyImage, float mass, Vector2D position, Vector2D explosionSize, int delay) {
        super(bodyImage, mass, explosionSize, position, 50);
        
        countdown = new Timer(delay, countdownListener);
        countdown.setRepeats(false);
        countdown.start();
        
        this.dam = 100;
        
        explosionRange = new Rectangle2D.Float(position.getX() - explosionSize.getX()/2,  
                                   position.getY() - explosionSize.getY()/2,
                                    explosionSize.getX(), explosionSize.getY());
    }
    
    public Grenade(Sprite sprite, float mass, Vector2D position, Vector2D explosionSize, int delay) {
        this(sprite.getCurrentFrame(), mass, position, explosionSize, delay);
        
        this.sprite = sprite;
        hasSprite = true;
    }
    
    public Grenade(float mass, Vector2D position, int delay) {
        this(DEFAULT_GRENADE_SPRITE.getCurrentFrame(), mass, position, delay);
        
        this.sprite = DEFAULT_GRENADE_SPRITE;
        hasSprite = true;
    }

    @Override
    public boolean intersects(Zombie zomb) {
//        if(explosionRange.intersects(zomb.getBounds()) && hasExploded && !hasExpired) {
//            zomb.damage(getSplashDamage(zomb));
//            System.err.println("Player damages Zombie for " + getSplashDamage(zomb) + " points!");
//            return true;
//        }
//        else
            return false;
    }
    
    @Override
    public void checkUniqueAnimation(Set<Animation> e) {
        if(hasExploded && !hasExpired && !animExpired) {
            FXGenerator.generateAnimation(FXGenerator.TYPE_AMBIENT_EXPLOSION, position.copy().subtract(0, 150), e);
            animExpired = true;
            countdown.setDelay(explosionDelay);
            countdown.start();
        }
    }
    
    @Override
    public SpriteAnimation getUniqueAnimation() {
        return null;
    }
    
    @Override
    public void paint(Component c, Graphics2D g2) {
        if(hasSprite) {
            bodyImage = sprite.getCurrentFrame();
            sprite.refreshPosition(position);
        }
        
        if(!hasExploded)
            g2.drawImage(bodyImage, (int)position.getX(), (int)position.getY(), c);
    }
    
    @Override
    public void traceRotation() {
        if(!hasSprite)
            this.lookAt(position.add(position.getX() + (mass * position.getY()), position.getY() + (mass * position.getY())));
    }   
    
    private ActionListener countdownListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            hasExploded = true;
            
            if(countdown.getDelay() == explosionDelay) 
                hasExpired = true;
        }
    };
    
    private static final Sprite DEFAULT_GRENADE_SPRITE = new Sprite("resources/items/ammo/grenade_fx.png",
            Vector2D.ZERO.copy(), 18, 19, 0, 0, 50, 32, 1);
}