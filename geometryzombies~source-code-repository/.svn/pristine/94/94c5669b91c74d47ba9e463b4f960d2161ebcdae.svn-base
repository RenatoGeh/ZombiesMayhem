package geometryzombiesmayhem;

import java.util.*;

/**
 * Handles all firing functionality.
 * 
 * @author Renato Lui Geh
 */
public abstract class Gun extends Item {
    private Attributes attributes;
    private Map<ProjectileType, Integer> projectiles = new HashMap<>();
    private ProjectileType currentProj;
    private ArrayList<FireMode> modes = new ArrayList<>();
    private int currentModeN = 0;
    private FireMode currentMode;
    private FireMode.ShootAction currentSa = null;
    private GunType type = this.getDefaultGunType();
    
    private float minDamage = 10; //base minimum damage
    private float maxDamage = 30; //base maximum damage
    
    private float recoilDampenerX = 1, recoilDampenerY = 1;
    
    {
        ProjectileType[] types = ProjectileType.getTypes();
        
        for(int i=0;i<types.length;i++)
            this.projectiles.put(types[i], 0);
    }
    
    /**
     * Serialization constructor.
     */
    public Gun() {super();};
    
    public Gun(String name, ProjectileType defaultProj, int initialAmmo, FireMode... modes) {  
        super(name, AssetManager.loadImage("resources/interface/icons/glock_icon.png").resize(.5f, .5f), 
                new Sprite("resources/items/guns/glock_icon_sprite.png", 
                Vector2D.CENTER.copy(), 
                42, 55, 0, 0, 50, 8, 1), 100f);
        
        this.currentProj = defaultProj;
        this.addAmmo(defaultProj, initialAmmo);
        this.modes.addAll(Arrays.asList(modes));
        currentMode = modes[0];
        
        attributes = new Attributes(Attributes.test);
        attributes.construct(this);
    }
    
    protected void startShooting() {
        currentSa = currentMode.start(currentProj,this);
        
        if(!this.supportsMode(FireMode.AUTOMATIC))
            return;
        
        if(this.currentMode.equalsMode(FireMode.AUTOMATIC))
            this.type.getStance(GunType.Stance.FIRE).frameLength = 
                    (int)this.currentMode.getDelay();
        else
            this.type.getStance(GunType.Stance.FIRE).frameLength = 
                    this.type.getDefaultFireLength();
    }
    
    protected void setSupportedProjectiles(ProjectileType... types) {
        projectiles.clear();
        this.addSupportedProjectiles(types);
    }
    
    protected void addSupportedProjectiles(ProjectileType... types) {
        for(int i=0;i<types.length;i++)
            if(!this.supportsType(types[i]))
                projectiles.put(types[i], 0);
    }
    
    protected void clearSupportedProjectiles() {projectiles.clear();}
    
    protected boolean supportsMode(FireMode mode) {return modes.contains(mode);}
    
    protected boolean supportsType(ProjectileType type) {return projectiles.containsKey(type);}
    
    protected void fire() {currentMode.trigger(currentProj, Player.getDefaultPlayer().getPosition(), 
            GameFrame.mousePosition, this, currentSa);}
    
    protected boolean isShooting() {return currentMode.isShooting();}
    
    protected void stopShooting() {currentMode.stop(currentSa);}
    
    protected boolean checkCondition(int shotNumber) {return projectiles.get(currentProj)>shotNumber-1;}
    protected void manageAfterShots(int shotNumber) {System.err.println(getAmmo(currentProj)); removeAmmo(currentProj, shotNumber);}
    
    protected void switchFireMode() {currentModeN = ((currentModeN<modes.size()-1)? currentModeN + 1 : 0); currentMode = modes.get(currentModeN);};
    
    protected void switchProjectileType(ProjectileType type) {this.currentProj = type;}
    protected ProjectileType getCurrentType() {return currentProj;}
    
