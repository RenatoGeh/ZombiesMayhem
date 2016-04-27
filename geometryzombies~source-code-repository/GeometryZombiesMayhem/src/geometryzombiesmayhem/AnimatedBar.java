package geometryzombiesmayhem;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An animated bar.
 * 
 * @author Renato Lui Geh
 */
public class AnimatedBar implements Paintable {
    private Vector2D position;
    private float[] imPos;
    private int n;
    
    private MutableImage buffer;
    private MutableImage[] images;
    private MutableImage current;
    private int width, height = width = 0;
    private float speed = 0;
    protected boolean isActive = true;
    
    private int index = 0;
    
    public AnimatedBar(Vector2D position, int width, int height, float speed, MutableImage... images) {
        this.images = images;
        this.speed = speed;
        
        this.width = width;
        this.height = height;
        
        this.position = position;
        
        for(Image im : images) 
            im = new javax.swing.ImageIcon(im).getImage();
        
        NO_ANIMATION_INDEX = images.length;
        
        init();
    }
    
    private void init() {        
        buffer = new MutableImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        
        current = images[index];
        
        n = width/current.getWidth(null);
        imPos = new float[n+2];
        
        for(int i=0;i<n+2;i++) {
            graph.drawImage(current, (i-1) * current.getWidth(null), 0, null);
            imPos[i] = (i-1) * current.getWidth(null);
        }

        graph.dispose();
    }
    
    private void update() {
        buffer = new MutableImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        
        if(index != NO_ANIMATION_INDEX) {
            for(int i=0;i<n+2;i++) {
                float foo;

                if(i != n+1)
                    foo = imPos[i+1] - current.getWidth(null);
                else
                    foo = imPos[0] - current.getWidth(null);

                imPos[i] = ((imPos[i]>buffer.getWidth())? foo : imPos[i]);

                graph.drawImage(current, (int)imPos[i], 0, null);

                imPos[i] += speed;
            }      
        } else {
            for(int i=0;i<n+2;i++) {
                graph.drawImage(current, (int)imPos[i], 0, null);
            }
        }
        
        graph.dispose();
    }
    
    public void managePainting(Graphics2D g2) {
        update();
        g2.drawImage(buffer, null, (int)position.getX(), (int)position.getY());
        
        Color defColor = g2.getColor();
        Stroke defStroke = g2.getStroke();
        
        BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        
        g2.setStroke(stroke);
        g2.setColor(Color.BLUE.brighter());
        
        g2.drawRect((int)position.getX(), (int)position.getY(), width, height);
        
        g2.setStroke(defStroke);
        g2.setColor(defColor);
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        managePainting(g2);
    }
    
    public void setIndex(int index) {if(index<=images.length && this.index != index){ this.index = index; refresh();}}
    public int getIndex() {return index;}
    
    public void refresh() {init();}
    
    private final int NO_ANIMATION_INDEX;

    @Override
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}