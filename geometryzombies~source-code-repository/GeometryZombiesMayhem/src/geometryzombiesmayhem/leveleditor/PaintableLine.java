/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.Paintable;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 *
 * @author Yan
 */
public class PaintableLine implements Paintable{
    Line2D line;
    private boolean active = true;
    private boolean isInterface = false;
    
    private static final Stroke stroke;
    
    static {
        float[] dash = { 7.5f };
        stroke = new BasicStroke(2.5f, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
    }

    public void setInterface(boolean isInterface) {this.isInterface = isInterface;}
    
    public Line2D getLine() {
        return line;
    }
    
    public PaintableLine(Point s,Point e) {
        line = new Line2D.Float(s, e);
    }
    
    public void updateEnd(Point e) {
        line = new Line2D.Float(line.getP1(), e);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void paint(Graphics2D g2, Component c) {
        if(isInterface) g2.translate(-GameFrame.translateX, GameFrame.translateY);
        Color def = g2.getColor();
        Color t = new Color(0,255,0,125);
        Stroke st = g2.getStroke();
        g2.setStroke(stroke);
        g2.setColor(t);
        g2.draw(line);
        g2.setColor(def);
        if(isInterface) g2.translate(GameFrame.translateX, -GameFrame.translateY);
    }

    @Override
    public boolean showOnlyOnEditor() {
        return true;
    }
}
