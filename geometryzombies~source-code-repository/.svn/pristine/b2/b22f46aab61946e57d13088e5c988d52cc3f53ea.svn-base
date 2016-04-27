package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Base class for the sub-classes that extend Player.
 *
 * @version 0.0.9
 * @author Renato Lui Geh
 */
public class Player extends Person {

    /**
     * Player's name.
     */
    private String name;
    /**
     * Player's controllability.
     */
    private boolean controllable = true;
    /**
     * Player's stamina
     */
    private float stamina;
    private float maxStamina;
    /**
     * Player's direction.
     *
     * @see Vector2D
     */
    private Vector2D direction = Vector2D.ZERO.copy();
    private boolean bulletTime = false;
    private boolean isAiming = false;
    private boolean isSprinting = false;
    private boolean isMoving = false;
    private boolean isShooting = false;
    private boolean enemyOnSight = false;
    public boolean hasControl = true;
    public boolean interactibleInRange = false;
    private float defaultSpeed = 3f;
    private int jumps = 0, jumplimit = 2;
    private ArrayList<Gun> guns = new ArrayList<>();
    private int currentGunN = 0;
    private Model m;
    
//    public FlareShape flare = new FlareShape.Elliptic(this.direction, new Vector2D(250, 250), 100, 200, 0, Color.WHITE);
    private FlareShape flare = null;// = new FlareShape.Conical(this.position.copy(), this.direction, 200, (float)Math.PI/9, Color.BLACK);
//    /**
//     * Creates a new Player. Use this with super when extending.
//     * 
//     * @param name
//     * @param position
//     * @param bodyImage
//     * @param mass 
//     */
//    public Player(String name, Vector2D position, Image bodyImage, float mass) {
//        this(name,position,bodyImage,mass,100);
//    }
//    /**
//     * Creates a new Player. Use this with super when extending. 
//     * This version of the constructor sets a default position.
//     * 
//     * @param name
//     * @param bodyImage
//     * @param mass 
//     */
//    public Player(String name, Image bodyImage, float mass) {
//        this(name,Vector2D.CENTER.copy(),bodyImage,mass,100);
//    }
//    
//    /**
//     * Creates an instance of a Player by using Stats.
//     * 
//     * <p><u>How to use it:</u></p>
//     * <p>Player p1 = new Player(AssetManager.loadStats("Resources/Player/myPlayer.player");</p>
//     * <p><b>Keep in mind that you must, for now, have the file myPlayer.player somewhere inside the class directory.</b></p>
//     * <p>Use of Program Files or AppData will be made possible shortly.</p>
//     * 
//     * @see Stats
//     * 
//     * @param stat 
//     */
//    public Player(Stats stat) {
//        this(stat.getName(),stat.getPosition(),stat.getImage(),stat.getMass(),100);
//        
//        controllable = stat.getControllability();
//    }
    
    /**
     * Serialization constructor.
     */
    public Player() {super();};
    
    public Player(String name, Vector2D position, Sprite sprite, float mass, float stamina) {
        super(mass, sprite, position.getX(), position.getY(), 100);

        this.name = name;
        this.interactible = true;
        this.id = Body.ID_PLAYER;
        this.stamina = this.maxStamina = stamina;
        this.neverDown = this.frameBoundsAffected = false;

        GameSettings.Controls.reset(controlMap);
        initStances();

        this.addGun(new Pistol(ProjectileType.BULLET, 50));
        this.addGun(new Rifle(ProjectileType.BULLET, 50));

        m = new Model(this);
        AwesomeTimer.addAction(s, 10, true, TimeUnit.MILLISECONDS);
    }

