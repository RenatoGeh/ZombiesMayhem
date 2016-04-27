package geometryzombiesmayhem;

public class Legs extends Member {
    public static enum Stance {IDLE, WALKING, CROUCH};
    private Stance stance;
    
    public Legs(Sprite s) {
        super(s);
        stance = Stance.IDLE;
    }
    
    public void setStance(Stance stance) {this.stance = stance;}
    public Stance getStance() {return stance;}
    
    @Override
    public void refresh() {
        at.setToIdentity();
        at.translate(offset.getX(), offset.getY());
    }
    
    public Sprite getDefaultSprite(Stance stance) {
        switch(stance) {
            case IDLE:
                return DEFAULT_IDLE;
            case WALKING:
                return DEFAULT_WALKING;
            case CROUCH:
                return null;
        }
        throw new IllegalArgumentException("No such Stance!");
    }
    
    private Sprite DEFAULT_WALKING = new Sprite("resources/models/player/walk_legs.png", Vector2D.ZERO.copy(), 
                        31, 39, 0, 0, 50, 14, 1);
    private Sprite DEFAULT_IDLE = new Sprite("resources/models/player/idle_legs.png", Vector2D.ZERO.copy(),
                        33, 39, 0, 0, 250, 4, 1);
    
    private static final long serialVersionUID = 5646516841534L;
}
