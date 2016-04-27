package geometryzombiesmayhem;

public class Arms extends Member {
    public static enum Stance {IDLE, AIMING, FIRE};
    private Stance stance;
    private Gun g;
    
    public Arms(Sprite s, Vector2D anchor, Gun g) {
        super(s);
        this.anchor = anchor;
        this.g = g;
        stance = Stance.IDLE;
    }
    
    public Arms(Sprite s, Vector2D anchor, Vector2D offset, Gun g) {
        this(s, anchor, g);
        this.offset = offset;
    }
    
    public Arms(Sprite s, Vector2D anchor, Vector2D offset, boolean auto, Gun g) {
        super(s);
        this.offset = offset;
        this.g = g;
        stance = Stance.IDLE;
        if(auto)
            this.retrieveEmbeddedAnchor();
//            s.filterSprite(GameSettings.Graphics.getFilter());
        else
            this.anchor = anchor;
    }
    
    public void setStance(Stance stance) {this.stance = stance;}
    public Stance getStance() {return stance;}
    
    public static float createAngle(Vector2D position, Vector2D direction) {
        return Member.createAngle(position, direction);
    }
    
    @Override
    public void refresh() {
        at.setToIdentity();
        at.translate(offset.getX(), offset.getY());
        
        if(this.mirrored)
            at.rotate(-angle, anchor.getX(), anchor.getY());
        else
            this.at.rotate(angle, anchor.getX(), anchor.getY());
    }
    
    
    public Sprite getDefaultSprite(Stance stance) {
        switch(stance) {
            case IDLE:
                return DEFAULT_IDLE;
            case AIMING:
                return DEFAULT_AIMING;
            case FIRE:
                return DEFAULT_FIRE;
        }
        throw new IllegalArgumentException("No such Stance!");
    }
    
    private Sprite DEFAULT_AIMING = new Sprite("resources/models/player/aim_arms_marked.png", Vector2D.ZERO.copy(), 
                        39, 39, 0, 0, 550, 3, 1);
    private Sprite DEFAULT_IDLE = new Sprite("resources/models/player/idle_arms_marked.png", Vector2D.ZERO.copy(),
                33, 39, 0, 0, 250, 4, 1);
    
    private Sprite DEFAULT_FIRE = new Sprite("resources/models/player/fire_arms.png", Vector2D.ZERO.copy(),
            54, 39, 0, 0, 50, 7, 1, true, false);
    
    private static final long serialVersionUID = -65464684512857L;
}