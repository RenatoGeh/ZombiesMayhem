package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.Vector2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

@Deprecated
public class Prop extends Body {
    private Shape bodyShape;
    private boolean usesImage;
    private boolean usesColor;
    private Vector2D size;
    private int type;
    private Color color;
    protected Image bodyImage;
    
    public Prop(Image bodyImage, Vector2D position, float mass) {
        super(mass, position.getX(), position.getY(), 0, 0);
        
        this.bodyImage = bodyImage;
        this.position = position;
        
        size = new Vector2D(bodyImage.getWidth(null), bodyImage.getHeight(null));
        
        usesImage = true;
    }
    
    public Prop(Image bodyImage, Vector2D position) {
        super(0, position.getX(), position.getY(), 0, 0);
        
        this.bodyImage = bodyImage;
        this.position = position;
        
        size = new Vector2D(bodyImage.getWidth(null), bodyImage.getHeight(null));
        
        usesImage = true;
    }
    
    public Prop(int type, Vector2D position, Vector2D size, float mass) {
        super(mass, position.getX(), position.getY(), 0, 0);
        
        this.position = position;
        
        this.bodyShape = retrieveShape(type, position, size);
        
        this.size = size;
        this.type = type;
        
        usesImage = false;
        usesColor = false;
    }
    
    public Prop(int type, Vector2D position, Vector2D size) {
        super(0, position.getX(), position.getY(), 0, 0);
        
        this.position = position;
        
        this.bodyShape = retrieveShape(type, position, size);
        
        this.size = size;
        this.type = type;
        
        usesImage = false;
        usesColor = false;
    }
    
    public Prop(int type, Vector2D position, Vector2D size, Color color) {
        super(0, position.getX(), position.getY(), 0, 0);
        
        this.position = position;
        this.bodyShape = retrieveShape(type, position, size);
        this.size = size;
        this.type = type;
        
        usesImage = false;
        usesColor = true;
        
        this.color = color;
    }
    
    public Prop(int type, Vector2D position, Vector2D size, float mass, Color color) {
        super(mass, position.getX(), position.getY(), 0, 0);
        
        this.position = position;
        this.bodyShape = retrieveShape(type, position, size);
        this.size = size;
        this.type = type;
        
        usesImage = false;
        usesColor = true;
        
        this.color = color;
        
        this.setBounciness(0.2f);
    }
    
    private Shape retrieveShape(int type, Vector2D position, Vector2D size) {
        switch(type) {
            case SHAPE_ARC:
                return new Arc2D.Float(position.getX(), position.getY(), size.getX(), size.getY(), 0, 180, Arc2D.CHORD);
            case SHAPE_LINE:
                return new Line2D.Float(position.getX(), position.getY(), size.getX(), size.getY());
            default:
                return new Rectangle2D.Float(position.getX(), position.getY(), size.getX(), size.getY());
        }
    }
    
    public float getWidth() {
        return this.size.getX();
    }
    
    public float getHeight() {
        return this.size.getY();
    }
    
    protected void manageBodiesCollision(Body b) {
//        if(!b.equals(this)) {
//            if(b.maxX() >= this.minX() && b.minX() <= this.maxX()) {
//                if(b.maxY() >= this.minY() && b.maxY() <= this.maxY()) {
//                    //System.err.println("Yo");
//                    //b.Vy = Math.abs(b.Vy) * bounciness;
//                    //b.addTempYForce(b.Fy);
//                    b.Vy = -b.Vy *bounciness;
//                    b.addTempYForce(b.Fy);
//                    //b.Fy = 0;
//                    //b.addTempYForce(b.Fy);
//                }
//            }
//        }
        if(!b.equals(this)) {
            if(b.maxX() >= this.minX() && (b.minX() - (b.maxX() - b.minX())/2) <= this.maxX()) {
                if((int)b.maxY() >= (int)this.minY() && (int)b.maxY() <= (int)this.maxY()) {
//                    b.Vy = -Math.abs(b.Vy) * floorBounciness;
//                    b.addTempYForce(-b.Fy);
                    b.setMaxY(this.minY());
                }
            }
        }
    }
    
    public void moveWithBack(float Vx) {
//        this.Vx = Vx;
    }
    
    public void refreshShape() {
        this.bodyShape = retrieveShape(type, position, size);
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(usesImage)
            g2.drawImage(bodyImage, (int)position.getX(), (int)position.getY(), c);
        else {
            Color defaultColor = g2.getColor();
            this.refreshShape();
                if(usesColor) {
                    g2.setColor(color);
                    g2.draw(bodyShape);
                    g2.fill(bodyShape);
                    g2.setColor(defaultColor);
                }
                else {
                    g2.draw(bodyShape);
                }
        }
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        g2.drawImage(bodyImage, at, c);
    }

    @Override
    public float minX() {
        return position.getX();
    }

    @Override
    public float maxX() {
        return position.getX() + size.getX();
    }

    @Override
    public float minY() {
        return position.getY();
    }

    @Override
    public float maxY() {
        return position.getY() + size.getY();
    }

    @Override
    public void setMaxX(float x) {
        position.setX(x - size.getX());
    }

    @Override
    public void setMaxY(float y) {
        position.setY(y - size.getY());
    }

    @Override
    public void setMinX(float x) {
        position.setX(x);
    }

    @Override
    public void setMinY(float y) {
        position.setY(y);
    }
    
    public static final int SHAPE_QUAD = 0;
    public static final int SHAPE_ARC = 1;
    public static final int SHAPE_LINE = 2;

    @Override
    protected boolean manageCollision(Body b) {
        return false;
    }

    @Override
    public Body copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}