package geometryzombiesmayhem;

import java.util.LinkedList;

/**
 * <p>A <code>Node</code> with a <code>Dialog</code> as value.</p>
 * 
 * @author Renato Lui Geh
 */
public class DialogNode implements java.io.Serializable {
    private Dialog nodeValue;
    private LinkedList<DialogNode> childNodes;
    private boolean isActive = false;
    
    public DialogNode(Dialog value) {
        this.nodeValue = value;
        this.childNodes = new LinkedList<>();
    }
    
    public Dialog getValue() {return nodeValue;}
    
    public DialogNode lowerLevel(int index) {return childNodes.get(index);}
    
    public void add(Dialog e) {childNodes.add(new DialogNode(e));}
    public void add(Dialog... e) {for(Dialog d : e) this.add(d);}
    
    public boolean childIsActive() {
        if(isActive)
            return true;
        if(childNodes.isEmpty())
            return false;
        
        for(DialogNode d : childNodes)
            if(d.childIsActive())
                return true;
        
        return false;
    }
    
    public DialogNode getActiveChild() {
        if(isActive)
            return this;
        if(childNodes.isEmpty())
            return null;
        
        for(DialogNode d : childNodes)
            if(d.childIsActive())
                return d;
        
        return null;
    }
    
    public void checkNode() {
        if(isActive) {
            if(nodeValue instanceof InteractibleDialog) {               
                int n = ((InteractibleDialog)nodeValue).getResultingID();
                if(n != 0) {
                    this.disable();
                    
                    if(!this.childNodes.isEmpty()) {
                        this.childNodes.get(n-1).enable();
                        Dialog.register(childNodes.get(n-1).getValue());
                    }
                }
            } else {
                if(!this.childNodes.isEmpty()) {
                    this.childNodes.getFirst().enable();
                    Dialog.register(this.childNodes.getFirst().getValue());
                }
                
                this.disable();
            }
        }
    }
    
    public void checkNodes() {
        for(DialogNode node : childNodes)
            node.checkNode();
    }
    
    public void reset() {
        this.disable();
        
        for(DialogNode node : childNodes)
            node.reset();
    }
    
    protected boolean isActive() {return isActive;}
    protected void setActive(boolean isActive) {this.isActive = isActive;}
    
    public void enable() {this.isActive = true;}
    public void disable() {this.isActive = false;}
    
    public boolean isBottom() {return this.childNodes.isEmpty();}
    
    private static final long serialVersionUID = 4242424242424242L;
}