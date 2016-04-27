package geometryzombiesmayhem;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class GeneralAdapter extends MouseAdapter implements KeyListener,ComponentListener,ActionListener,ChangeListener {
    private List<MouseAdapter> ma = null;
    private List<KeyListener> kl = null;
    private List<ComponentListener> cl = null;
    private List<ActionListener> al = null;
    private List<ChangeListener> chl = null;
    
    private final byte DEF_SIZE = 2;
    
    public void add(MouseAdapter adapter) {
        if(ma == null) ma = new ArrayList<>(DEF_SIZE);
        synchronized (ma) {
            ma.add(adapter);
        }
    }
    
    public void add(KeyListener listener) {
        if(kl == null) kl = new ArrayList<>(DEF_SIZE);
        synchronized (kl) {
            kl.add(listener);
        }
    }
    
    public void add(ComponentListener listener) {
        if(cl == null) cl = new ArrayList<>(DEF_SIZE);
        synchronized (cl) {
            cl.add(listener);
        }
    }
    
    public void add(ActionListener listener) {
        if(al==null) al = new ArrayList<>(DEF_SIZE);
        synchronized(al) {
            al.add(listener);
        }
    }
    
    public void add(ChangeListener listener){
        if(chl==null) chl = new ArrayList<>(DEF_SIZE);
        synchronized(chl) {
            chl.add(listener);
        }
    }
    
    public void remove(MouseAdapter adapter) {if(ma==null)return; synchronized (ma) {ma.remove(adapter);} }
    public void remove(KeyListener listener) {if(kl==null)return; synchronized (kl){kl.remove(listener);} }
    public void remove(ComponentListener listener) {if(cl==null)return; synchronized (cl){cl.remove(listener);} }
    public void remove(ActionListener listener) {if(al==null)return; synchronized(al) { al.remove(listener); } }
    public void remove(ChangeListener listener) {if(chl==null)return; synchronized(chl) { chl.add(listener); } }
    
    @Override
    public void keyReleased(KeyEvent event) {
        if(kl==null) return;
        synchronized(kl) {
        for(KeyListener listener : kl) {
            listener.keyReleased(event);
        }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent event) {
        if(kl==null) return;
        synchronized(kl) {
        for(KeyListener listener : kl) {
            listener.keyPressed(event);
        }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent event){
        if(kl==null) return;
        synchronized(kl) {
        for(KeyListener listener : kl) {
            listener.keyTyped(event);
        }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mouseDragged(event);
        }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mouseMoved(event);
        }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mouseClicked(event);
        }
        }
    }
    
    @Override
    public void mousePressed(MouseEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mousePressed(event);
        }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mouseReleased(event);
        }
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mouseEntered(event);
        }
        }
    }
    
    @Override
    public void mouseExited(MouseEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mouseExited(event);
        }
        }
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent event){
        if(ma==null) return;
        synchronized(ma) {
        for(MouseAdapter listener : ma) {
            listener.mouseWheelMoved(event);
        }
        }
    }

    @Override
    public void componentHidden(ComponentEvent event) {
        if(cl==null) return;
        synchronized(cl) {
        for(ComponentListener listener : cl) {
            listener.componentHidden(event);
        }
        }
    }

    @Override
    public void componentShown(ComponentEvent event) {
        if(cl==null) return;
        synchronized(cl) {
        for(ComponentListener listener : cl) {
            listener.componentShown(event);
        }
        }
    }

    @Override
    public void componentMoved(ComponentEvent event) {
        if(cl==null) return;
        synchronized(cl) {
        for(ComponentListener listener : cl) {
            listener.componentMoved(event);
        }
        }
    }

    @Override
    public void componentResized(ComponentEvent event) {
        if(cl==null) return;
        synchronized(cl) {
        for(ComponentListener listener : cl) {
            listener.componentResized(event);
        }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(al==null) return;
        synchronized(al) {
            for(ActionListener listener : al)
                listener.actionPerformed(event);
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if(chl==null) return;
        synchronized(chl) {
            for(ChangeListener listener : chl)
                listener.stateChanged(event);
        }
    }
    
    
}