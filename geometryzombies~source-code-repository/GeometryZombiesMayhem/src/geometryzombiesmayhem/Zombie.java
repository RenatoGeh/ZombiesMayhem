package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for the sub-classes that extend Zombie.
 * 
 * @version 0.0.9
 * @author Renato Lui Geh
 */
public class Zombie extends Person implements ArtificialIntelligence {
    /**
     * Zombie's name.
     */
    protected String name;
    /**
     * zombie's controllability.
     */
    protected boolean controllable = false;
    protected boolean independent = false;
    
    private Rectangle2D.Float healthBar, baseHealthBar;
    private Vector2D labelSize = new Vector2D(ZM.GRAPHICS.getFontMetrics(ZM.GRAPHICS.getFont()).stringWidth("Level: " + this.getLevel()), ZM.GRAPHICS.getFontMetrics(ZM.GRAPHICS.getFont()).getHeight());
    
    private transient java.util.Random rand = ZM.RANDOM;
    public boolean showBars = false;
        
    /**
     * Serialization constructor.
     */
    public Zombie() {super();};
    
    /**
     * Creates a new zombie. Use this with super when extending.
     * 
     * @param name
     * @param position
     * @param zombieSprite
     * @param mass 
     */
    public Zombie(String name, Vector2D position, Sprite zombieSprite, float mass) {
        super(mass, zombieSprite, position.getX(), position.getY(), 100);
        
        this.name = name;
        
        healthBar = new Rectangle2D.Float(position.getX() - (this.getHealth() *.2f), position.getY() - sprite.frameHeight - 6, this.getHealth() * .2f, 4);
        baseHealthBar = new Rectangle2D.Float(position.getX() - (this.getHealth() * .2f), position.getY() - sprite.frameHeight - 6, 100 * .2f, 4);
        
        this.interactible = true;
        this.id = Body.ID_ZOMBIE;
    }
    /**
     * Creates a new Zombie. Use this with super when extending. 
     * This version of the constructor sets a default position.
     * 
     * @param name
     * @param zombieSprite
     * @param mass 
     */
    public Zombie(String name, Sprite zombieSprite, float mass) {
        this(name,Vector2D.CENTER.copy(),zombieSprite,mass);
    }
    
    public Zombie(String name, Vector2D position, Sprite sprite, float mass, boolean independent) {
        this(name, position, sprite, mass);
        
        this.independent = independent;
    }
    

//    /**
//     * Creates an instance of a Zombie by using Stats.
//     * 
//     * <p><u>How to use it:</u></p>
//     * <p>Zombie p1 = new Zombie(AssetManager.loadStats("Resources/Zombie/myZombie.zomb");</p>
//     * <p><b>Keep in mind that you must, for now, have the file myZomb.zomb somewhere inside the class directory.</b></p>
//     * <p>Use of Program Files or AppData will be made possible shortly.</p>
//     * 
//     * @see Stats
//     * 
//     * @param stat 
//     */
//    public Zombie(Stats stat) {
//        this(stat.getName(),stat.getPosition(),stat.getImage(),stat.getMass());
//
//        controllable = stat.getControllability();
//    }
//    
//    /**
//     * Sets the image of the zombie.
//     * Returns true if it has caught an exception.
//     * 
//     * @param zombieImage
//     * @return errorFound
//     */
//    public boolean setImage(Image zombieImage) {
//        boolean errorFound = false;
//        try {
//            this.bodyImage = zombieImage;
//        }
//        catch(Exception exc) {
//            errorFound = true;
//            exc.printStackTrace();
//        }
//        return errorFound;
//    }
//    /**
//     * Gets the Zombie image.
//     * 
//     * @return bodyImage
//     */
//    public Image getImage() {
//        return bodyImage;
//    }
    
    /**
     * Sets the zombie name.
     * Returns true if it has caught an exception.
     * 
     * @param name
     * @return errorFound
     */
    public void setName(String name) {
            this.name = name;
    }
    /**
     * Return the zombie's name.
     * 
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Checks whether the zombie is out of bounds.
     * Return true if zombie is out of bounds.
     * 
     * <p>Note: this method checks whether the zombie is out of the PaintArea's bounds, and not the Sidescrolling bounds.</p>
     * 
     * @return 
     */
    public boolean isOutOfBounds() {
        if(position.getX() > GameFrame.FRAME_WIDTH || position.getY() > GameFrame.FRAME_HEIGHT ||
           position.getX() < 0 || position.getY() < 0) {
            return true;
        }
        else return false;
    }
    
