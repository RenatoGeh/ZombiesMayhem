/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.*;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Yan
 */
public class Battery extends OutputBody {
    float voltage;
    MutableImage i;
    
    public Battery() {this(1,new Vector2D(0,0));}
    
    public Battery(float voltage,Vector2D pos) {
        super(pos);
        this.voltage = voltage;
        i = BATTERY;
        Rectangle2D b = new Rectangle2D.Float(pos.getX(), pos.getY(), 46, 60);
        this.bounds = new RotatableRectangle(b,0);
        if(voltage == 0 || voltage == -0) throw new UnsupportedOperationException("Can't be done, mate");
    }
    public Battery(Vector2D pos) { this(1,pos); }
    

    @Override
    public void add(Input c) {
        c.connected(this);
        c.updateVoltage(this, voltage);
    }

    @Override
    public void remove(Input c) {
        c.disconnected(this);
    }

    @Override
    public float currentVoltage() {
        return voltage;
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(GameFrame.onEditor)
            g2.drawImage(i, (int)position.getX(),(int)position.getY(), c);
    }
    
    private static MutableImage BATTERY = AssetManager.loadImage("resources/items/battery.png").resize(0.1f, 0.1f);

    @Override
    public Body copy() {
        return new Battery(voltage,position);
    }
}
