package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.Paintable;
import geometryzombiesmayhem.Vector2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Renato Lui Geh
 */
public class PaintableShape<E extends Shape> implements Paintable {
    private Shape s;
    private boolean isActive = true;
    private Vector2D position;
    private boolean updates = true;
    private Color color = null;
    private boolean isInterface = false;
    
    public PaintableShape(E s, Vector2D position, Color color) {
        this(s, position); 
        this.color = color;
    }
    public PaintableShape(E s, Vector2D position) {
        this.s = s;
        this.position = position;
    }
    
    public void setInterface(boolean isInterface) {this.isInterface = isInterface;}
    public void setUpdates(boolean updates) {this.updates = updates;}

    private Shape updatePosition() {     
        AffineTransform at = new AffineTransform();
        at.translate(position.getX() - s.getBounds().getX(), position.getY() - s.getBounds().getY() - s.getBounds().getHeight());
        return at.createTransformedShape(s);
    }
    
    public void translate(float x, float y) {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        this.s = at.createTransformedShape(s);
    }
    
    public void lookAt(Vector2D direction) {
        Vector2D d = position.distanceVector(direction);
        float angle = (float) Math.atan(d.getY() / d.getX());

        if(d.getX() < 0)
            angle += Math.PI;
        
        this.rotate(angle);
    }
    public void rotate(float angle) {rotate(angle, 0, 0);}
    public void rotate(float angle, float x, float y) {
        AffineTransform at = new AffineTransform();
        at.rotate(angle, x, y);
        this.s = at.createTransformedShape(s);
    }
    
    public float getCenterX() {return (float)s.getBounds().getCenterX();}
    public float getCenterY() {return (float)s.getBounds().getCenterY();}
            
    public Shape getShape() {return s;}
    public E getElement() {return (E)s;}
    public void setShape(Shape s) {this.s = s;}
    public void setElement(E s) {this.s = s;}
    
    @Override
    public boolean isActive() {return isActive;}

    @Override
    public void setActive(boolean active) {this.isActive = active;}

    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}
    
    @Override
    public void paint(Graphics2D g2, Component c) {    
        if(isInterface) g2.translate(-GameFrame.translateX, GameFrame.translateY);
        Color def = g2.getColor();
        Color t = new Color(0,255,0,125);
        g2.setColor(color==null?t:color);
        g2.draw(updates? updatePosition() : this.s);
        g2.setColor(def);
        if(isInterface) g2.translate(GameFrame.translateX, -GameFrame.translateY);
    }

    @Override
    public boolean showOnlyOnEditor() {
        return true;
    }
}