    /**
     * Forces the zombie to jump.
     */
    public void jump() {
        Vy = -20;
    }
    
    /**
     * Moves the zombie. Returns zombie's current position.
     * 
     * <p><b>This is the X-Axis only version. See other variants for more information</b></p>
     * 
     * @param speed
     * @return position
     */
    public Vector2D move(float speed) {
        this.position.addLocal(speed, 0);
        return position;
    }
    /**
     * Moves the zombie. Returns zombie's current position.
     * 
     * <p><b>This is the Vector2D version. See other variants for more information</b></p>
     * 
     * @param speed
     * @return position
     */
    public Vector2D move(Vector2D speed) {
        this.position.addLocal(speed);
        return position;
    }
    /**
     * Moves the zombie. Returns zombie's current position.
     * 
     * <p><b>This is the X and Y axis version. See other variants for more information</b></p>
     * 
     * @param xSpeed
     * @param ySpeed
     * @return position
     */
    public Vector2D move(float xSpeed, float ySpeed) {
        this.position.addLocal(xSpeed, ySpeed);
        return position;
    }
    
    /**
     * Sets the plater Mass.
     * 
     * @see Body
     * 
     * @param mass
     */
    public void setMass(float mass) {this.mass = mass;}
    
    /**
     * Sets the zombie controllability.
     * 
     * @param control
     */
    public void setControllable(boolean control) {this.controllable = control;}
    /**
     * Returns the zombie controllability.
     * 
     * @return 
     */
    public boolean isControllable() {
        return controllable;
    }
    
    public boolean intersects(Player player) {
        if(this.getBounds().intersects(player.getBounds().getBounds2D()))
            return true;
        else return false;
    }
    
    public void setShowBars(boolean showBars) {
        Player.getDefaultPlayer().setEnemyOnSight(showBars);
        this.showBars = showBars;
    }
    
    /**
     * Makes the Zombie follow the assigned Player.
     * 
     * @see Player
     * 
     * @param player
     * @param speed 
     */
    public void follow(Player player, float speed) {
        if(player.getPosition().getX() > position.getX())
            move(speed);
        else if(player.getPosition().getX() < position.getX())
            move(-speed);
    }

    
    public Vector2D getSize() {
        return new Vector2D(sprite.frameWidth, sprite.frameHeight);
    }

    /**
     * Overrides the Body's paint method.
     * 
     * @param c
     * @param g 
     */
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        update();
        
        this.sprite.paint(g2, c, position);
        
        if(showBars) {
            baseHealthBar.setRect((position.getX() - (this.getHealth() * .2f + 10))+(maxX() - minX()), position.getY() - sprite.frameHeight/4, baseHealthBar.getWidth(), baseHealthBar.getHeight());
            g2.setColor(Color.BLACK);
            g2.draw(baseHealthBar);
            g2.fill(baseHealthBar);

            healthBar.setRect((position.getX() - (this.getHealth() * .2f + 10))+(maxX() - minX()), position.getY() - sprite.frameHeight/4, this.getHealth() * .2f, 4);
            g2.setColor(Color.RED);
            g2.draw(healthBar);
            g2.fill(healthBar);
            
            Color defColor = g2.getColor();
            Font defFont = g2.getFont();
            g2.setFont(ZM.GRAPHICS.getFont());
            g2.setColor(Color.CYAN);
            g2.drawString("Level " + this.getLevel(), this.getPosition().getX() + labelSize.getX()/8, this.getPosition().getY() - labelSize.getY());
            
            g2.setFont(g2.getFont().deriveFont(Font.BOLD));
            g2.setColor(g2.getColor().darker().darker());
            g2.drawString(name, this.getPosition().getX() + labelSize.getX()/8, this.getPosition().getY() - 2*labelSize.getY());
            g2.setColor(defColor);
            g2.setFont(defFont);
        }
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        update();
        
        sprite.paint(g2, c, at,position);
        
