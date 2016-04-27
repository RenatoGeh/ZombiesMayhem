package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Scene implements Paintable, Serializable {
    private String name;
    private ArrayList<SceneLayer> background;
    private ArrayList<SceneLayer> foreground;
    
    /**
     * Serialization use <b>only</b>.
     */
    public Scene() {};
    
    public Scene(String name) {
        this.name = name;
        background = new ArrayList<>();
        foreground = new ArrayList<>();
    }
    
    public void addForegroundLayer(SceneLayer layer) {foreground.add(layer);}
    public void addForegroundLayer(SceneLayer layer, int index) {foreground.add(index, layer);}
    
    public void addLayer(SceneLayer layer) {
        background.add(layer);
    }
    
    public void addLayer(SceneLayer layer, int index) {
        background.add(index, layer);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public int size() {
        return background.size();
    }
    
    public void clear() {
        background.clear();
    }    
    
    public ArrayList<SceneLayer> getLayers() {
        return background;
    }
    
    public SceneLayer getLayer(int index) {
        return background.get(index);
    }
    
    public SceneLayer getLayer(String name) {
        for(int i=0;i<background.size();i++)
            if(background.get(i).getName().equals(name))
                return background.get(i);
                
        return null;
    }
    
    public void removeAll(Collection<SceneLayer> removed) {
        background.removeAll(removed);
    }
    
    public boolean isEmpty() {
        return background.isEmpty();
    }
    
    public void removeLayer(int index) {
        background.remove(index);
    }
    
    public boolean removeLayer(SceneLayer layer) {
        if(background.remove(layer))
            return true;
        else 
            return false;
    }
    
    @Override
    public void paint(Graphics2D g, Component c) {
        if(background.isEmpty())
            System.err.println("No layers to paint!");
        for(int i=0;i<background.size();i++) {
            if(background.get(i).followsPlayer())
                background.get(i).repaintWithPlayer(g, c, Player.getDefaultPlayer().Vx);
            else
                background.get(i).repaint(g, c);
            //System.out.println("Layer " + i + " deviates " + layers.get(i).getDeviation());
            //System.out.println("Layer " + i + " moves with speed of " + layers.get(i).getSpeed());
        }
    }
    
    public void paintForeground(Graphics2D g2, Component c) {
        if(foreground.isEmpty()) return;
        for(SceneLayer layer : foreground) {
            if(layer.followsPlayer())
                layer.repaintWithPlayer(g2, c, Player.getDefaultPlayer().Vx);
            else
                layer.repaint(g2, c);
        }
    }
    
    public void paint(Graphics g, Component c) {
        if(background.isEmpty())
            System.err.println("No Layers to paint!");
        
        for(int i=0;i<background.size();i++) {
            background.get(i).repaint((Graphics2D)g, c);
        }
    }
    
    public static final float DEFAULT_LAYER_SPEED = 5;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean active) {
        
    }
    
    private static final long serialVersionUID = 98723135431L;

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}
