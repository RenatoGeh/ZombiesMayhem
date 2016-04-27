package geometryzombiesmayhem;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Contains each <code>DialogNode</code> (with a <code>Dialog</code> value) linked to another <code>DialogNode</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class DialogTree implements java.io.Serializable {
    private DialogNode rootNode;
    private boolean isActive = false;
    private boolean wasUsed = false;
    private boolean repeats = false;
    
    public DialogTree(Dialog rootNode) {
        this.rootNode = new DialogNode(rootNode);
    }
    
    protected DialogNode getRoot() {return rootNode;}
    
    public void add(Dialog e) {rootNode.add(e);}
    public void add(Dialog... e) {rootNode.add(e);}
    
    public boolean isActive() {return isActive;}
    protected void setActive(boolean isActive) {this.isActive = isActive; this.rootNode.setActive(isActive);}
    
    public void setRepeats(boolean repeats) {this.repeats = repeats;}
    
    public boolean wasUsed() {return wasUsed;}
    
    public void enable() {
        this.isActive = true; 
        DialogTree.register(this);
        rootNode.enable();
        Dialog.register(rootNode.getValue());
    }
    public void disable() {
        this.isActive = false; 
        this.rootNode.disable(); 
        this.wasUsed = true; 
        DialogTree.remove(this);
    }
    
    public void reset() {
        this.rootNode.reset(); 
        this.isActive = false; 
        this.wasUsed = false; 
        DialogTree.remove(this);
    }
    
    private static Set<DialogTree> trees = new HashSet<>();
    
    public static void register(DialogTree tree) {trees.add(tree);}
    public static void remove(DialogTree tree) {trees.remove(tree);}
    
    protected static void checkTrees() {
        for(DialogTree tree : trees) {
            if(!tree.isActive())
                continue;
            
            DialogNode d = tree.getRoot().getActiveChild();
            if(d != null)
                d.checkNode();
            else
                tree.disable();
            
            if(tree.repeats)
                if(tree.wasUsed)
                    tree.reset();
        }
    }
    
    private static final long serialVersionUID = 108117105103105L;
}