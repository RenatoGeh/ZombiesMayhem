package geometryzombiesmayhem;

/**
 * Player's Package
 * 
 * <p>
 *  A <i>Package</i> to contain all of the Player's data
 *  to be sent over through the internet.
 * </p>
 * 
 * @author Renato Lui Geh
 * @see Client
 * @version 0.0.2
 */
public class PlayerPackage implements java.io.Serializable {
    private String name;
    private Vector2D position;
    private float health;
    private String stance;
    private Vector2D direction;
    
    public PlayerPackage() {
        this.name = "Player";
        this.health = 0;
        this.position = Vector2D.ZERO.copy();
        this.stance = "";
        this.direction = Vector2D.ZERO.copy();
    }
    
    public PlayerPackage(PlayerPackage p) {
        this.name = p.getName();
        this.health = p.getHealth();
        this.position = p.getPosition();
        this.stance = p.getStance();
        this.direction = p.getDirection();
    }
    
    public PlayerPackage(String name, Vector2D position, Vector2D direction, float health, String stance) {
        this.name = name;
        this.position = position;
        this.health = health;
        this.stance = stance;
        this.direction = direction;
    }
    
    public void refreshPackage(PlayerPackage p) {
        this.name = p.getName();
        this.health = p.getHealth();
        this.position = p.getPosition();
        this.stance = p.getStance();
        this.direction = p.getDirection();
    }
    
    public Vector2D getDirection() {
        return this.direction;
    }
    
    public String getStance() {
        return this.stance;
    }
    
    public Vector2D getPosition() {
        return this.position;
    }
    
    public float getHealth() {
        return health;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return ("Name: " + this.name + "\n" +
                "Position: " + this.position.toString() + "\n" +
                "Direction" + this.getDirection().toString() + "\n" +
                "Health: " + this.getHealth() + "\n" +
                "Stance: " + this.getStance());
    }
    
    private static final long serialVersionUID = 987654651321L;
}