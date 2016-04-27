package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.MutableImage;
import geometryzombiesmayhem.Player;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Renato Lui Geh
 */
public class Lever extends CircuitryBody implements Output {
    private float voltage, deviation;
    private boolean lastAction = false;
    private MutableImage im;
    private List<Input> inputs = new ArrayList<>();
    
    public Lever() {this(Vector2D.ZERO.copy());}
    public Lever(Vector2D position) {this(1, position);}
    public Lever(float deviation, Vector2D position) {this(deviation, deviation, position);}
    public Lever(float voltage, float deviation, Vector2D position) {
        super(position);    
        this.voltage = voltage;
        this.deviation = deviation;
        this.interactible = true;
        this.doNotCheckCollision = new int[]{Body.ID_BOUND, Body.ID_ITEM, Body.ID_PROJECTILE, Body.ID_WARP, Body.ID_ZOMBIE, Body.ID_CIRCUITRY};
        
        this.im = this.voltage==0?Lever.LEVER_OFF:Lever.LEVER_ON;
    }
    
    @Override
    protected boolean manageCollision(Body b) {
        boolean ready = ((Player)b).interactibleInRange;
        if((ready != lastAction) && ready)
            interact();
        this.lastAction = ready;
        return true;
    }
    
    private void toggle() {
        this.voltage=this.voltage==0?deviation:0;
        
        for(Input i : inputs)
            i.updateVoltage(this, voltage);
        
        this.im = this.voltage==0?Lever.LEVER_OFF:Lever.LEVER_ON;
    }
    
    @Override
    public void add(Input in) {
        this.inputs.add(in);
        for(Input i : inputs) i.updateVoltage(this, this.voltage);
    }
    
    @Override
    public void remove(Input in) {this.inputs.remove(in);}
    
    public void interact() {toggle();}
    
    @Override
    public float minX() {return this.position.getX();}
    @Override
    public float minY() {return this.position.getY();}
    @Override
    public float maxX() {return this.position.getX() + this.im.getWidth();}
    @Override
    public float maxY() {return this.position.getY() + this.im.getHeight();}
    
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        g2.drawImage(im, null, (int)position.getX(), (int)position.getY());
    }
    
    @Override
    public float currentVoltage() {return this.voltage;}
    
    private static final MutableImage LEVER_ON = AssetManager.loadImage("resources/circuitry/lever/lever_on.png");
    private static final MutableImage LEVER_OFF = AssetManager.loadImage("resources/circuitry/lever/lever_off.png");

    @Override
    public Body copy() {
        return new Lever(voltage,deviation,position);
    }
}
