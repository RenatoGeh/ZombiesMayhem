package geometryzombiesmayhem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>A Selector.</p>
 * 
 * @author Renato Lui Geh
 * @author Yan
 */
public class Selector extends ComponentP implements Paintable,IDable {
    private boolean isSelected = false;
    private boolean isEnabled = true;
    
    private String name;
    
    private Rectangle2D baseFrame;
    private Rectangle2D optionsFrame;
    private Rectangle2D selectorBounds;
    private Rectangle2D[] optionsBounds;
    
    private String[] options;
    private String current;
    
    private Vector2D nameDim;
    private Vector2D[] optionsDim;
    
    private Font font;
    private Color color;
    private Color fontColor;
    
    private Vector2D position;
    private boolean isActive;

    private Image selector = DEFAULT_SELECTOR;
    
    private Set<ChangeListener> changeL = new HashSet<>();
    private List<ActionListener> listeners = new ArrayList<>();
    
    private final int id;
    private float topWidth;
    
    public Selector(String name, Font f, Color c, Color fontC, Vector2D position, Tooltip t, int id,String... options) {

        this.name = name;
        
        this.position = position;
        this.options = options;
        
        current = options[0];
        
        font = f;
        fontColor = fontC;
        color = c;
        
        nameDim = getFontDimension(name);
        optionsDim = new Vector2D[options.length];
        
        for(int i=0;i<options.length;i++)
            optionsDim[i] = getFontDimension(options[i]);
        
        float width = 0, height = nameDim.getY() * options.length;
        
        for(Vector2D dim : optionsDim)
            width = ((dim.getX() > width)? dim.getX() : width);
        
        this.topWidth = width;
        
        optionsBounds = new Rectangle2D[options.length];
        
        for(int i=0;i<options.length;i++)
            optionsBounds[i] = new Rectangle2D.Float(nameDim.getX() + position.getX(),
                    position.getY() + ((i+1)*optionsDim[i].getY()) + 5,
                    width, nameDim.getY());
        
        baseFrame = new Rectangle2D.Float(nameDim.getX() + position.getX() + 5, position.getY() + 5, width, nameDim.getY());
        optionsFrame = new Rectangle2D.Float(nameDim.getX() + position.getX() + 5, position.getY() + nameDim.getY() + 5, width, height);
        
        bounds = selectorBounds = this.selectorBounds();
        
        this.setTooltip(tooltip);
        
        isActive = true;
        this.id = id;
    }
    
    public Selector(String name, Font f, Color c, Color fontC, Vector2D position, int id,String... options) {
        this(name, f, c, fontC, position, null, id, options);
    }
    
    public Selector(String name, Font f, Color c, Vector2D position, int id, String... options) {
        this(name, f, c, Color.BLACK, position, id, options);
    }
    
    public Selector(String name, Font f, Color c, Vector2D position, Tooltip t, int id, String... options) {
        this(name, f, c, Color.BLACK, position, t, id, options);
    }
    
    public Selector(String name, Vector2D position, int id, String... options) {
        this(name, new Font(Font.SANS_SERIF, Font.PLAIN, 20), ColorFactory.getRandomColor(ColorFactory.Type.Integer, true), position, id, options);
    }
    
    private Vector2D getFontDimension(String text) {
        FontMetrics fm = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE).createGraphics().getFontMetrics(font);

        float height = fm.getHeight();
        float width = fm.stringWidth(text);
        