    protected final void setAmmo(ProjectileType type, int n) {if(supportsType(type)) projectiles.put(type, n);}
    protected final void addAmmo(ProjectileType type, int n) {if(supportsType(type)) projectiles.put(type, getAmmo(type) + n);}
    protected final void removeAmmo(ProjectileType type, int n) {if(supportsType(type)) projectiles.put(type, projectiles.get(type) - n);}
    protected final void resetAmmo(ProjectileType type) {
        Set<ProjectileType> types = new HashSet<>();
        Set<Map.Entry<ProjectileType, Integer>> collection = projectiles.entrySet();
        
        for(Iterator it = collection.iterator();it.hasNext();) {
            Map.Entry<ProjectileType, Integer> entry = (Map.Entry<ProjectileType, Integer>)it.next();
            types.add(entry.getKey());
        }
        
        if(!types.isEmpty())
            for(ProjectileType t : types)
                projectiles.put(t, 0);
    }
    
    public void setRecoilDampener(float recoilDampenerX, float recoilDampenerY) {this.recoilDampenerX = recoilDampenerX; this.recoilDampenerY = recoilDampenerY;}
    public Vector2D getRecoilDampener() {return new Vector2D(recoilDampenerX, recoilDampenerY);}
    public float getRecoilDampenerX() {return recoilDampenerX;}
    public float getRecoilDampenerY() {return recoilDampenerY;}
    public GunType getGunType() {return type;}
    
    public Attributes getAttributes() {return attributes;}
    
    public float getMaxDamage() {return maxDamage;}
    public float getMinDamage() {return minDamage;}
    
    public float getDamage() {return (float)(ZM.RANDOM.nextInt((int)(maxDamage-minDamage) + 1) + (int)minDamage);}
    
    protected void setMaxDamage(float maxDamage) {this.maxDamage = maxDamage;}
    protected void setMinDamage(float minDamage) {this.minDamage = minDamage;}
    
    protected GunType getDefaultGunType() {
        return new GunType("Default_Pistol", 
            new Sprite("resources/models/player/idle_arms_marked.png", Vector2D.ZERO.copy(),
            33, 39, 0, 0, 250, 4, 1),
            new Sprite("resources/models/player/aim_arms_marked.png", Vector2D.ZERO.copy(), 
            39, 39, 0, 0, 550, 3, 1, true, false), 
            new ActionSprite("resources/models/player/fire_arms.png", Vector2D.ZERO.copy(),
            54, 39, 0, 0, 50, 7, 1, true, false, 3, ActionSprite.DELAY_FRAME) {
                @Override
                public void trigger() {
                    System.err.println("It's a pistol.");
                }
            });
    }
    
    protected void setGunType(GunType type) {this.type = type;}
    
    protected int getAmmo(ProjectileType type) {
        if(this.supportsType(type)) return projectiles.get(type);
        else throw new IllegalArgumentException("No such ProjectileType!");
    }
    
    protected int getAmmo() {return this.getAmmo(currentProj);}
    
    protected FireMode getCurrentProperties() {return currentMode;}
    
    @Deprecated
    private boolean hasKey(ProjectileType type) {return projectiles.containsKey(type);}

    @Override
    protected void onUse(Player p) {
        p.addGun(this);
    }
    
    @Override
    protected void onCollision(Player p) {
        
    }

    public static class GunType implements java.io.Serializable {
        /* 
        * Note to self:
        *       
        * Order of Sprites:
        *   - Idle
        *   - Aim
        *   - Fire
        */
        
        private String name;
        private Sprite[] stances;
        private int defFireLength;
        
        protected GunType() {};
        
        protected GunType(String name, Sprite... stances) {
            this.name = name;
            this.stances = stances;
            this.defFireLength = stances[Stance.FIRE.ordinal()].frameLength;
        }
        
        public String getName() {return name;}
        public Sprite getStance(Stance s) {return stances[s.ordinal()];}
        public Sprite[] getStances() {return stances;}
        
        protected int getDefaultFireLength() {return defFireLength;}
        
        public static enum Stance {IDLE, AIM, FIRE};
    }
    
    private static final long serialVersionUID = 828288281234712L;
}