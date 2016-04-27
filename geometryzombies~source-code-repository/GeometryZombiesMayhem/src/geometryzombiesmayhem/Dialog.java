/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Yan
 */
public class Dialog implements Paintable, Serializable {
    private MutableImage avatar;
    private String text;
    private transient TextBox tb;
    private boolean isActive = true;
    private boolean requiresAttention = true;
    
    private static boolean started = false;
    
    private static ArrayList<Dialog> dialogs = new ArrayList<>();
    public static boolean onDialog = false;

    private static KeyAdapter kl = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if(e.getKeyChar()=='\n') {
                updateDialog();
                DialogTree.checkTrees();
            }
        }
    };
    
    public static java.util.List<Dialog> getDialogs() {return dialogs;}
    
    public static void load(GeneralAdapter gA) {
        gA.add(kl);  
    }
    
    public static void close(GeneralAdapter gA) {
        gA.remove(kl);
    }
    
    public static void updateDialog () {
        if(!onDialog) return;
        dialogs.get(0).onEnd();
        Paintables.removeInterface(dialogs.remove(0));
        if(onDialog = !dialogs.isEmpty()) {
            dialogs.get(0).onStart();
            Paintables.registerInterface(dialogs.get(0));
        }
    }
    public static void register(Dialog d) {
        if(started || !dialogs.isEmpty()) {
            onDialog = true;
            if(dialogs.isEmpty()) {
                d.onStart();
            Paintables.registerInterface(d); 
            }
            dialogs.add(d);
        } else{
            dialogs.add(d);
        }
    }
    
    public static void start() {
        started = true;
        Dialog d = dialogs.remove(0);
        register(d);
    }
    
    public Dialog(MutableImage avatar, String text, Color c, int alpha, Font font) {
        this.text = text;
        this.avatar = avatar;
//        int mc = (int) (1.85*760.0/(font.getSize()/72.0*0.0254f*ZM.realPixelsPerMeter)); // not sure why i multiplied by 1.8, but the rest should calaculate how many charaters are writable ate the screen
        //System.err.println(mc);
//        while(avatar.getWidth(null)==-1);
        //System.err.println(avatar.getWidth(null));
        int mc = 650;
        tb = new TextBox(text, new Vector2D(avatar.getWidth(null) + 20, 20), ColorFactory.getColor(c, alpha), font, mc);
//        tb = new TextBox(new Vector2D(avatar.getWidth(null) + 20, 20), text, c, alpha, font, mc);
        tb.setFontColor(Color.WHITE);
//        tb.setFixedSize(GameFrame.FRAME_WIDTH - avatar.getWidth(null) - 20, avatar.getHeight(null));
//        tb.setWordWrap(true);
    }
    
    public Dialog(MutableImage avatar, String text, Color c, int alpha, Font font, boolean requiresAttention) {
        this(avatar,text,c,alpha,font);
        this.requiresAttention = requiresAttention;
    }
    
    public static Dialog getDialog(Predefined p,String text) {
        Dialog d = null;
        switch(p) {
            case TARMA:
                d = new Dialog(AssetManager.loadImage("resources/face_tarma.png"),
                        text,Color.BLACK,80,new Font(Font.DIALOG_INPUT, Font.PLAIN, 15));
            break;
            case JESUS:
                d = new Dialog(AssetManager.loadImage("resources/renatoisjesus.png"),
                        text, Color.BLACK, 80, new Font(Font.DIALOG_INPUT, Font.PLAIN, 15));
            break;
        }
        return d;
    }
    
    public static Dialog getDialog(Predefined p, String text, boolean requiresAttention) {
        Dialog d = getDialog(p,text);
        d.requiresAttention = requiresAttention;
        return d;
    }
    
    public Font getFont() {return tb.getFont();}
    public TextBox getTextBox() {return tb;}
    
    @Override
    public synchronized void paint(Graphics2D g2, Component c) {
        g2.drawImage(avatar, 10, 10, c);
        tb.paint(g2, c);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public void onStart() {
        if(requiresAttention){
            Player.getDefaultPlayer().setControl(false);
        }
    }
    public void onEnd() {
        if(requiresAttention) {
            Player.getDefaultPlayer().setControl(true);
        }
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
    
    public static enum Predefined {TARMA, JESUS;}
    
    public static int getMaxCharsOnScreen(Font font) {
        return (int)(1.85*760.0/(font.getSize()/72.0*0.0254f*ZM.realPixelsPerMeter));
    }
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.tb = new TextBox((Vector2D)in.readObject(), (String)in.readObject(),
                (Color)in.readObject(), (Font)in.readObject(), in.readInt());
    }
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeObject(tb.getPosition());
        out.writeObject(tb.getText());
        out.writeObject(tb.getFontColor());
        out.writeObject(tb.getFont());
        out.writeInt(tb.getMaxCharsPerLine());
    }
    
    private static final long serialVersionUID = 696969696969696969L;
}
