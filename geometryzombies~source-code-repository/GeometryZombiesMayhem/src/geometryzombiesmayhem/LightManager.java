package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Handles everything related to lightning effects.</p>
 * 
 * @author Renato Lui Geh
 */
public final class LightManager {
    private static final List<LightSource> lights = new ArrayList<>();
    
    private static final Area DEFAULT_AMBIENT = new Area(new Rectangle2D.Float(0, 0, ZM.gameFrame.getWidth(), ZM.gameFrame.getHeight()));
    private static final Area AMBIENT = (Area)DEFAULT_AMBIENT.clone();
    
    private static Color backColor = ColorFactory.getColor(Color.BLACK, 0);
    
    public static synchronized void register(LightSource source) {lights.add(source);}
    public static synchronized void remove(LightSource source) {lights.remove(source);}
    
    public static synchronized void removeAll() {lights.clear();}
    
    public static synchronized boolean contains(LightSource source) {return lights.contains(source);}
    public static synchronized List<LightSource> getSources() {return lights;}
    
    private static final AffineTransform at = new AffineTransform();
    
    public synchronized static void paint(Graphics2D g2, Component c) {
        synchronized(lights) {
            if(backColor.getAlpha() == 0) return;
            
            at.translate(-GameFrame.translateX, GameFrame.translateY);
            
            Paint defPaint = g2.getPaint();
            
            for(Iterator it = lights.iterator();it.hasNext();) {                
                LightSource source = (LightSource)it.next();
                if(source instanceof LightSource.Blinkable)
                    if(source.isOff()) continue;
                
                Area area = source.getArea();
                area.transform(at);
                AMBIENT.subtract(area);

                at.translate(2*GameFrame.translateX, -2*GameFrame.translateY);
                area.transform(at);
//                Color defColor = g2.getColor();
                Area intersect = new Area();
//                for(int i=0;i<lights.indexOf(source);i++)
//                    if(lights.get(i)!=source) {          
//                        Area aux = new Area(area);
//                        aux.intersect(lights.get(i).getArea());
//                        intersect.add(aux);
//                    }
//                for(int i=0;i<lights.size();i++)
//                    if(lights.get(i)!=source) {
//                        Rectangle2D back = area.getBounds2D();
//                        Rectangle2D fore = lights.get(i).getBounds2D();
//                        double backArea = back.getWidth()*back.getHeight();
//                        double foreArea = fore.getWidth()*fore.getHeight();
//                        if(backArea < foreArea) {
//                            Area aux = new Area(area);
//                            aux.intersect(lights.get(i).getArea());
//                            intersect.add(aux);
//                        }
//                    }                
                //Primitive version of light source overlaying. Not 100% yet.
                for(int i=0;i<lights.size();i++) {
                    if(lights.get(i)==source) continue;
                    if(i<lights.indexOf(source)) {
                        Area aux = new Area(area);
                        aux.intersect(lights.get(i).getArea());
                        intersect.add(aux);
                    }
//                    Rectangle2D back = area.getBounds2D();
//                    Rectangle2D fore = lights.get(i).getBounds2D();
                    double backArea = source.getAreaValue();//back.getWidth()*back.getHeight();
                    double foreArea = lights.get(i).getAreaValue();//fore.getWidth()*fore.getHeight();
                    if(backArea < foreArea) {
                        Area aux = new Area(area);
                        aux.intersect(lights.get(i).getArea());
                        intersect.add(aux);
                    }
                }
                
//                for(int i=0;i<Body.getBodies().size();i++) {
//                    Body e = Body.getBodies().get(i);
//                    if(!(e instanceof Bound)) continue;
//                    if(!(e instanceof Bound.Simple)) continue;
//                    
//                    Bound.Simple bound = (Bound.Simple)e;
//                    Line2D boundLine = bound.getLine();
//                    java.util.ArrayList<Vector2D> pts = Vector2D.getPoints(source.getShape());
//                    Vector2D focus = source.getPosition();
//                }
                
                area.subtract(intersect);
//                for(LightSource inSource : lights)
//                    if(inSource != source) {
//                        intersect.intersect(inSource.getArea());
//                        area.subtract(inSource.getArea());
////                        g2.setColor(Color.YELLOW);
////                        g2.fill(intersect);
////                        area.subtract(intersect);
////                        g2.fill(intersect);
////                        Color defColor = g2.getColor();
////                        g2.setColor(Color.YELLOW);
////                        g2.draw(intersect);
////                        g2.setPaint(source.getPaint());
////                        g2.fill(intersect);
////                        g2.setColor(defColor);
////                        g2.setPaint(inSource.getPaint());
////                        g2.fill(intersect);
//                    }
////                  g2.setColor(Color.RED);
////                  g2.fill(area);
////                  g2.setColor(defColor);
////                for(Body b : Body.getBodies())
////                    if(b instanceof Bound) {
////                        
////                    }
////                Color defColor = g2.getColor();
////                g2.setColor(ColorFactory.getColor(backColor, 255-getDarkness()));
////                g2.draw(area);
////                g2.setColor(defColor);
                g2.setPaint(source.getPaint());
                g2.draw(area);
                g2.fill(area);
//                g2.draw(intersect);
//                g2.fill(intersect);
//                g2.draw(area);
                source.paintExtra(g2, c);
                at.translate(-2*GameFrame.translateX, 2*GameFrame.translateY);
            }
            
            g2.setPaint(defPaint);
            
            Color defColor = g2.getColor();
            g2.setColor(backColor);
            
            at.translate(2*GameFrame.translateX, -2*GameFrame.translateY);
            AMBIENT.transform(at);

            g2.fill(AMBIENT);
            
            LightManager.reset();
            
            g2.setColor(defColor);
        }
    }
    
    public static void setDarkness(int alpha) {backColor = ColorFactory.getColor(backColor, alpha);}
    public static void setColor(Color c) {backColor = c;}
    
    public static int getDarkness() {return backColor.getAlpha();}
    public static Color getColor() {return backColor;}
    
    private synchronized static void reset() {
        AMBIENT.reset();
        AMBIENT.add(DEFAULT_AMBIENT);
        at.setToIdentity();
    }
}
