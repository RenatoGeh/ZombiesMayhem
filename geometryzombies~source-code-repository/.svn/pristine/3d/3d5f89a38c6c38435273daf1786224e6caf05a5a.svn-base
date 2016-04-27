/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.*;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Yan
 */
public abstract class CircuitryBody extends Body implements Circuitry {
    public CircuitryBody() {this(Vector2D.ZERO);}
    
    public CircuitryBody(Vector2D position) {
        super(0,position.getX(),position.getY(),0,0);
        this.boundsAffected = this.interactible = this.neverDown = this.frameBoundsAffected = false;
        this.id = Body.ID_CIRCUITRY;
    }
    
    @Override
    protected boolean manageCollision(Body b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float minX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float maxX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float minY() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float maxY() {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
