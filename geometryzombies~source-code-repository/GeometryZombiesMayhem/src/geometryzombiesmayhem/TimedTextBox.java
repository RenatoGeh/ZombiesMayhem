package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.TimeUnit;

/**
 * <code>TextBox</code> + <code>AwesomeTimer</code>
 * 
 * @see TextBox
 * @see AwesomeTimer
 * 
 * @author Renato Lui Geh
 */
public class TimedTextBox extends TextBox {
    private boolean isVisible = false;
    private long delay;
    private TimeUnit unit;
    private long eraseDelay = 0;

    public TimedTextBox(Vector2D position, String text, Color color, int alpha, int style, int size, int maxChars, long delay, TimeUnit unit, boolean triggerNow) {
        super(position, text, color, alpha, style, size, maxChars);
        
        this.delay = delay;
        this.unit = unit;
        
        if(triggerNow)
            trigger();
    }
    
    public TimedTextBox(Vector2D position, String text, Color color, int alpha, Font f, int maxChars, long delay, TimeUnit unit, boolean triggerNow) {
        super(position, text, color, alpha, f, maxChars);
        
        this.delay = delay;
        this.unit = unit;
        
        if(triggerNow)
            trigger();
    }

    private void trigger() {
            AwesomeTimer.remove(delayAction);
            AwesomeTimer.addAction(delayAction, delay, false, unit);
            if(eraseDelay>0)
                AwesomeTimer.addAction(new EraseAction(), eraseDelay, false);
            isVisible = true;
    }
    
    public void setEraseDelay(long eraseDelay, TimeUnit unit) {this.eraseDelay = unit.toNanos(eraseDelay);}
    public long getEraseDelay() {return eraseDelay;}
    
    public void stopErasing() {eraseDelay = 0;}
    
    public void activate() {this.trigger();}
    
    @Override
    public void paint(java.awt.Graphics2D g2, java.awt.Component c) {
        if(isVisible)
            handlePainting(g2, c);
    }
    
    private AwesomeAction delayAction = new AwesomeAction() {
        @Override
        public void run() {
            isVisible = false;
        }
    };
    
    private class EraseAction extends AwesomeAction {

        @Override
        public void run() {
            String s = TimedTextBox.this.text;
            s = s.substring(s.indexOf("\n")+1);
            TimedTextBox.this.text =s;
        }
        
    }
    
    public void print(String s) {addText(s); activate();}
    public void println(String s) {writeLine(s + '\n'); activate();}
    public String read() {return getText();}
}