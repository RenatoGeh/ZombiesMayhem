package geometryzombiesmayhem;

/**
 * <p>A pistol.</p>
 * 
 * @author Renato Lui Geh
 */
public class Pistol extends Gun {
    
    public Pistol() {super();}
    
    public Pistol(ProjectileType defaultProj, int initialAmmo) {
        super("BasePistol", defaultProj, initialAmmo, FireMode.SEMI_AUTO, FireMode.AUTOMATIC);
        
        this.setSupportedProjectiles(defaultProj);
        this.setRecoilDampener(1.5f, 1.5f);

        this.addAmmo(defaultProj, initialAmmo);
    }
    
    @Override
    protected GunType getDefaultGunType() {
        return new GunType("Default_Pistol", 
            new Sprite("resources/models/player/idle_arms_marked.png", Vector2D.ZERO.copy(),
            33, 39, 0, 0, 250, 4, 1),
            new Sprite("resources/models/player/aim_arms_marked.png", Vector2D.ZERO.copy(), 
            39, 39, 0, 0, 550, 3, 1, true, false), 
            new ActionSprite("resources/models/player/fire_arms.png", Vector2D.ZERO.copy(),
            54, 39, 0, 0, 50, 7, 1, true, false, 3, ActionSprite.DELAY_FRAME) {
                
                private boolean isReady = false;
                
                @Override
                public void trigger() {
                    if(getCurrentProperties().equalsMode(FireMode.SEMI_AUTO)) {
                        isReady = true;
                    } else {
                        isReady = false;
                    }
                    
                    if(!isShooting())
                        startShooting();
                    if(getAmmo() <= 0) {
                        this.stop();
                        getCurrentProperties().syncShooting(false);
                    }
                }
                
                @Override
                protected synchronized void perFrameAction() {                    
                    if(isReady) {
                        if(this.frameIndex == (lines*cols)-2) {
                            this.stop();
                            getCurrentProperties().syncShooting(false);
                        }
                    }
                }
            });
    }

    @Override
    public Body copy() {
        return new Pistol(this.getCurrentType(),this.getAmmo());
    }
    
    
}