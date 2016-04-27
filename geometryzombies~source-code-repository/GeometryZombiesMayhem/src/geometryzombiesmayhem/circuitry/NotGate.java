/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.RotatableRectangle;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Yan
 */
public class NotGate extends CircuitryBody implements Input,Output{
    float voltage = 0;
    Set<Input> ins = new HashSet<>();
    
    public NotGate(){this(Vector2D.ZERO.copy());};
    public NotGate(Vector2D pos) {
        super(pos);
        Rectangle2D b = new Rectangle2D.Float(pos.getX(), pos.getY(), 46, 60);
        this.bounds = new RotatableRectangle(b,0);
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Body copy() {
        return new NotGate(position);
    }
    
    @Override
    public float currentVoltage() {
        return 1-voltage;
    }

    @Override
    public void updateVoltage(Output o, float newVoltage) {
        voltage = newVoltage;
        update();
    }

    @Override
    public void connected(Output o) {
    }

    @Override
    public void disconnected(Output o) {
    }

    @Override
    public void add(Input c) {
        c.connected(this);
        ins.add(c);
        c.updateVoltage(this, 1-voltage);
    }

    @Override
    public void remove(Input c) {
        c.connected(this);
        ins.remove(c);
    }
    
    public void update() {
        float u = 1 - voltage;
        for(Input in: ins){
            in.updateVoltage(this, u);
        }
    }
    
}
