package geometryzombiesmayhem;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Types of Fire Modes.
 *  
 * @author Renato Lui Geh
 */
public enum FireMode {
    AUTOMATIC(100, TimeUnit.MILLISECONDS), SEMI_AUTO(500, TimeUnit.MILLISECONDS),
    BURST(1, 3, 250, 50, TimeUnit.MILLISECONDS), BOLT_ACTION(1, TimeUnit.SECONDS),
    PUMP_ACTION(6, 1, 0, TimeUnit.SECONDS);
    
    private long delay = 0, inBetweenDelay = 0;
    private int shots = 1;
    private int burstShots = 0;
    private TimeUnit unit;
    
    private boolean isShooting = false;
    
    private int burstTracker = 0;
        
    private ProjectileType type = ProjectileType.LASER;
    
    private Random rand = ZM.RANDOM;
    
    private FireMode(long delay, TimeUnit unit) {
        this.delay = delay;
        this.unit = unit;
    }
    
    private FireMode(long delay, long inBetweenDelay, TimeUnit unit) {
        this(delay, unit);
        
        this.inBetweenDelay = inBetweenDelay;
    }
    
    private FireMode(int shots, long delay, TimeUnit unit) {
        this(delay, unit);
        
        this.shots = shots;
    }
    
    private FireMode(int shots, long delay, long inBetweenDelay, TimeUnit unit) {
        this(shots, delay, unit);
        
        this.inBetweenDelay = inBetweenDelay;
    }
    
    private FireMode(int shots, int burstShots, long delay, long inBetweenDelay, TimeUnit unit) {
        this(shots, delay, inBetweenDelay, unit);
        
        this.burstShots = burstShots;
    }
    
    public ShootAction start(ProjectileType type,Gun gun) {
        this.type = type;
        
        long initialDelay = Math.abs((long)(((inBetweenDelay > 0)? inBetweenDelay : delay)));
        
//        System.err.println(initialDelay + "\t" + delay + "\t" + inBetweenDelay);
        
        
        ShootAction s = new ShootAction(gun);
        if(GameFrame.velocity>0)
            s.run();
        
        isShooting = true;
        
        if(this == SEMI_AUTO) return null;
        
        AwesomeTimer.setTimeAffected(s, true);
        AwesomeTimer.setWarpTime(s, Player.getDefaultPlayer());
        AwesomeTimer.addAction(s, initialDelay, true, unit);
        return s;
    }
    
    public void stop(ShootAction sa) {AwesomeTimer.remove(sa); isShooting = false;}
    
    public boolean isShooting() {return isShooting;}
    
    protected void syncShooting(boolean isShooting) {this.isShooting = isShooting;}
    
    public void shoot(ProjectileType type, Vector2D position, Vector2D mousePosition, Gun gun) {        
        for(int i=0;i<shots;i++) {
            float spread = i*(20*rand.nextFloat() * ((rand.nextBoolean())? 1 : -1));
            type.shoot(position, mousePosition.add(0, spread), gun.getDamage(), gun, gun.getAttributes());
        }

        float recoilX = ((rand.nextFloat() * 10) * ((rand.nextBoolean())? 1 : -1))/gun.getRecoilDampenerX();
        float recoilY = ((rand.nextFloat() * 10))/gun.getRecoilDampenerY();
        mousePosition.subtractLocal(recoilX, recoilY);
        Player.getDefaultPlayer().mouseMoved(null, mousePosition);
        gun.manageAfterShots(shots);
    }
    
    public void trigger(ProjectileType type, Vector2D position, Vector2D mousePosition, Gun gun, ShootAction a) {
        if(!gun.checkCondition(shots)) return;
        
        if(inBetweenDelay > 0) {
            if(burstTracker >= burstShots) {
                AwesomeTimer.remove(a);
                AwesomeTimer.addAction(a, delay, true, unit);
                burstTracker = 0;
            } else {
                if(burstTracker == 0) {
                    AwesomeTimer.remove(a);
                    AwesomeTimer.addAction(a, inBetweenDelay, true, unit);
                }
                shoot(type, position, mousePosition,gun);
                burstTracker++;
            }
        } else {
            shoot(type, position, mousePosition,gun);
        }
    }
    
    public class ShootAction extends AwesomeAction {
        private Gun g;
        public ShootAction(Gun gun) {
            g = gun;
        }
        
        @Override
        public void run() {
            trigger(type, Player.getDefaultPlayer().getPosition(), GameFrame.mousePosition,g,this);
        }
        
    }
    
    public long getDelay() {return delay;}
    public long getInBetweenDelay() {return inBetweenDelay;}
    public long getShots() {return shots;}
    public TimeUnit getTimeUnit() {return unit;}
    public int getBurstShots() {return burstShots;}
    
    public void setDelay(long delay) {this.delay = delay;}
    public void setInBetweenDelay(long inBetweenDelay) {this.inBetweenDelay = inBetweenDelay;}
    public void setShots(int shots) {this.shots = shots;}
    public void setTimeUnit(TimeUnit unit) {this.unit = unit;}
    public void setBurstShots(int burstShots) {this.burstShots = burstShots;}
    
    public void setRate(long delay, TimeUnit unit) {
        this.setDelay(delay);
        this.setTimeUnit(unit);
    }
    
    public void setFireShots(int shots, int burstShots) {
        this.setShots(shots);
        this.setBurstShots(burstShots);
    }
    
    public boolean equalsMode(FireMode e) {
        return e.ordinal() == this.ordinal() && e.name().equals(this.name());
    }
}