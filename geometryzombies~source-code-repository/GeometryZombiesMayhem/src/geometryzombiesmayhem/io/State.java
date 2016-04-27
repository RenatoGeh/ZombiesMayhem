package geometryzombiesmayhem.io;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.ColorFactory;
import geometryzombiesmayhem.Dialog;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.Level;
import geometryzombiesmayhem.LightManager;
import geometryzombiesmayhem.LightSource;
import geometryzombiesmayhem.Player;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>A representation of all the <code>Serializable</code> objects organized in a logical order.</p>
 * 
 * @author Renato Lui Geh
 */
public class State implements Serializable {
    public static enum Type {LEVEL, SAVE};
    
    private transient String path = "Path is hidden.";
    private String date;

    public State(Type t) {this(t.equals(Type.LEVEL)?StateManager.DEFAULT_LEVEL_PATH:StateManager.DEFAULT_SAVE_PATH);}
    public State() {this(Type.SAVE);}
    
    public State(String path) {
        this.path = path;
        this.date = FORMATTER.format(new Date());
    }
    
    public String getPath() {return path;}
    public String getDate() {return date;}
    
    private void writeObject(ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        
        out.writeObject(Section.Level);
        
        System.err.println("Writing Level...");
        out.writeObject(GameFrame.currentLevel);
        
        out.writeObject(Section.Player);

        System.err.println("Writing players...");
        for(Player p : Player.getPlayers())
            out.writeObject(p);
        
        out.writeObject(Section.Body);
        
        System.err.println("Writing bodies...");
        for(Body b : Body.getBodies())
            out.writeObject(b);
        
        out.writeObject(Section.Light);
        
        System.err.println("Writing light sources...");
        for(LightSource source : LightManager.getSources())
            out.writeObject(source);
        out.writeObject(LightManager.getColor());
        
        out.writeObject(Section.Dialog);
        
        System.err.println("Writing dialogs...");
        for(Dialog diag : Dialog.getDialogs())
            out.writeObject(diag);
        
        System.err.println("Completing transfer...");
        out.writeObject(Section.EOF);
    }
    
    private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        Section current = (Section)in.readObject();
        Object last = null;
        
        System.err.println("Reading Level...");
        if(current.equals(Section.Level)) {
            GameFrame.currentLevel = (Level)in.readObject();
            current = (Section)in.readObject();
        }        
        
        //Players:
        System.err.println("Reading players...");
        if(current.equals(Section.Player)) {
            boolean first = true;
            while(true) {
                last = in.readObject();
                if(!(last instanceof Player)) break;
                if(first) {
                    Player.getPlayers().clear(); 
                    Player.registerAsDefault((Player)last); 
                    first = false; 
                    GameFrame.paintArea.focusBody = Player.getDefaultPlayer();
                    continue;
                }
                else Player.register((Player)last);
            }
        }
        
        if(last != null)
            if(last instanceof Section) {
                current = (Section)last;
            } else 
                current = (Section)in.readObject();
        
        //Bodies:
        System.err.println("Reading bodies...");
        if(current.equals(Section.Body)) {
            boolean first = true;
            while(true) {
                last = in.readObject();
                if(!(last instanceof Body)) break;
                if(first) {Body.getBodies().clear(); first = false;}
                Body.register((Body)last);
            }
        }
        
        if(last != null)
            if(last instanceof Section) {
                current = (Section)last;
            } else
                current = (Section)in.readObject();
        
        //Light Sources:
        System.err.println("Reading light sources...");
        if(current.equals(Section.Light)) {
            boolean first = true;
            while(true) {
                last = in.readObject();
                if(!(last instanceof LightSource)) {
                    if(last instanceof java.awt.Color)
                        darkness = (java.awt.Color)last;
                    break;
                }
                if(first) {LightManager.getSources().clear(); first = false;}
                LightManager.register((LightSource)last);
            }
        }

        if(last != null)
            if(last instanceof Section) {
                current = (Section)last;
            } else
                current = (Section)in.readObject();
        
        System.err.println("Reading dialogs...");
        if(current.equals(Section.Dialog)) {
            boolean first = true;
            while(true) {
                last = in.readObject();
                if(!(last instanceof Dialog)) break;
                if(first) {Dialog.getDialogs().clear(); first = false;}
                Dialog.register((Dialog)last);
            }
        }
        
        if(last.equals(Section.EOF))
            System.err.println("Transfer Complete!");
    }
    
    protected static enum Section {
        Player("//PLAYER:"),
        Body("//BODY:"),
        Light("//LIGHT:"),
        Dialog("//DIALOG:"),
        Level("//LEVEL:"),
        EOF("//EOF");
        
        private String rep;
        private Section(String rep) {
            this.rep = rep;
        }
        
        @Override
        public String toString() {return rep;}
        public boolean compare(String e) {return this.rep.equals(e);}
    }
    
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static final long serialVersionUID = 9183749813240L;
    private static java.awt.Color darkness = ColorFactory.getColor(java.awt.Color.BLACK, 0);
    
    public static java.awt.Color getDarknessColor() {return darkness;}
}
