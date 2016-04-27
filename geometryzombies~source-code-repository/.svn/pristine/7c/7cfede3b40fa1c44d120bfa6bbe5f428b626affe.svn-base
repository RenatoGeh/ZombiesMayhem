/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.Vector2D;
import java.util.ArrayList;

/**
 *
 * @author Yan
 */
public abstract class OutputBody extends CircuitryBody implements Output {
    ArrayList<Input> inputs = new ArrayList<>();
    
    public OutputBody() {super();}
    
    public OutputBody(Vector2D pos) {
        super(pos);
    }

    @Override
    public void add(Input c) {
        inputs.add(c);
        c.connected(this);
    }

    @Override
    public void remove(Input c) {
        inputs.remove(c);
        c.disconnected(this);
    }
    
}
