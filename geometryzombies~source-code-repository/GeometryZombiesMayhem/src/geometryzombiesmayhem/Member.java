package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class Member implements java.io.Serializable {
    protected AffineTransform at;
    protected Sprite s;

    protected Vector2D anchor;
    protected float angle = 0;
    
    protected boolean mirrored = false;
    
    protected Vector2D offset = new Vector2D();
    
    protected Member() {};
    
    protected Member(Sprite s) {
        this.s = s;
        this.at = new AffineTransform();
        at.translate(offset.getX(), offset.getY());
        this.anchor = new Vector2D();
    }
    
    protected Vector2D getAnchor() {return anchor;}
    protected float getAngle() {return angle;}
    protected AffineTransform getAffineTransform() {return at;}
    protected Image getImage() {return s.getCurrentFrame();}
    
    protected float getWidth() {
        return getImage().getWidth(null);//(float)(getImage().getWidth(null) * Math.sin(angle) + getImage().getHeight(null) * Math.cos(angle));
    }
    protected float getHeight() {
        return getImage().getHeight(null);//(float)(getImage().getWidth(null) * Math.cos(angle) + getImage().getHeight(null) * Math.sin(angle));
    }
    
    protected void setAngle(float angle) {this.angle = angle;}
    
    public void adjustPosition(int width, int height) {
        at.setToIdentity();
        
        int x = (int)(width/2 - this.getWidth()/2);
        int y = (int)(height/2 - this.getHeight()/2);
        
        this.refresh();
        
        at.translate(offset.getX(), offset.getY());
        at.translate(x, y);
    }
    
    protected void setSprite(Sprite s) {this.s = s;}
    protected void setSprite(Sprite s, boolean autoMarking) {
        this.s = s;
        if(autoMarking)
            this.retrieveEmbeddedAnchor();
    }
    protected Sprite getSprite() {return s;}
    
    public static float createAngle(Vector2D position, Vector2D direction) {
        Vector2D d = position.distanceVector(direction);
        float a = (float) Math.atan(d.getY() / d.getX());
        
        //a += ((d.getX() < 0)? Math.PI : 0);
        
        return a;
    }
    
    protected void setOffset(Vector2D offset) {
        at.translate(offset.getX() - this.offset.getX(), offset.getY() - this.offset.getY());
        this.offset = offset; 
    }
    protected void setOffset(float x, float y) {
        at.translate(x - offset.getX(), y - offset.getY());
        this.offset.set(x, y); 
    }
    protected Vector2D getOffset() {return offset;}
    
    public abstract void refresh();
    public void paint(Graphics2D g2,Vector2D pos) {
        s.paint(g2, null, at, (int)(pos.getX() + offset.getX()), (int)(pos.getY() + offset.getY()));
    }
    
    protected void retrieveEmbeddedAnchor() {       
        MutableImage im = s.getImage(0);
        
        for(int i=0;i<im.getWidth()-1;i++) {
            for(int j=0;j<im.getHeight()-1;j++) {
                Vector2D foo = this.checkAnchorMark(i, j, im);
                if(foo != null) {
                    anchor = foo;
                    s.unmarkImage(anchor, 0);
                    System.err.println(anchor);
                    return;
                }
            }
        }
    }
    
    private Vector2D checkAnchorMark(int i, int j, BufferedImage aux) {
        if(aux.getRGB(i, j) == Color.RED.getRGB())
            if(aux.getRGB(i+1, j) == Color.GREEN.getRGB())
                if(aux.getRGB(i+1, j+1) == Color.BLUE.getRGB())
                    if(aux.getRGB(i, j+1) == 0)
                        return new Vector2D(i, j);
        return null;
    }
    
    private static final long serialVersionUID = 8738721871872L;
}
