/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.*;

/**
 *
 * @author Yan
 */
public class Paintables {
    private final static ArrayList<Paintable> paintables = new ArrayList<>();
    private final static ArrayList<Paintable> interfaceP = new ArrayList<>();
    final static Set<Sprite> sprites = new HashSet<>();    
    private final static ArrayList<Paintable> theaterP = new ArrayList<>();
        
    public static void register(Paintable p) {synchronized(paintables) {paintables.add(p);} if(p instanceof Sprite) sprites.add((Sprite) p);}
    public static boolean contains(Paintable p) { synchronized(paintables) { return paintables.contains(p); } }
    public static void remove(Paintable p) {synchronized(paintables) {paintables.remove(p);}}
    
    public static void register(Paintable... p) {for(Paintable e : p) register(e);}
    public static void remove(Paintable... p) {for(Paintable e : p) remove(e);}
    
    public static void registerInterface(Paintable p) {synchronized(interfaceP) { interfaceP.add(p); }}
    public static boolean containsInterface(Paintable p) {synchronized(interfaceP) { return interfaceP.contains(p); }}
    public static void removeInterface(Paintable p) {synchronized(interfaceP) { interfaceP.remove(p);}}
    
    public static void registerInterface(Paintable... p) {for(Paintable e : p) registerInterface(e);}
    public static void removeInterface(Paintable... p) {for(Paintable e : p) removeInterface(e);}
    
    public static void registerTheater(Paintable p) {synchronized(interfaceP) { theaterP.add(p); }}
    public static void removeTheater(Paintable p) {synchronized(interfaceP) { theaterP.remove(p);}}
    
    public static void registerTheater(Paintable... p) {for(Paintable e : p) registerTheater(e);}
    public static void removeTheater(Paintable... p) {for(Paintable e : p) removeTheater(e);}
    
    public static void clear() {paintables.clear();}
    public static void clearInterface() {interfaceP.clear();}
    public static void clearTheater() {theaterP.clear();}
    public static void clearAll() {clear(); clearInterface(); clearTheater();}
    
    public static void paintPaintables(Graphics2D g2, Component c) {       
        synchronized(paintables) {
            for(Iterator<Paintable> it = paintables.iterator(); it.hasNext();) {
                Paintable p = it.next();
                if(!p.isActive()) it.remove();
                if(p.showOnlyOnEditor() && !GameFrame.onEditor) continue;
                
                p.paint(g2, c);
            }
        }
    }
    
    public static void paintInterface(Graphics2D g2, Component c) {
        synchronized(interfaceP) {
            for(Iterator<Paintable> it = interfaceP.iterator();it.hasNext();) {                
                Paintable p = it.next();

                if(p.isActive()) {
                    p.paint(g2, c);
                } else {
                    it.remove();
                    break;
                }
            }
        }   
    }
    
    public static void paintTheater(Graphics2D g2, Component c) {
        synchronized(theaterP) {
            for(Iterator<Paintable> it = theaterP.iterator();it.hasNext();) {
                Paintable p = it.next();
                
                if(p.isActive())
                    p.paint(g2, c);
                else {
                    it.remove();
                    break;
                }
            }
        }
    }
    
    static void checkSprites(Collection<WarpBox> c) {
        for(Iterator<Sprite> it = sprites.iterator();it.hasNext();) {
            Sprite s = it.next();
            if(!s.isActive()) it.remove();
            s.Vmodifier = 1;
            for(WarpBox b: c) {
                if(b.hasVelocity && b.bounds.intersects(s.getBounds())) s.Vmodifier*=b.velocity;
            }
        }
    }
    
    static boolean containsTheater(Paintable p) {return theaterP.contains(p);}
    
    public static synchronized void paint(Graphics2D g2, Component c) {
        Paintables.paintPaintables(g2, c);
        if(GameFrame.isInitialized) {
            LightManager.paint(g2, c);
            UserInterface.getUI().paint(g2, c);
        }
    }
}
