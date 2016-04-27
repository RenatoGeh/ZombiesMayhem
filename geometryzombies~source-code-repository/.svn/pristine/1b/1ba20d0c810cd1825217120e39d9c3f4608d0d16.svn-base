package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * <p>An <code>Interactible</code> <code>Dialog</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class InteractibleDialog extends Dialog {
    private Alternative[] alternatives;
    private int selectedID = 0;
    
    public InteractibleDialog(MutableImage avatar, String text, Color c, int alpha, Font font, boolean requiresAttention, String[] options, final Runnable[] runs) {
        super(avatar, text, c, alpha, font, requiresAttention);
        
        alternatives = new Alternative[options.length];
        
        for(int i=0;i<alternatives.length;i++) {
            final int n = i;
            alternatives[i] = new Alternative(this.getTextBox().getPosition().add(0, 35 + i*TextBox.getEstimatedSize(options[i], font, true, Dialog.getMaxCharsOnScreen(font)).getY()).add(0, 
                    this.getTextBox().getVectoredSize().getY()), options[i], font, Dialog.getMaxCharsOnScreen(font), i+1) {
                        @Override
                        public void run() {
                            runs[n].run();
                        }
                    };
            alternatives[i].setFontColor(Color.WHITE);
        }
        
        this.getTextBox().setFixedSize((float)this.getTextBox().getSize().getWidth(), 
                (this.alternatives[alternatives.length-1].getHeight() + 
                this.alternatives[alternatives.length-1].getPosition().getY()) + 25);
    }
    
    public InteractibleDialog(MutableImage avatar, String text, Color c, int alpha, Font font, boolean requiresAttention, Alternative... alternatives) {
        super(avatar, text, c, alpha, font, requiresAttention);
        
        this.alternatives = alternatives;
        
        this.getTextBox().setFixedSize((float)this.getTextBox().getSize().getWidth(), 
                (this.alternatives[alternatives.length-1].getHeight() + 
                this.alternatives[alternatives.length-1].getPosition().getY()) + 25);
    }
    
    public InteractibleDialog(MutableImage avatar, String text, Color c, int alpha, Font font, Alternative... alternatives) {
        this(avatar, text, c, alpha, font, false, alternatives);
    }
    
    public static Dialog getDialog(Predefined p, String text, boolean requiresAttention, String[] options, Runnable[] runs) {
        Dialog d = null;
        switch(p) {
            case TARMA:
                d = new InteractibleDialog(AssetManager.loadImage("resources/face_tarma.png"), text, Color.BLACK, 80, 
                        new Font(Font.DIALOG_INPUT, Font.PLAIN, 20), requiresAttention, options, runs);
                break;
        }
        return d;
    }

    @Override
    public void paint(Graphics2D g2, Component c) {
        super.paint(g2, c);
        
        for(Alternative a : alternatives) {
            if(!a.isDone())
                a.paint(g2, c);
            else
                AwesomeTimer.addAction(new AwesomeAction() {
                    @Override
                    public void run() {
                        InteractibleDialog.updateDialog();
                    }
                }, 500, false);
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.loadInstance();
    }
    
    @Override
    public void onEnd() {
        super.onEnd();
        this.closeInstance();
        
        for(int i=0;i<alternatives.length;i++)
            if(alternatives[i].isSelected())
                this.selectedID = i+1;
        
        DialogTree.checkTrees();
    }
    
    public int getResultingID() {return selectedID;}
    
    public void loadInstance() {
        for(Alternative a : alternatives)
            a.load();
    }
    
    public void closeInstance() {
        for(Alternative a : alternatives)
            a.close();
    }
}