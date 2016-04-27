package geometryzombiesmayhem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.TimeUnit;

/**
 * <p>A single-lined text input <code>ComponentP</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class InputField extends ComponentP implements Paintable, IDable {
    private MutableImage frame;
    private String text;
    private Font textFont = DEFAULT_FONT;
    private Color textColor = Color.BLACK;
    
    private java.util.List<ActionListener> listeners = new java.util.ArrayList<>();
    
    private Color frameColor = Color.GRAY;
    
    private float xOffset = 0;
    private float yOffset;
    
    private FontMetrics metrics;
    private Caret caret;
    
    private boolean unfocusIntended = false;
    private boolean isEnabled = true;
    
    private int id = 0;
    
    public InputField(Vector2D position, Vector2D size, String text, int id) {
        this(position, size, text);
        this.id = id;
    }
    
    public InputField(Vector2D position, Vector2D size, String text) {        
        this.frame = new MutableImage((int)size.getX(), (int)size.getY(), MutableImage.TYPE_INT_ARGB_PRE);
        this.bounds = new Rectangle2D.Float(position.getX(), position.getY(), size.getX(), size.getY());
        
        this.text = text;

        this.metrics = ZM.GRAPHICS.getFontMetrics(textFont);
        
//        float n = textFont.getSize()*(size.getY())/metrics.getHeight();
        float n = textFont.getSize2D()*size.getY()/metrics.getHeight();
        textFont = new Font(Font.DIALOG, Font.PLAIN, (int)n);
        
        this.metrics = ZM.GRAPHICS.getFontMetrics(textFont);
        
        yOffset = size.getY();
        
        this.update();
        
        caret = new Caret();
    }
    
    public void setSize(float pixels) {
        this.metrics = ZM.GRAPHICS.getFontMetrics(textFont);
        
        float n = textFont.getSize()*pixels/metrics.getHeight();
        textFont = new Font(Font.DIALOG, Font.PLAIN, (int)n);
        
        yOffset = pixels;
        
        this.metrics = ZM.GRAPHICS.getFontMetrics(textFont);
        
        this.update();
    }
    
    protected String getText() {return this.text;}
    @Override
    public boolean isEnabled() {return this.isEnabled;}
    @Override
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        this.update();
    }
    
    public static int getPoints(float pixels, Font f) {       
        return (int)(f.getSize()*pixels/ZM.GRAPHICS.getFontMetrics(f).getHeight());
    }
    
    public void addActionListener(ActionListener listener) {listeners.add(listener);}
    public void removeActionListener(ActionListener listener) {listeners.remove(listener);}
    
    protected final void update() {
        this.frame.reset();
        
        Graphics2D graph = frame.createGraphics();

        graph.setFont(textFont);
        graph.setColor(this.isEnabled?textColor:Color.DARK_GRAY);
        graph.drawString(this.text, xOffset, 3*yOffset/4);
        
        graph.dispose();
    }
    
    @Override
    public void manageFocus() {if(isEnabled) caret.setReady(true);}
    @Override
    public void manageUnFocus() {
        if(!isEnabled) return;
        caret.setReady(false); 
        this.caret.setActive(false);
        if(unfocusIntended)
            for(ActionListener list : listeners)
                list.actionPerformed(new java.awt.event.ActionEvent(this, this.id, getText()));
        unfocusIntended = false;
    }
        
    @Override
    public void processKeyEvent(KeyEvent event) {
        if(!isEnabled) return;
        super.processKeyEvent(event);
        if(event.getID() == KeyEvent.KEY_TYPED) {
            char key = event.getKeyChar();

            if(event.isActionKey() || key == 27) return; //Action Key or ESCape.
            if(key == 10) {this.unfocusIntended = true; this.window.forceUnfocus();}
            
            if(key == '\b')
                if(!text.isEmpty()) {                    
                    text = text.substring(0, text.length() - 1);
                    
                    if(text.length() > 4)
                        if(this.metrics.stringWidth(text) - this.metrics.stringWidth(text.substring(text.length() - 2, text.length())) < xOffset)
                            xOffset += metrics.charWidth(metrics.charWidth(text.charAt(text.length() - 1)));
                    
                    this.update();
                    
                    return;
                }
            
            this.text += key;
            
            if(metrics.stringWidth(this.text) + metrics.charWidth(key) > this.frame.getWidth())
                xOffset -= metrics.charWidth(metrics.charWidth(key));
                
            this.update();
        }
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        Color defColor = g2.getColor();
        
        g2.setColor(frameColor);
        
        g2.fill(bounds);
        g2.drawImage(frame, null, this.getX(), this.getY());
        
        g2.setColor(defColor);
        
        this.caret.paint(g2, c);
    }
    
    @Override
    public void updateWindowDefaultPosition() {}

    protected void setText(String text) {this.text = text;}
    
    @Override
    public boolean isActive() {return true;}
    @Override
    public void setActive(boolean active) {}
    
    private static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);

    @Override
    public int getID() {return this.id;}

    private class Caret implements Paintable {
        private Line2D caret;
        private Stroke style;
        private boolean isActive = false;
        
        public Caret() {this(new BasicStroke(1));}
        public Caret(Stroke style) {
            this.style = style;
            
            float x = InputField.this.metrics.stringWidth(text) - InputField.this.xOffset;
            caret = new Line2D.Float(x + InputField.this.getX(), 
                    InputField.this.getY() + (InputField.this.getHeight() - InputField.this.yOffset)/2, 
                    x + InputField.this.getX(), 
                    InputField.this.getY() + InputField.this.getHeight());
        }
        
        private void update() {
            float x = InputField.this.metrics.stringWidth(text) - InputField.this.xOffset;
            caret.setLine(x + InputField.this.getX(), 
                    InputField.this.getY() + (InputField.this.getHeight() - InputField.this.yOffset)/2, 
                    x + InputField.this.getX(), 
                    InputField.this.getY() + InputField.this.getHeight());
        }
        
        @Override
        public boolean isActive() {return isActive;}
        @Override
        public void setActive(boolean active) {this.isActive = active;}
        
        protected void setReady(boolean ready) {
            if(ready) {
                AwesomeTimer.addAction(actionListener, 500, true, TimeUnit.MILLISECONDS);
            } else
                AwesomeTimer.remove(actionListener);
        }

        @Override
        public void paint(Graphics2D g2, Component c) {
            if(!isActive)
                return;
            
            this.update();
            Stroke defStroke = g2.getStroke();
            g2.setStroke(style);
            g2.draw(caret);
            g2.setStroke(defStroke);
        }
        
        private AwesomeAction actionListener = new AwesomeAction() {
            @Override
            public void run() {Caret.this.setActive(!Caret.this.isActive());}
        };
        
        @Override
        public boolean showOnlyOnEditor() {
            return false;
        }
    }
}
