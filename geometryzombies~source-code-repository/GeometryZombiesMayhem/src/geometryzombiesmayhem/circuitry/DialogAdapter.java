package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.Dialog;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.MutableImage;
import geometryzombiesmayhem.RotatableRectangle;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;

/**
 *
 * @author Renato Lui Geh
 */
public class DialogAdapter extends CircuitryBody implements Input{
    public static enum Type {ONCE, EVERY};
    
    private Dialog diag;
    private float voltage = 0;
    private boolean hasPassed = false;
    private Type type;
    
    public DialogAdapter(Vector2D position, Dialog d, Type t) {
        super(position);
        
        this.diag = d;
        this.type = t;
        
        this.bounds = new RotatableRectangle(position.getX(), position.getY(), 
                DIALOG_ADAPTER.getWidth(), DIALOG_ADAPTER.getHeight(), 0);
    }
    
    @Override
    public void updateVoltage(Output o, float newVoltage) {
        if(newVoltage>voltage&&voltage==0) {
            if(this.type==Type.ONCE && hasPassed) return;
            
            Dialog.register(diag);
            Dialog.start();
            
            this.voltage = newVoltage;
            this.hasPassed = true;
        }
    }

    @Override
    public void connected(Output o) {}
    @Override
    public void disconnected(Output o) {}

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(GameFrame.onEditor) g2.drawImage(DIALOG_ADAPTER, null, (int)position.getX(), (int)position.getY());
    }
    
    @Override
    public Body copy() {return new DialogAdapter(this.position, this.diag, this.type);}

    private static MutableImage DIALOG_ADAPTER = AssetManager.loadImage("resources/items/dialog_adapter.png");
}
