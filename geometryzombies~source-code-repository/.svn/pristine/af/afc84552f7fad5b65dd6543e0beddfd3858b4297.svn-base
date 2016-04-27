package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Tooltip box and text</p>
 * 
 * <p>
 *  Mainly created for easy implementations of in-game tooltips.
 * </p>
 * 
 * <b>
 * <p> This class now uses TextBox as the main component. Please check <code>TextBox</code>.</p>
 * </b>
 * 
 * <p>
 *  This class provides all the utilities for a tooltip-like box with text.
 *  It allows you to choose the position on-screen, the text to show,
 *  a basic color from Java AWT's Color class as well as an alpha feature. The class now
 *  allows defining the amount of delay before the tooltip appears on the screen.
 * </p>
 * 
 * <p>
 *  <strike>
 *   The class already provides a fixed number of maximum characters per line.
 *   Simply Override this class to change it. 
 *  </strike>
 *  <br/>
 *  The class now has defines the max number of chars from the TextBox
 *  constructor. You may have to re-construct the embedded TextBox to 
 *  achieve the desired effect.
 * </p> 
 * 
 * <p>
 *  The method formatText automatically
 *  formats the text procedurally to fit it into the box.
 * </p>
 * 
 * <p>
 *  Simply create an instance of the tooltip and call the paint method to
 *  draw it on the given component.
 * </p>
 * 
 * <p>
 *  <b> Note: </b>
 *  <strike>You will have to create a timer yourself if you wish to time the tooltip.</strike>
 *  <p>
 *   The class now has its own AWESOMETIMER FUCK YEAH. Just define a delay and it will do all by itself.
 *  </p>
 * </p>
 * 
 * @see Color
 * @see Vector2D
 * @see MultiMenu
 * @see TextBox
 * 
 * @author Renato Lui Geh
 * @author Yan
 */
public class Tooltip implements ActionListener, Paintable {
    private boolean isActive;
    private boolean createTooltip = false;
    private TextBox textBox;
    private int id = -1;
    private int delay;
    
    public Tooltip(Vector2D position, String text, Color color, int alpha, int delay, int id) {
        this(position, text, color, alpha, delay);
        this.id = id;
    }

    public Tooltip(Vector2D position, String text, Color color, int alpha, int delay) {
        textBox = new TextBox(position, text, color, alpha, Font.PLAIN, 12, 50);
        this.delay = delay;
    }
    
    public Tooltip(String text, Color color, int alpha, int delay, int id) {
        this(Vector2D.ZERO.copy(), text, color, alpha, delay, id);
    }
    
    public Tooltip(String text, Color color, int alpha, int delay) {
        this(Vector2D.ZERO.copy(), text, color, alpha, delay);
    }

    public void refreshPosition(Vector2D position) {
        textBox.refreshPosition(position);
    }
    
    public void refreshPosition(float x, float y) {
        textBox.refreshPosition(new Vector2D(x, y));
    }
    
    public void refreshPosition(Point p) {
        refreshPosition(new Vector2D(p));
    }
    
    public void startTooltip() {
        AwesomeTimer.addAction(tAction, delay, false, TimeUnit.MILLISECONDS);
    }
    
    private AwesomeAction tAction = new AwesomeAction () {

        @Override
        public void run() {
            createTooltip = true;
        }
        
    };
    
    public int getID() {return id;}
    
    public void stopTooltip() {
        createTooltip = false;
        AwesomeTimer.remove(tAction);
    }
    
    @Override
    public void paint(java.awt.Graphics2D g2, java.awt.Component c) {
        if(createTooltip) 
            textBox.paint(g2, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createTooltip = true;
    }

    public boolean isVisible() {return createTooltip;}
    
    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}