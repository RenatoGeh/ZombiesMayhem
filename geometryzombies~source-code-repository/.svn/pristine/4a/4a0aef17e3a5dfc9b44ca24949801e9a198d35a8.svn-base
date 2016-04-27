/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yan
 */
public abstract class ProjectileType implements java.io.Serializable {
    protected MutableImage shotImage;
    protected Sprite shotSprite;
    List<Shot> shots = new ArrayList<>();
    float V = .1f; //base Velocity
    float distModifier = 0;
    
    public static final ProjectileType ARROW = new Arrow(),LASER = new Laser();
    public static final ProjectileType BULLET = new Bullet(), GOO = new Goo();
    
    /**
     * Serialization constructor.
     */
    public ProjectileType() {};
    
    static ProjectileType[] getTypes() {
        ProjectileType[] types = {ARROW, LASER, BULLET, GOO};
        return types;
    }
    
    public ProjectileType(MutableImage shotImage){
        this.shotImage = shotImage;
        this.shotSprite = null;
    }
    
    public ProjectileType(Sprite shotSprite) {
        this.shotImage = null;
        this.shotSprite = shotSprite;
    }
    
    public void shoot(Vector2D position, Vector2D mouseposition){
        float x = mouseposition.getX() - position.getX();
        float y = mouseposition.getY() - position.getY();
        float Vx = (float) ((float) Math.signum(x) * Math.sqrt((V*V*x*x)/(x*x + y*y)));
        float Vy = (float) ((float) Math.signum(y) * Math.sqrt((V*V*y*y)/(x*x + y*y))); 
        if(distModifier!=0) {
            float dist;
            Vx *= Math.max(distModifier,dist = position.distance(mouseposition));
            Vy *= Math.max(distModifier,dist);
        }
        
        Shot s = getShot(Vx,Vy,position);
        
        Body.register(s);
        if(shots.size()>40) {
            shots.get(0).destroy();
        }
    }
    
    public void shoot(Vector2D position, Vector2D mousePosition, float damage) {shoot(position, mousePosition, damage, null, null);}
    
    public void shoot(Vector2D position, Vector2D mousePosition, float damage, Gun g, Attributes at) {
        float x = mousePosition.getX() - position.getX();
        float y = mousePosition.getY() - position.getY();
        float Vx = (float) ((float) Math.signum(x) * Math.sqrt((V*V*x*x)/(x*x + y*y)));
        float Vy = (float) ((float) Math.signum(y) * Math.sqrt((V*V*y*y)/(x*x + y*y))); 
        if(distModifier!=0) {
            float dist;
            Vx *= Math.max(distModifier,dist = position.distance(mousePosition));
            Vy *= Math.max(distModifier,dist);
        }
        
        Shot s = getShot(Vx,Vy,position);
        s.setDamage(damage);
        
        if(at != null && g != null)
            at.attribute(g, s);
        
        Body.register(s);
        if(shots.size()>40) {
            shots.get(0).destroy();
        }
    }
    
    public void shoot(float Vx, float Vy, Vector2D position, int... targets) {
        Shot s = getShot(Vx, Vy, position, targets);
        Body.register(s);
        
        if(shots.size() > 40)
            shots.get(0).destroy();
    }
    
    public void shoot(Vector2D position, Vector2D mouseposition, int... targets){
        float x = mouseposition.getX() - position.getX();
        float y = mouseposition.getY() - position.getY();
        float Vx = (float) ((float) Math.signum(x) * Math.sqrt((V*V*x*x)/(x*x + y*y)));
        float Vy = (float) ((float) Math.signum(y) * Math.sqrt((V*V*y*y)/(x*x + y*y))); 
        if(distModifier!=0) {
            float dist;
            Vx *= Math.max(distModifier,dist = position.distance(mouseposition));
            Vy *= Math.max(distModifier,dist);
        }
        
        Shot s = getShot(Vx, Vy, position, targets);
        
        Body.register(s);
        if(shots.size()>40) {
            shots.get(0).destroy();
        }
    } 
    
    protected abstract float getProjectileMass();
    
    protected abstract float getProjectileDamage();
    
    protected Shot getShot(float Vx,float Vy,Vector2D position) { return new Shot(this,Vx,Vy,position); }
    
    protected Shot getShot(float Vx,float Vy,Vector2D position, int... targets) { return new Shot(this, Vx, Vy, position, targets); }
    
    public MutableImage getShotImage() { 
        if(shotSprite==null)
            return shotImage;
        else
            return shotSprite.getCurrentFrame();
    }
    