        if(showBars) {
            baseHealthBar.setRect((position.getX() - (this.getHealth() * .2f + 10))+(maxX() - minX()), position.getY() - sprite.frameHeight/4, baseHealthBar.getWidth(), baseHealthBar.getHeight());
            g2.setColor(Color.BLACK);
            g2.draw(baseHealthBar);
            g2.fill(baseHealthBar);

            healthBar.setRect(position.getX() - (this.getHealth() * .2f + 10)+(maxX() - minX()), position.getY() - sprite.frameHeight/4, this.getHealth() * .2f, 4);
            g2.setColor(Color.RED);
            g2.draw(healthBar);
            g2.fill(healthBar);
            
            Color defColor = g2.getColor();
            Font defFont = g2.getFont();
            g2.setFont(ZM.GRAPHICS.getFont());
            g2.setColor(Color.CYAN);
            g2.drawString("Level " + this.getLevel(), this.getPosition().getX() + labelSize.getX()/8, this.getPosition().getY() - labelSize.getY());
            
            g2.setFont(g2.getFont().deriveFont(Font.BOLD));
            g2.setColor(g2.getColor().darker().darker());
            g2.drawString(name, this.getPosition().getX() + labelSize.getX()/8, this.getPosition().getY() - 2*labelSize.getY());
            g2.setColor(defColor);
            g2.setFont(defFont);
        }
    }
    
    public void update() {}
    
    @Override
    public void kill() {
        this.health = 0;
        Sprite death = this.getDeathAnimation();
        if(death != null) {
            death.centerOn(this.getCenter());
            Paintables.register(death);
        }
        destroy();
    }

    @Override
    public float minX() {
        return position.getX();
    }

    @Override
    public float maxX() {
        return position.getX() + sprite.frameWidth;
    }

    @Override
    public float minY() {
        return position.getY();
    }

    @Override
    public float maxY() {
        return position.getY() + sprite.frameWidth;
    }

    @Override
    public void setMaxX(float x) {
        position.setX(x - sprite.frameWidth);
    }

    @Override
    public void setMaxY(float y) {
        position.setY(y - sprite.frameHeight);
    }

    @Override
    public void setMinX(float x) {
        position.setX(x);
    }

    @Override
    public void setMinY(float y) {
        position.setY(y);
    }
    
    public void followPlayer(Vector2D playerPosition) {
        
        lookAt(new Vector2D(playerPosition.getX(), position.getY()));
        if(playerPosition.getX() > position.getX() + 40f) 
            Vx = randomVelocity;// - playerVx;
        if(playerPosition.getX() < position.getX() - 40f)
            Vx = -randomVelocity;// - playerVx;
    }
    
    protected float randomVelocity = rand.nextFloat() + rand.nextFloat();
    
    public SpriteAnimation getDeathAnimation() {
        SpriteAnimation deathAnim = new SpriteAnimation("resources/animations/death/zombie_death.png",
            position, 47, 47, 0, 0, 50, 12, 1, 1, 0);
        
        return deathAnim;
    }
    
    public static Sprite getDefaultSprite() {
//        Sprite s = new Sprite("resources/sprites/zombie/zombie_man.png",
//            Vector2D.CENTER.copy(), 32, 41, 0, 0, 80, 10, 1);
//        s.resizeImages(Zombie.DEFAULT_WIDTH, Zombie.DEFAULT_HEIGHT);

        Sprite s = new Sprite("resources/sprites/zombie/spritesheet_walk.png",
            Vector2D.CENTER.copy(), 100, 88, 0, 0, 100, 4, 4);
        
        s.flipImages(Sprite.FLIP_HORIZONTAL);
        s.resizeImages(Zombie.DEFAULT_WIDTH, Zombie.DEFAULT_HEIGHT);
        
        return s;
    }
    
    protected Sprite DEFAULT_SPRITE = Zombie.getDefaultSprite();
    protected Sprite DEFAULT_WALK_SPRITE = null;
    
    @Deprecated
    public static ActionSprite getDefaultAttackSprite() {return new ActionSprite("resources/sprites/zombie/zombie_man_attack.png",
            Vector2D.CENTER.copy(), 47, 41, 0, 0, 50, 27, 1, true, 23, ActionSprite.DELAY_FRAME) {
                @Override
                public void trigger() {
                    
                }
            }; //TODO: WTF is this <- This... Is... SPARTA!!!
    }
    
    protected ActionSprite attackSprite = new ActionSprite("resources/sprites/zombie/attack_soldier.png",
            Vector2D.CENTER.copy(), 100, 88, 0, 0, 100, 4, 4, true, false, 2, ActionSprite.DELAY_FRAME) {            
            @Override
            public void trigger() {
                if(!AIManager.isEnabled())
                    this.stop();
                if(this.isRunning() && this.isActive())
                    attack();
            }
    };
    {
        attackSprite.resizeImages(Zombie.DEFAULT_WIDTH, Zombie.DEFAULT_HEIGHT);
        attackSprite.flipImages(Sprite.FLIP_HORIZONTAL);
    }

    public static Sprite getTestSprite() {
        Sprite s = new Sprite("resources/sprites/zombie/spritesheet_walk.png",
            Vector2D.CENTER.copy(), 100, 88, 0, 0, 80, 4, 4);
        
        s.flipImages(Sprite.FLIP_HORIZONTAL);
        s.resizeImages(Zombie.DEFAULT_WIDTH, Zombie.DEFAULT_HEIGHT);
        
        return s;
    }
    
    public static final int DEFAULT_WIDTH = 46;
    public static final int DEFAULT_HEIGHT = 40;
    
    @Override
    protected boolean manageCollision(Body b) {
        return false;
    }
    
    private static ArrayList<Zombie> zombies = new ArrayList<>();
    
    public static ArrayList<Zombie> getList() {return zombies;}
    public static void register(Zombie z) {zombies.add(z); AIManager.register(z);}
    public static void remove(Zombie z) {zombies.remove(z); AIManager.remove(z);}
    public static void removeAll() {
        for(Iterator<Zombie> it = zombies.iterator();it.hasNext();) {
            AIManager.remove(it.next());
            it.remove();
        }
    }
    
    public static void setBars(boolean showBars) {for(Zombie z : zombies)z.setShowBars(showBars);}
    
    @Override
    public void damage(float d) {
        super.damage(d);
        if(health<=0) {
            this.setShowBars(false);
            
            Paintables.register(getDeathAnimation());
            Paintables.register(new TimedSentence(position.copy(), "$10 kill", 5f, 0, .2f, 1000, true, .4f));
                
            destroy();
        }
    }
    
    @Override
    public void load() {
        super.load();
        Zombie.register(this);
    }
    
    @Override
    public void destroy() {
        super.destroy();
        this.sprite.stop();
        Zombie.remove(this);
    }

    protected float movementsRange = 300;
    protected float weaponsRange = 15;
    private Vector2D closest;
    
    public void getClosestPlayer() {
        List<Player> players = Player.getPlayers();
        if(players.size()==1) {
            closest = Player.getDefaultPlayer().position;
            return;
        }
        
        float distance = -1;
        for(Player p : players) {
            float foo = this.position.distance(p.position);
            if(foo > distance) {
                distance = foo;
                closest = p.position;
            }
        }
    }
    
    @Override
    public boolean checkMovementsRange() {
        Vector2D pos = closest;
        float distance;
        if(pos == null) {
            throw new ExceptionInInitializerError("No players found!");
        } else {
            distance = this.position.distance(pos);
            if(distance <= movementsRange)
                return true;
            else
                return false;
        }
    }

    @Override
    public boolean checkWeaponsRange() {
        Vector2D pos = closest;
        float distance;
        if(pos == null) {
            throw new ExceptionInInitializerError("No players found!");
        } else {
            distance = this.position.distance(pos);
            if(distance <= weaponsRange)
                return true;
            else
                return false;
        }
    }
    
    @Override
    public void handleMovement() {
        if(attackSprite.isRunning()) {
            this.attackSprite.stop();
            this.sprite = DEFAULT_SPRITE;
            this.sprite.startSprite();
        }
        
        Vector2D pos = closest;
        if(!this.independent) {
            this.followPlayer(pos);
        }
    }

    @Override
    public void handleAttack() {
        if(!attackSprite.isRunning()) {
            this.sprite.stop();
            this.sprite = attackSprite;
            this.attackSprite.startSprite();
        }
        
        Vx = 0;
    }
    
    @Override
    public void attack() {
        Player.getDefaultPlayer().damage(20);
    }

    @Override
    public void handleIdling() {
        this.Vx = 0;
    }

    @Override
    public void handleBefore() {
        getClosestPlayer();
    }

    @Override
    public Body copy() {
        Zombie z = new Zombie(name,position.copy(),Zombie.getDefaultSprite(),mass,independent);

        return z;
    }
}
