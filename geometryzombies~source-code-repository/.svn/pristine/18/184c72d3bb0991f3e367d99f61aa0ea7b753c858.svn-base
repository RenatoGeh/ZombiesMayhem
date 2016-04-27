package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * <p>A <code>Paintable</code> <code>Image</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class PaintableImage extends MutableImage implements Paintable,Serializable {
    private boolean isActive = true;
    private Vector2D position;
    
    public static PaintableImage get(String path, Vector2D position) {
        return new PaintableImage(AssetManager.loadRawImage(path),path,position);
    }
    
    public static PaintableImage get(PIData d) {
        PaintableImage pi;
        if(d.path.equals("")) {
            pi = new PaintableImage(d.i.getImage(),"",d.pos);
        }else {
            pi = new PaintableImage(AssetManager.loadRawImage(d.path),d.path,d.pos);
            if(d.w!=pi.getWidth() || d.h != pi.getHeight()) System.out.println("FUUUUUU"); //Paintableimage resize???
        }
        pi.isActive = d.active;
        if(d.onP) Paintables.register(pi);
        if(d.onI) Paintables.registerInterface(pi);
        return pi;
    }
    

    public PaintableImage(int width, int height, int imageType, Vector2D position) {
        super(width, height, imageType);
        
        this.position = position;
    }
    
    public PaintableImage(Image im, String path, Vector2D position) {
        super(im,path);
        
        this.position = position;
    }
    
    public PaintableImage(Image im, String path, Vector2D position, boolean tracked) {
        super(im, path, tracked);
        
        this.position = position;
    }
    
    public final void drawImage(Image im, Vector2D pos) {
        Graphics2D graph = this.createGraphics();
        GameSettings.Graphics.apply(graph);
        
        graph.drawImage(im, (int)pos.getX(), (int)pos.getY(), null);
        
        graph.dispose();
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
    
    private static class PIData{
        String path;
        int w,h;
        Vector2D pos;
        ImageIcon i;
        boolean active,onP,onI;
        
        public PIData(PaintableImage pi) {
            path = pi.path;
            pos = pi.position;
            active = pi.isActive;
            onP = Paintables.contains(pi);
            onI = Paintables.containsInterface(pi);
            if(path.equals("")) {
                w = h = 0;
                i = new ImageIcon(pi.defImage);
            } else {
                i = null;
                w = pi.getWidth();
                h = pi.getHeight();
            }
        }
        
        private Object readResolve() throws ObjectStreamException {
            return PaintableImage.get(this);
        }
        
        private static final long serialVersionUID = 270819950021122012L;
    }
    private static final long serialVersionUID = 1123581321355691L;
    
    @Override
    public Image getImage() {return ImageManipulator.convert(this);}
    public Vector2D getPosition() {return position;}
    
    public void setPosition(Vector2D position) {this.position = position;}
    public void setPosition(float x, float y) {this.position.set(x, y);}
    
    public Rectangle2D getBounds() {
        return new Rectangle2D.Float(position.getX(), position.getY(), this.getWidth(), this.getHeight());
    }
    
    public int getX() {return (int)position.getX();}
    public int getY() {return (int)position.getY();}
    
    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}
    @Override
    public void paint(Graphics2D g2, Component c) {g2.drawImage(this, null, (int)position.getX(), (int)position.getY());}
}
