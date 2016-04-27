/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.Paintable;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Yan
 */
public class PaintableRectangle implements Paintable{

    private boolean active = true;
    private Rectangle2D.Float rec;
    private Point s;
    private boolean isInterface = false;
    
    public PaintableRectangle(Point s,Point e) {
        rec = new Rectangle2D.Float(s.x, s.y, e.x-s.x, e.y-s.y);
        this.s = s;
    }
    
    public void setInterface(boolean isInterface) {this.isInterface = isInterface;}
    
    public void updateEnd(Point e) {
        if(e.x<s.x) rec.x = e.x;
        else rec.x = s.x;
        if(e.y<s.y) rec.y = e.y;
        else rec.y = s.y;
        rec.width = Math.abs(e.x - s.x);
        rec.height = Math.abs(e.y - s.y);
    }

    public Rectangle2D getRectangle() {
        return rec;
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
        g2.draw(rec);
        g2.setColor(def);
        if(isInterface) g2.translate(GameFrame.translateX, -GameFrame.translateY);
    }
    
    private static final Stroke stroke;
    
    static {
        float[] dash = { 7.5f };
        stroke = new BasicStroke(2.5f, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
    }

    @Override
    public boolean showOnlyOnEditor() {
        return true;
    }
}