        return new Vector2D(width, height);
    }

    private void updateRectangle(Vector2D translation, Rectangle2D... rects) {
        for(Rectangle2D rect : rects)
            rect.setRect(rect.getX() + translation.getX(), rect.getY() + translation.getY(),
                    rect.getWidth(), rect.getHeight());
    }
    
    private void updateBounds() {
        Vector2D translate = new Vector2D(this.bounds.getBounds2D().getX() - this.selectorBounds.getX(), 
                this.bounds.getBounds2D().getY() - this.selectorBounds.getY());
        
        this.updateRectangle(translate, this.baseFrame, this.optionsFrame, this.selectorBounds);
        this.updateRectangle(translate, this.optionsBounds);
        
        this.position.addLocal(translate);
    }
    
    public void setColor(Color c) {this.color = c;}
    public void setFontColor(Color c) {this.fontColor = c;}
    
    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}
    
    public void addActionListener(ActionListener al) {this.listeners.add(al);}
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        updateBounds();
        
        Stroke defStroke = g2.getStroke();
        Font defFont = g2.getFont();
        Color defColor = g2.getColor();

        g2.setFont(font);
        g2.setColor(color);
        g2.setStroke(Selector.DEFAULT_STROKE);
        
        g2.draw(selectorBounds);
        
        g2.fill(baseFrame);
        g2.setColor(Color.BLACK);
        g2.draw(baseFrame);
        
        g2.setColor(fontColor);
        g2.drawString(name, position.getX(), position.getY() + nameDim.getY());
        g2.drawString(current, position.getX() + nameDim.getX() + 5, position.getY() + nameDim.getY());
        g2.setColor(color);
        
        g2.drawImage(selector, (int)(position.getX() + nameDim.getX() + 5 + topWidth), (int)position.getY() + 5, null);
        
        if(isSelected) {
            g2.setColor(Color.BLACK);
            g2.draw(optionsFrame);            
            g2.setColor(color);
            g2.fill(optionsFrame);
            
            g2.setColor(fontColor);
            
            for(int i=0;i<options.length;i++)
                g2.drawString(options[i], nameDim.getX() + position.getX() + 5, position.getY() + 2*nameDim.getY() + (i*optionsDim[i].getY()));
        }
        
        g2.setFont(defFont);
        g2.setColor(defColor);
        
        
        if(!isEnabled)
            g2.drawLine((int)position.getX(), (int)(position.getY() + baseFrame.getHeight()/2 + nameDim.getY()/4),
                        (int)(position.getX() + baseFrame.getWidth()), (int)(baseFrame.getHeight()/2 + position.getY() + nameDim.getY()/4));
    
        g2.setStroke(defStroke);
    }
    
    private Rectangle2D selectorBounds() {return new Rectangle2D.Float(position.getX() + nameDim.getX() + 5 + topWidth, position.getY() + 5, selector.getWidth(null), selector.getHeight(null));}
    
    public void addChangeListener(ChangeListener listener) { changeL.add(listener); }
    public void removeChangeListener(ChangeListener listener) { changeL.remove(listener); }
    
    @Override
    public void reset() {this.current = options[0];}
    
    protected void checkButton(MouseEvent e) {        
        if(!isSelected && e.getID() == MouseEvent.MOUSE_PRESSED) {
            if(this.selectorBounds.contains(e.getX(), e.getY())) {
                this.setSelected(true);
                this.window.gA.add(mA);
            }
        }
        if(isSelected){
            int n = -1;

            for(int i=0;i<optionsBounds.length;i++) 
                if(optionsBounds[i].contains(e.getX(), e.getY()))
                    n = i;

            if(n != -1){
                if(e.getID() == MouseEvent.MOUSE_RELEASED) {
                    if(current == null ? options[n] == null : current.equals(options[n])) return;
                    this.current = options[n];
                    ChangeEvent event = new ChangeEvent(this);

                    for(ChangeListener cl : changeL)
                        cl.stateChanged(event);

                    for(ActionListener al : listeners)
                        al.actionPerformed(new ActionEvent(this, this.id, current));

                    AwesomeTimer.addAction(new AwesomeAction() {
                        @Override
                        public void run() {
                            window.gA.remove(mA);
                            aux = false;
                            setSelected(false);
                        }
                    }, 0, false);
                }
            } if(n==-1) {
                if(e.getID() == MouseEvent.MOUSE_PRESSED) {
                    if(!aux) { aux = true; return; }
                    AwesomeTimer.addAction(new AwesomeAction() {
                            @Override
                            public void run() {
                                window.gA.remove(mA);
                                aux = false;
                                setSelected(false);
                            }
                        }, 0, false);
                }
            }
        }
    }
    private boolean aux = false;
    
    private MouseAdapter mA = new MouseAdapter(){
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            checkButton(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            checkButton(e);
        }
    };
    
    @Override
    protected void processMouseEvent(MouseEvent event) {
        super.processMouseEvent(event);
        if(!isSelected) checkButton(event);
    }
    
    public String getSelected() {return current;}
    public String getOption(int index) {return options[index];}
    
    public boolean isSelected() {return isSelected;}
    public void setSelected(boolean isSelected) {this.isSelected = isSelected;}
    
    public void setSelector(Image im) {this.selector = im;}
    public Image getSelector() {return selector;}
    
    @Override
    public void setEnabled(boolean enabled) {this.isEnabled = enabled;}
    @Override
    public boolean isEnabled() {return isEnabled;}
    
    private static final Image DEFAULT_SELECTOR = AssetManager.loadImage("resources/interface/selector/select.png");

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void updateWindowDefaultPosition() {
        position.add(window.x, window.y);
    }
    
    private static final Stroke DEFAULT_STROKE = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
}
