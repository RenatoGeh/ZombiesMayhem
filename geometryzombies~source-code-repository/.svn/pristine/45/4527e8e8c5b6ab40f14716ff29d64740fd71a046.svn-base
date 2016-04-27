package geometryzombiesmayhem;

/**
 * <p>Represents a Crawler Zombie type.</p>
 * 
 * @author Renato Lui Geh
 */
public class Crawler extends Zombie {
    
    public Crawler() {super();}
    
    public Crawler(String name, Vector2D position, Sprite zombieSprite, float mass) {
        super(name, position, zombieSprite, mass);
        
        this.movementsRange = 150;
        this.weaponsRange = 50;
        this.health = 200;
        
        this.randomVelocity *= 2;
    }

    public Crawler(String name, Sprite zombieSprite, float mass) {
        this(name, Vector2D.CENTER.copy() ,zombieSprite, mass);
    }
    
    public Crawler(String name, Vector2D position, Sprite sprite, float mass, boolean independent) {
        this(name, position, sprite, mass);
    }
    
    @Override
    public void attack() {
        if(Player.getDefaultPlayer().getPosition().distanceSqr(this.position) < 50*50)
            Player.getDefaultPlayer().damage(20);

        Sprite explosion = this.getExplosionAnimation();
        if(explosion != null) {
            explosion.centerOn(this.getCenter());
            Paintables.register(explosion);
        }
        
        this.kill();
        
        AudioManager.generateLine("crawler_explosion", "resources/sounds/fx/ambient/explosions/1.wav", false);
    }
    
    @Override
    public void update() {
        if(!this.sprite.equals(attackSprite)) {
            if(Vx != 0) {
                if(!this.sprite.equals(DEFAULT_WALK_SPRITE)) {
                    this.sprite.stop();
                    this.sprite = DEFAULT_WALK_SPRITE;
                    this.sprite.startSprite();
                }
            } else {
                if(!this.sprite.equals(DEFAULT_SPRITE)) {
                    this.sprite.stop();
                    this.sprite = DEFAULT_SPRITE;
                    this.sprite.startSprite();
                }
            }
        }
    }
    
    @Override
    public SpriteAnimation getDeathAnimation() {
        return new SpriteAnimation("resources/sprites/zombie/crawler/death.png",
                position, 63, 48, 0, 0, 50, 32, 1, 1, 0);
    }
    
    public SpriteAnimation getExplosionAnimation() {
        return new SpriteAnimation("resources/sprites/zombie/crawler/explosion.png",
                position, 80, 82, 0, 0, 50, 21, 1, 1, 0);
    }
    
    public static Sprite getDefaultIdleSprite() {
        Sprite s = new Sprite("resources/sprites/zombie/crawler/idle.png",
                Vector2D.CENTER.copy(), 49, 28, 0, 0, 80, 13, 1);
        
        return s;
    }
    
    public static Sprite getDefaultWalkSprite() {
        Sprite s = new Sprite("resources/sprites/zombie/crawler/walk.png",
                Vector2D.CENTER.copy(), 55, 28, 0, 0, 80, 12, 1);
        
        return s;
    }

    @Override
    public Body copy() {
        return new Crawler(name,position.copy(),sprite.copy(),mass,independent);
    }
    
    {
        DEFAULT_SPRITE = Crawler.getDefaultIdleSprite();
        DEFAULT_WALK_SPRITE = Crawler.getDefaultWalkSprite();
        attackSprite = new ActionSprite("resources/sprites/zombie/crawler/attack.png",
                Vector2D.CENTER.copy(), 50, 35, 0, 0, 80, 18, 1, false, false, 17, ActionSprite.DELAY_FRAME) {
                    @Override
                    public void trigger() {
                        if(this.isRunning() && this.isActive())
                            attack();
                    }
                };
    }
}