    void paint(Graphics2D g2, Component c, AffineTransform at,Vector2D v2) {
        if(shotSprite==null)
            g2.drawImage(shotImage, at, c);
        else
            shotSprite.paint(g2, c, at, v2);
    }
    
    Sprite requireIndependent() {
        return shotSprite.copy();
    }
    
    public int getShotWidth() {
        if(shotSprite == null)
            return shotImage.getWidth(null);
        else
            return shotSprite.frameWidth;
    }
    
    public int getShotHeight() {
        if(shotSprite == null)
            return shotImage.getHeight(null);
        else
            return shotSprite.frameHeight;
    }
    
    public abstract SpriteAnimation getHitAnimation(Body b);
    
    private static class Bullet extends ProjectileType {
        protected Bullet() {
            super(AssetManager.loadImage("resources/items/ammo/bullet.png"));
            V=50; distModifier = 0;
        }

        @Override
        protected Shot getShot(float Vx, float Vy, Vector2D position) {
            Shot s = super.getShot(Vx, Vy, position);
            s.boundsAffected = s.frameBoundsAffected = false;
            return s;
        }
        
        @Override
        protected float getProjectileMass() {return 0f;}
        @Override
        protected float getProjectileDamage() {return 30;}
        @Override
        public SpriteAnimation getHitAnimation(Body b) {return null;}
    }
    
    private static class Arrow extends ProjectileType {

        protected Arrow() {
            super(AssetManager.loadImage("resources/items/ammo/arrow.png"));
            distModifier = 2f;
        }

        @Override
        protected Shot getShot(float Vx, float Vy, Vector2D position) {
            return new ArrowShot(this,Vx, Vy, position);
        }

        @Override
        protected float getProjectileMass() {
            return 1f;
        }

        @Override
        protected float getProjectileDamage() {
            return 65;
        }

        @Override
        public SpriteAnimation getHitAnimation(Body b) {
            return null;
        }
        
        private static class ArrowShot extends Shot {

            public ArrowShot() {super();}
            
            public ArrowShot(ProjectileType type, float Vx, float Vy, Vector2D position) {
                super(type, Vx, Vy, position);
                this.getStuck = true;
            }
            
            public ArrowShot(ProjectileType type, float Vx, float Vy, Vector2D position, int... targets) {
                super(type, Vx, Vy, position, targets);
                this.getStuck = true;
            }

            @Override
            protected void update() {
                if(!checkStuff()) return;
                this.lookAt(position.subtract(Vx, Vy));
            }
        }
        
    }
    
    private static class Laser extends ProjectileType{
        
        protected Laser() {
            super(new Sprite("resources/items/ammo/laser_fx.png",
                            Vector2D.ZERO.copy(), 33, 15, 0, 0, 50, 10, 1));
            V = 10;
        }

        @Override
        protected float getProjectileMass() {
            return 0f;
        }

        @Override
        protected Shot getShot(float Vx, float Vy, Vector2D position) {
            Shot s = super.getShot(Vx, Vy, position);
            s.boundsAffected = s.frameBoundsAffected = s.neverDown = false;
            return s;
        }

        @Override
        protected float getProjectileDamage() {
            return 50;
        }

        @Override
        public SpriteAnimation getHitAnimation(Body b) {
            return new SpriteAnimation("resources/animations/projectiles/hit_laser.png", 
            b.position, 44, 55, 0, 0, 100, 8, 1, 1, 0);
        }
      
    }
    
    private static class Goo extends ProjectileType {        
        protected Goo() {
            super(AssetManager.loadImage("resources/items/ammo/goo_proj.png"));
            distModifier = 2f;
        }

        @Override
        protected Shot getShot(float Vx, float Vy, Vector2D position) {
            return new GooShot(this,Vx, Vy, position);
        }

        @Override
        protected float getProjectileMass() {
            return 5f;
        }

        @Override
        protected float getProjectileDamage() {
            return 5;
        }

        @Override
        public SpriteAnimation getHitAnimation(Body b) {
            return null;
        }
        
        private static class GooShot extends Shot {
            public GooShot() {super();}
            
            protected GooShot(ProjectileType type, float Vx, float Vy, Vector2D position) {
                super(type, Vx, Vy, position);
                this.getStuck = true;
            }
            
            protected GooShot(ProjectileType type, float Vx, float Vy, Vector2D position, int... targets) {
                super(type, Vx, Vy, position, targets);
                this.getStuck = true;
            }
            
            @Override
            protected void update() {
                if(!checkStuff()) return;
                this.lookAt(position.subtract(Vx, Vy));
            }
        }
    }
    
    private static final long serialVersionUID = 78987654321321L;
}