    public Player(String name, Vector2D position, float mass) {
        this(name, position, null, mass, 100);
        sprite = STANCE_IDLE;
    }

//    /**
//     * Sets the image of the player.
//     * Returns true if it has caught an exception.
//     * 
//     * @param bodyImage
//     * @return errorFound
//     */
//    public boolean setImage(Image bodyImage) {
//        boolean errorFound = false;
//        try {
//            this.bodyImage = bodyImage;
//        }
//        catch(Exception exc) {
//            errorFound = true;
//            exc.printStackTrace();
//        }
//        return errorFound;
//    }
//    /**
//     * Gets the Player image.
//     * 
//     * @return bodyImage
//     */
//    public Image getImage() {
//        return bodyImage;
//    }
    /**
     * Sets the player name. Returns true if it has caught an exception.
     *
     * @param name
     * @return errorFound
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the player's name.
     *
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Checks whether the player is out of bounds. Return true if player is out
     * of bounds.
     *
     * <p>Note: this method checks whether the player is out of the PaintArea's
     * bounds, and not the Sidescrolling bounds.</p>
     *
     * @return
     */
    public boolean isOutOfBounds() {
        if (position.getX() > GameFrame.FRAME_WIDTH || position.getY() > GameFrame.FRAME_HEIGHT
                || position.getX() < 0 || position.getY() < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Forces the player to jump.
     */
    public void jump() {
        if(getStamina()<30 && !GameFrame.godMode) return;
        if (hasBounced) {
            jumps = 0;
            hasBounced = false;
        }
        jumps++;
        //System.err.println(jumps + "\t" + jumplimit);
        if (jumps > jumplimit && !GameFrame.godMode) {
            return;
        }
        addTempYForce(Fy);
        Vy = -12.5f;
        if(!GameFrame.godMode) subtractStamina(30);
    }

    /**
     * Moves the player. Returns player's current position.
     *
     * <p><b>This is the X-Axis only version. See other variants for more
     * information</b></p>
     *
     * @param speed
     * @return position
     */
    public Vector2D move(float speed) {
        this.position.addLocal(speed, 0);
        return position;
    }

    /**
     * Moves the player. Returns player's current position.
     *
     * <p><b>This is the Vector2D version. See other variants for more
     * information</b></p>
     *
     * @param speed
     * @return position
     */
    public Vector2D move(Vector2D speed) {
        this.position.addLocal(speed);
        return position;
    }

    /**
     * Moves the player. Returns player's current position.
     *
     * <p><b>This is the X and Y axis version. See other variants for more
     * information</b></p>
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
     * @return
     */
    @Override
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * Sets the player controllability.
     *
     * @param control
     * @return
     */
    public void setControllable(boolean control) {
            controllable = control;
    }

    /**
     * Returns the player controllability.
     *
     * @return
     */
    public boolean isControllable() {
        return controllable;
    }
    
    @Override
    public void damage(float damage) {
        super.damage(damage);
        
        if(Player.getDefaultPlayer() == this)
            UserInterface.getUI().notifyHealth();
    }

    protected void update() {
        if (!guns.get(currentGunN).isShooting() && this.isShooting) {
            this.isShooting = false;
        }
    }

    public Vector2D getSize() {
        return new Vector2D(sprite.frameWidth, sprite.frameHeight);
    }
    
    public LightSource getFlashlight() {return flare;}
    public void setFlashlight(LightSource source) {this.flare = (FlareShape)source;}

    public float getStamina() {
        return stamina;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public Gun getGun() {
        return guns.get(currentGunN);
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public void addStamina(float add) {
        stamina += add;
        if(stamina>maxStamina) stamina = maxStamina;
    }

    public void subtractStamina(float sub) {
        stamina -= sub;
        if(stamina<0)stamina = 0;
    }

    public final void addGun(Gun g) {
        g.setPosition(this.position);
        this.guns.add(g);
    }
    
    public void removeGun(Gun g) {
        this.guns.remove(g);
    }

    public void removeCurrentGun() {
        this.guns.remove(currentGunN);
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isAiming() {
        return isAiming;
    }

    public boolean isShooting() {
        return isShooting;
    }
    
    protected void setEnemyOnSight(boolean enemyOnSight) {this.enemyOnSight = enemyOnSight;}

    @Override
    @Deprecated
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        m.paint(g2, c);
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        m.paint(g2, c);
    }

    @Override
    public void checkBoundsInteractions() {
        m.manageBounds();
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
        return position.getY() + sprite.frameHeight;
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

    public void keyReleased(KeyEvent event) {
        if (!hasControl || !controlMap.containsKey(event.getKeyCode())) {
            return;
        }
        keyReleasedString(controlMap.get(event.getKeyCode()));
    }
        
    public void keyReleasedString(String s) {
        switch (s) {
            case "left":
            case "right":
                if (isMoving) {
                    Vx = 0;
                    isMoving = false;
                }
                break;
            case "down":
                if (!isMoving) {
                    break;
                }
            case "sprint":
                if(isSprinting) {
                    isSprinting = false;
                    addStaminaChange(0.1f);
//                    this.subtractStamina(0.1f);
                }
                if (isMoving && !isAiming) {
                    Vx -= Math.signum(Vx) * (DEFAULT_SPRINTING_ADDED_SPEED);
                }
                break;
            case "bullet_time":
                if(bulletTime) {
                    bulletTime = false;
                    addStaminaChange(0.1f);
//                    this.subtractStamina(0.1f);
                }
                GameFrame.velocity = 1f;
                break;
            case "interact":
                this.interactibleInRange = false;
                break;
        }
    }

    public void keyPressed(KeyEvent event) {
        
        if (!hasControl || !controlMap.containsKey(event.getKeyCode())){
            return;
        }
        keyPressedString(controlMap.get(event.getKeyCode()));
    }
        
   public void keyPressedString(String s) {
        switch (s) {
            case "sprint":
                if (isMoving && !isSprinting && !isAiming){
                    Vx += Math.signum(Vx) * (DEFAULT_SPRINTING_ADDED_SPEED);
                }
                if(!isSprinting){
                    isSprinting = true;
                    addStaminaChange(-0.1f);
                }
                    
                break;
            case "up":
                jump();
                break;
            case "down":
                Vx = 0;
                isMoving = false;
                isSprinting = false;
                break;
            case "left":
                if (!isMoving) {
                    Vx = -DEFAULT_PLAYER_SPEED;
                    if (isAiming) {
                        Vx -= -(DEFAULT_AIMING_REDUCED_SPEED);
                    } else if (isSprinting) {
                        Vx += -(DEFAULT_SPRINTING_ADDED_SPEED);
                    }
                    isMoving = true;
                }
                break;
            case "right":
                if (!isMoving) {
                    Vx = DEFAULT_PLAYER_SPEED;
                    if (isAiming) {
                        Vx -= (DEFAULT_AIMING_REDUCED_SPEED);
                    } else if (isSprinting) {
                        Vx += (DEFAULT_SPRINTING_ADDED_SPEED);
                    }
                    isMoving = true;
                }
                break;
            case "bullet_time":
                if(!bulletTime) {
                    GameFrame.velocity = .1f;
                    bulletTime = true;
                    addStaminaChange(-0.1f);
                }
                break;
            case "fire_mode":
                guns.get(currentGunN).switchFireMode();
                break;
            case "flashlight":
                if(flare != null)
                    flare.toggleEnabled();
                break;
            case "interact":
                this.interactibleInRange = true;
                break;
            case "reload":
                UserInterface.getUI().notifyGun();
                break;
        }
    }

    public void keyTyped(KeyEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseMoved(MouseEvent e, Vector2D mousePosition) {
        this.direction.set(mousePosition);
//        this.lookAt(mousePosition);
        m.lookAt();

        synchronized(Zombie.getList()) {
            for (Zombie z : Zombie.getList()) {
                if (z.getBounds().contains(mousePosition.getX(), mousePosition.getY())) {
                    z.setShowBars(true);
                } else if (z.showBars) {
                    z.setShowBars(false);
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e, Vector2D mousePosition) {
        if (!hasControl) {
            return;
        }
        switch (this.controlMap.get(e.getButton())) {
            case "shoot":
                if(this.isAiming || this.enemyOnSight) {
                    if (guns.get(currentGunN).getAmmo(guns.get(currentGunN).getCurrentType()) > 0) {
                        this.isShooting = true;
                    } else {
                        isShooting = false;
                    }
                }
                break;
            case "aim":
                if (isMoving && !isAiming) {
                    if (isSprinting) {
                        Vx -= Math.signum(Vx) * (DEFAULT_SPRINTING_ADDED_SPEED);
                    }
                    Vx -= Math.signum(Vx) * (DEFAULT_AIMING_REDUCED_SPEED);
                }
                this.isAiming = true;
                guns.get(currentGunN).setRecoilDampener(5, 5);
                break;
            case "mouse_secondary":
                break;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (!hasControl) {
            return;
        }
        switch (this.controlMap.get(e.getButton())) {
            case "shoot":
                guns.get(currentGunN).stopShooting();                   
                
                if (guns.get(currentGunN).getCurrentProperties().equalsMode(FireMode.AUTOMATIC)) {
                    guns.get(currentGunN).getGunType().getStance(Gun.GunType.Stance.FIRE).stop();
                    guns.get(currentGunN).getCurrentProperties().syncShooting(false);
                }
                isShooting = false;
                break;
            case "aim":
                if (isMoving) {
                    if (isSprinting) {
                        Vx += Math.signum(Vx) * (DEFAULT_SPRINTING_ADDED_SPEED);
                    }
                    Vx += Math.signum(Vx) * (DEFAULT_AIMING_REDUCED_SPEED);
                }
                this.isAiming = false;
                guns.get(currentGunN).setRecoilDampener(1, 1);
                break;
            case "mouse_secondary":
                break;
        }
    }

    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int n = e.getWheelRotation();

        if (currentGunN + n < 0 || currentGunN + n > guns.size() - 1) {
            return;
        }

        currentGunN += n;
        this.m.refresh();
    }

    public void setControl(boolean c) {
        hasControl = c;
        if (!hasControl) {
            //stop moving and stuff
            if (isMoving) {
                Vx = 0;
                isMoving = false;
            }
        }
    }
    
    private void addStaminaChange(float v) {
        staminaChange+= v;
    }
    
    public float staminaChange = 0;
    
    AwesomeAction s = new AwesomeAction(){

        @Override
        public void run() {
//            Player.this.flarePosition = Player.this.position;
            if(Player.this.flare!=null)
                Player.this.flare.setPosition(Player.this.getCenter());
            if(staminaChange>0) addStamina(staminaChange);
            else if(staminaChange<0 && !GameFrame.godMode) subtractStamina(-staminaChange);
            if(getStamina()==0 || staminaChange==0) {
                addStamina(.05f);
                if(staminaChange!=0) {
                    keyReleasedString("bullet_time");
                    keyReleasedString("sprint");
                }
            }
        }
        
    };

    public PlayerPackage generatePackage() {
        return new PlayerPackage(name, position, direction, health, null);
    }

    public void retrievePackage(PlayerPackage p) {
        this.name = p.getName();
        this.position = p.getPosition();
        this.health = p.getHealth();
        this.direction = p.getDirection();
    }

    /**
     * <p> This method manages the stance of a remote player. </p>
     *
     * <p> <b> Do not use this method with the local player (Player 1). </b>
     * </p>
     */
    @Deprecated
    public void manageStances() {
//        switch (stance) {
//            case ID_IDLE:
//                sprite = STANCE_IDLE;
//                break;
//            case ID_WALK:
//                sprite = STANCE_WALK;
//                break;
//            case ID_DUCK:
//                sprite = STANCE_DUCK;
//                break;
//        }
//
//        sprite.startSprite();
    }

    private void initStances() {
        STANCE_IDLE = Player.getSprite(Player.Stance.IDLE);
        STANCE_WALK = Player.getSprite(Player.Stance.WALK);
        STANCE_DUCK = Player.getSprite(Player.Stance.DUCK);
        STANCE_FIRE = Player.getSprite(Player.Stance.FIRE);
    }
    private static final float DEFAULT_PLAYER_SPEED = 3;
    private static final float DEFAULT_SPRINTING_ADDED_SPEED = 2;
    private static final float DEFAULT_AIMING_REDUCED_SPEED = 2;
    private final Map<Integer, String> controlMap = new HashMap<>();
    public Sprite STANCE_IDLE;
    public Sprite STANCE_WALK;
    public Sprite STANCE_DUCK;
    public Sprite STANCE_FIRE;
    private static final String BASE_PATH = "resources/sprites/player/stance/";
    private static final String ID_IDLE = "idle.gif";
    private static final String ID_WALK = "walk.gif";
    private static final String ID_DUCK = "duck.gif";
    private static final String ID_FIRE = "fire.gif";

    @Override
    protected boolean manageCollision(Body b) {
        return false;
    }

    public SpriteAnimation getDeathAnimation() {
        SpriteAnimation deathAnim = new SpriteAnimation("resources/animations/death/marco_death.png",
                this.position, 38, 41, 0, 0, 50, 18, 1, 1, 0);

        return deathAnim;
    }

    @Deprecated
    public static Sprite getSprite(Player.Stance stance) {
        switch (stance) {
            case IDLE:
                return new Sprite("resources/sprites/player/stance/idle.png",
                        Vector2D.CENTER.copy(), 38, 38, 0, 0, 80, 6, 1, true);
            case FIRE:
                return new Sprite("resources/sprites/player/stance/auto_fire.png",
                        Vector2D.CENTER.copy(), 61, 38, 0, 0, 80, 8, 1, true);
            case WALK:
                return new Sprite("resources/sprites/player/stance/walk.png",
                        Vector2D.CENTER.copy(), 48, 41, 0, 0, 80, 12, 1, true);
            case DUCK:
                return new Sprite("resources/sprites/player/stance/duck.png",
                        Vector2D.CENTER.copy(), 41, 24, 0, 0, 80, 6, 1, true);
        }
        throw new IllegalArgumentException("No such stance!");
    }
    public static final int DEFAULT_WIDTH = 38;
    public static final int DEFAULT_HEIGHT = 38;

    public static enum Stance {
        IDLE, FIRE, WALK, DUCK
    };
    //Players are registered here.
    private static ArrayList<Player> otherPlayers = new ArrayList<>();
    private static ArrayList<Player> players = new ArrayList<>();
    private static Player defaultPlayer;

    public static Player getOtherPlayer(int index) {
        return otherPlayers.get(index);
    }

    public static Player getPlayers(int index) {
        return players.get(index);
    }

    public static Player getDefaultPlayer() {
        return defaultPlayer;
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static void register(Player p) {
        otherPlayers.add(p);
        players.add(p);
    }

    public static void remove(Player p) {
        otherPlayers.remove(p);
        players.remove(p);
    }

    public static void registerAsDefault(Player p) {
        defaultPlayer = p;
        players.add(0, defaultPlayer);
    }

    public static void managePlayers(boolean online, Graphics2D g2, Component c, Client playerClient) {
        if (online) {
            for (Player p : otherPlayers) {
                p.paint(c, g2);
                p.update();

                p.retrievePackage(playerClient.pp2);
                p.lookAt(p.getDirection());
            }
        }

        if (defaultPlayer.getHealth() <= 0 && defaultPlayer.foo) {
//            Paintables.register(defaultPlayer.getDeathAnimation());
            AIManager.setEnabled(false);
            Body.removeAll();
            Zombie.removeAll();
            
            defaultPlayer.foo = false;
            Body.remove(defaultPlayer);
            GameOverMenu.getInstance().load(GameFrame.paintArea);
        }

        for (Player p : players) {
            p.update();
        }
    }
    private boolean foo = true;

    @Override
    public Body copy() {
        return new Player(name,position.copy(),sprite.copy(),mass,maxStamina);
    }
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        GameSettings.Controls.reset(controlMap);
    }
    
    private static final long serialVersionUID = 1298310298312L;
}
