package geometryzombiesmayhem;

public class Head extends Member {
    public Head(Sprite s, Vector2D anchor) {
        super(s);
        this.anchor = anchor;
    }
    
    public Head(Sprite s, Vector2D anchor, Vector2D offset) {
        this(s, anchor);
        this.offset = offset;
    }
    
    /**
     * <b>ATTENTION:</b> Auto anchor positioning!
     * @param s
     * @param offset 
     */
    public Head(Sprite s, Vector2D anchor, Vector2D offset, boolean auto) {
        super(s);
        this.offset = offset;
        if(auto)
            this.retrieveEmbeddedAnchor();
//            s.filterSprite(GameSettings.Graphics.getFilter());
        else
            this.anchor = anchor;
    }

    public static float createAngle(Vector2D position, Vector2D direction, boolean mirrored) {
        float a = Member.createAngle(position, direction);
        
//        System.err.println(a);
        if(!mirrored) {
            if(a > 0.3 || a < -1)
                return ((a>0.3f)? .3f : (a<-1f)? - 1f : a);
        } else {
            if(a < -0.3 || a > 1)
                return ((a<-0.3f)? -.3f : (a>1f)?  1f : a);
        }
        
        return a;
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
    
    private static final long serialVersionUID = 456875321587546L;
}
