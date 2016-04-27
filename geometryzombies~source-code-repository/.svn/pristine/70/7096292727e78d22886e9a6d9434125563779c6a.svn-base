package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.MutableImage;
import geometryzombiesmayhem.Player;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;

/**
 * <p>A dimmer for any circuitry component.</p>
 * 
 * @author Renato Lui Geh
 */
public class Dimmer extends OutputBody {
    private float voltage;
    private float crementer;
    private MutableImage im;
    
    public Dimmer() {this(Vector2D.ZERO.copy());}
    public Dimmer(float voltage, float crementer, Vector2D position) {
        super(position);
        this.voltage = voltage;
        this.crementer = crementer/1000;
        this.interactible = true;
        this.doNotCheckCollision = new int[]{Body.ID_BOUND, Body.ID_ITEM, Body.ID_PROJECTILE, 
            Body.ID_WARP, Body.ID_ZOMBIE, Body.ID_CIRCUITRY};
        this.im = DIMMER_IMAGE;
    }
    public Dimmer(Vector2D position) {this(0, .05f, position);}
    
    @Override
    public void add(Input in) {
        this.inputs.add(in);
        in.connected(this);
        in.updateVoltage(this, this.voltage);
    }
    
    @Override
    public void remove(Input in) {this.inputs.remove(in);}
    
    private void decrement() {
        if(voltage <= 0) {voltage = 0; return;}
        voltage -= crementer;
        for(Input in : inputs) in.updateVoltage(this, voltage);
    }
    
    private void increment() {
        if(voltage >= 1) {voltage = 1; return;}
        voltage += crementer;
        for(Input in : inputs) in.updateVoltage(this, voltage);
    }
    
    @Override
    public boolean manageCollision(Body e) {
        if(((Player)e).interactibleInRange)
            dim();
        return true;
    }
    
    public void dim() {      
        if(GameFrame.mousePosition.getY() < this.position.getY() + this.im.getHeight()/2)
            increment();
        else
            decrement();
    }
    
    @Override
    public float currentVoltage() {return voltage;}
    
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
    
    private static final MutableImage DIMMER_IMAGE = AssetManager.loadImage("resources/circuitry/dimmer/dimmer.png");

    @Override
    public Body copy() {
        return new Dimmer(voltage,crementer*1000,position);
    }
}
