/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Yan
 */
public class Detector extends OutputBody {

    private float voltage;


    public enum Type {
        PLAYER, PROJECTILE, ZOMBIE;
    }
    private Type type;
    private Rectangle2D b;
    private boolean aux = false, touchingPlayer = false;

    public Detector() {super();}
    
    public Detector(Rectangle2D bounds, Type type) {
        super(new Vector2D(bounds.getMinX(), bounds.getMinY()));
        this.interactible = true;
        int[] a = {Body.ID_BOUND, Body.ID_ITEM, Body.ID_WARP};
        this.doNotCheckCollision = a;
        this.id = 666;
        this.checkPriority = 3;
        this.type = type;
        this.managePriority = 5;
        this.b = bounds;
    }

    @Override
    protected void update(long deltaN) {
        super.update(deltaN);
        if (!aux) {
            aux = true;
        } else {
            touchingPlayer = false;
        }

        if (!touchingPlayer && voltage > 0) {
            
            voltage = 0;
            for (Input in : inputs) {
                in.updateVoltage(this,0);
            }
        }
    }

    @Override
    protected boolean manageCollision(Body b) {

        if ((type == Type.PLAYER && b.getID() == Body.ID_PLAYER) || (type == Type.PROJECTILE && b.getID() == Body.ID_PROJECTILE) || (type == Type.ZOMBIE && b.getID() == Body.ID_ZOMBIE)) {
            aux = false;
            touchingPlayer = true;
            if (voltage < 1) {
                voltage = 1;
                for (Input in : inputs) {
                    in.updateVoltage(this, 1);
                }
            }
        }
        return true;
    }

    @Override
    public void paintThis(Component c, Graphics2D g2) {
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
    }

    @Override
    public float minX() {
        return (float) b.getMinX();
    }

    @Override
    public float maxX() {
        return (float) b.getMaxX();
    }

    @Override
    public float minY() {
        return (float) b.getMinY();
    }

    @Override
    public float maxY() {
        return (float) b.getMaxY();
    }

    @Override
    public void setMaxX(float x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMaxY(float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMinX(float x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMinY(float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add(Input c) {
        super.add(c);
        if (voltage > 0) {
            c.updateVoltage(this, voltage);
        }
    }
    
    @Override
    public float currentVoltage() {
        return voltage;
    }
    
    @Override
    public Body copy() {
        return new Detector(b,type);
    }
    
    @Override
    public boolean contains(Point position) {
        for(Line2D line : this.getBounds().getSides())
            if(line.ptLineDist(position) <= 9)
                return true;
        return false;
    }
}
