package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * <p>A mutable variant of <code>InputField</code> that works the same way as a <code>MutantLabel</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class MutableInputField extends InputField {
    private final Object e;
    private final String initial;
    private boolean isFocused = false;
    
    private int decimal = 2;
    
    public MutableInputField(Vector2D position, Vector2D size, Object e) {this(position, size, "", e);}
    public MutableInputField(Vector2D position, Vector2D size, String text, Object e) {
        super(position, size, "");
        
        this.initial = text;
        this.e = e;
    }
    
    @Override
    protected String getText() {
        return e.toString();
    }
    
    @Override
    public void manageFocus() {
        super.manageFocus();
        this.isFocused = true;
    }
    @Override
    public void manageUnFocus() {
        super.manageUnFocus();
        this.isFocused = false;
    }
    
    public void setDecimalAccuracy(int decimal) {
        if(!(e instanceof Vector2D))
            throw new IllegalArgumentException("Inner object isn't a Vector2D!");
        
        this.decimal = decimal;
    }
    
    @Override
    public void processKeyEvent(KeyEvent event) {
        if(super.getText().length() == this.initial.length())
            if(event.getKeyChar() == '\b')
                return;
        
        super.processKeyEvent(event);
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        if(!isFocused) {
            String s = e instanceof Vector2D? ((Vector2D)e).toFormattedString(decimal) : e.toString();
            this.setText(initial + s);
            this.update();
        }
        
        super.paint(g2, c);       
    }
}
