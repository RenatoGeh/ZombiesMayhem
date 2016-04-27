package geometryzombiesmayhem;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>A Slider.</p>
 * 
 * @author Renato Lui Geh
 * @author Yan
 */
public class Slider extends ComponentP implements Paintable,IDable {
    
    private PaintableImage slider;
    
    private Vector2D nameDim;
    
    private String name;
    
    private Font font;
    
    private Color color;
    private Color fontColor;
    
    private double min, max, value;
    private DecimalFormat df = new DecimalFormat("#.##");
    
    private Rectangle2D slide;
    
    private Format format;
    
    protected Vector2D position;
    protected boolean isActive = true;
    
    private Set<ChangeListener> changeL = new HashSet<>();
    
    private static final Graphics graphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE).createGraphics();
    
    private final int id;
    
    public Slider(Vector2D position, String name, Font f, Color c, Color fontC, Format form, double min, double max, double defValue, Vector2D size, Tooltip t,int id) {
        this.name = name;
        this.font = f; this.format = form;
        this.color = c; this.fontColor = fontC;
        this.min = min; this.max = max;
        this.position = position; this.value = defValue;

        FontMetrics fm = graphics.getFontMetrics(f);

        nameDim = new Vector2D(fm.stringWidth(name), fm.getHeight());

        slide = new Rectangle2D.Float(position.getX() + nameDim.getX() + 10, position.getY() + nameDim.getY()/2, size.getX(), size.getY());
        
        bounds = new Rectangle2D.Float((float) slide.getX(), (float)slide.getY() - DEFAULT_SLIDER.getHeight(null)/3, size.getX(),DEFAULT_SLIDER.getHeight(null));

        slider = new PaintableImage(DEFAULT_SLIDER, "resources/interface/slider/slider.png", new Vector2D((float)(slide.getX() + (slide.getWidth() * value)/max),(float)slide.getY() - DEFAULT_SLIDER.getHeight(null)/3));
        
        this.setTooltip(t);
        this.id = id;
     }
    
    public Slider(Vector2D position, String name, Font f, Color c, Color fontC, Format form, double min, double max, double defValue, Vector2D size,int id) {
        this(position, name, f, c, fontC, form, min, max, defValue, size, null,id);
    }
    
    public Slider(Vector2D position, String name, Font f, Color c, Color fontC, Format form, double min, double max, Vector2D size, int id) {
        this(position, name, f, c, fontC, form, min, max, min, size,id);
    }


    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        Color defColor = g2.getColor();
        Font defFont = g2.getFont();
        
        g2.setFont(font);
        g2.setColor(fontColor);
        
        g2.drawString(name, position.getX(), position.getY() + nameDim.getY());
        
        g2.setFont(font.deriveFont(font.getSize2D()/2));
        
        if(format == Format.Precise) {
            g2.drawString(Double.toString(min), (float)slide.getX(), (float)(slide.getY() + slide.getHeight() + g2.getFont().getSize2D()));
            g2.drawString(Double.toString(max), (float)(slide.getX() + slide.getWidth() - g2.getFontMetrics().stringWidth(Integer.toString((int)max))), (float)(slide.getY() + slide.getHeight()+ g2.getFont().getSize2D()));
            
            g2.drawString(df.format(value), slider.getX(), slider.getY() + slider.getHeight() + g2.getFontMetrics().getHeight());
        } else {
            g2.drawString(Integer.toString((int)min), (float)slide.getX(), (float)(slide.getY() + slide.getHeight() + g2.getFont().getSize2D()));
            g2.drawString(Integer.toString((int)max), (float)(slide.getX() + slide.getWidth() - g2.getFontMetrics().stringWidth(Integer.toString((int)max))), (float)(slide.getY() + slide.getHeight()+ g2.getFont().getSize2D()));
            
            g2.drawString(Integer.toString((int)value), slider.getX() + g2.getFontMetrics().stringWidth(Integer.toString((int)value))/4, slider.getY() + slider.getHeight() + g2.getFontMetrics().getHeight());
        }
        
        g2.setColor(color);
        g2.draw(slide);
        g2.setColor(color.brighter().brighter());
        g2.fill(slide);
        g2.drawImage(slider, null, slider.getX(), slider.getY());
        
        g2.setColor(defColor);
        g2.setFont(defFont);
    }

    @Override
    protected void processMouseEvent(MouseEvent event) {
        super.processMouseEvent(event);
        
        checkSlider(event);
    }
    
    private void checkSlider(MouseEvent e) {
        if(e.getButton()!=MouseEvent.BUTTON1)return;
        if(e.getID() == MouseEvent.MOUSE_PRESSED) {
            if(slider.getBounds().contains(e.getX(), e.getY())) {
                
                window.gA.add(ml);
                isAdded = true;
            }
        }
        
        else if(e.getID() == MouseEvent.MOUSE_CLICKED &&  bounds.contains(e.getX(),e.getY())) {
            setX(e.getX());
        }
    }
    private boolean isAdded = false;
    private MouseAdapter ml = new MouseAdapter() {        
        @Override
        public void mouseDragged(MouseEvent e) {
            setX(e.getX());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // can't remove instantly or ConcurrentModificationException is thrown
            AwesomeTimer.addAction(new AwesomeAction() {
                @Override
                public void run() {
                    window.gA.remove(ml);
                    isAdded = false;
                }
            }, 10, false);
            
        }
    };
    
    private void setX(float x) {
        x = (float) Math.max(x, slide.getX());
        x = (float) Math.min(x, slide.getX() + slide.getWidth());
        slider.setPosition(x-slider.getWidth()/2, slider.getY());
        value = min + (max-min)*(x-slide.getX())/slide.getWidth();
        
        ChangeEvent event = new ChangeEvent(this);
        for(ChangeListener cl : changeL)
            cl.stateChanged(event);
    }
    
    public void addChangeListener(ChangeListener listener) { changeL.add(listener); }
    public void removeChangeListener(ChangeListener listener) { changeL.remove(listener); }
    
    public double getValue() {return ((format == Format.Integer)? (int)value : value);}

    @Override
    public int getID() {
        return id;
    }
    
    @Override
    public void reset() {this.value = (max-max)/2;}

    @Override
    public void updateWindowDefaultPosition() {
        position.add(window.x, window.y);
    }
    
    public static enum Format {Integer, Precise}
    
    private static final Image DEFAULT_SLIDER = AssetManager.loadImage("resources/interface/slider/slider.png");
}
