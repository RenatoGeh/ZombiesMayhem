package geometryzombiesmayhem;

/**
 * <p>A rifle.</p>
 * 
 * @author Renato Lui Geh
 */
public class Rifle extends Gun {
    
    /**
     * Serialization constructor.
     */
    public Rifle() {super();}
    
    public Rifle(ProjectileType defaultProj, int initialAmmo) {
        super("BaseRifle", defaultProj, initialAmmo, FireMode.SEMI_AUTO, FireMode.AUTOMATIC, FireMode.BURST);
        
        this.setSupportedProjectiles(defaultProj);
        this.setRecoilDampener(.5f, .5f);
        
        this.addAmmo(defaultProj, initialAmmo);
        
        this.setIcon(AssetManager.loadImage("resources/interface/icons/g3_icon.png").resize(.5f, .5f));
    }
    
    @Override
    protected GunType getDefaultGunType() {        
        return new GunType("Default_Rifle", 
            new Sprite("resources/models/player/rifle/idle.png", Vector2D.ZERO.copy(), 
            46, 39, 0, 0, 250, 7, 1),
            new Sprite("resources/models/player/rifle/aim.png", Vector2D.ZERO.copy(),
            46, 39, 0, 0, 250, 3, 1, true, false),
            new ActionSprite("resources/models/player/rifle/fire.png", Vector2D.ZERO.copy(),
            61, 39, 0, 0, 80, 4, 1, true, false, 0, ActionSprite.DELAY_FRAME) {
                
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
        return new Rifle(this.getCurrentType(),this.getAmmo());
    }
}