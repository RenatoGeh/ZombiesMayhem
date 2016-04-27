package geometryzombiesmayhem;

public class Torso extends Member {
    public Torso(Sprite s) {
        super(s);
    }

    @Override
    public void refresh() {
        at.setToIdentity();
        at.translate(offset.getX(), offset.getY());
    }
    
    private static final long serialVersionUID = 89746516468L;
}
