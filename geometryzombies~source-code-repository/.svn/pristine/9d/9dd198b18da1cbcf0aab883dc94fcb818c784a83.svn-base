package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.Body;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**@deprecated
 * An Island is an union of 5 Bodies
 * @author Yan Soares Couto
 */
public class Island {
    private static final int size = 5; 
    
    /**
     * All the islands
     */
    public static List<Island> islands = new ArrayList<>();
    /**
     * List of bodies in an island
     * DO NOT EDIT IT MANUALLY
     */
    public Set<Body> bodies = new HashSet<>(size);
    /**
     * Rectangle that contains all bodies
     */
    protected Rectangle2D bounds = new Rectangle(0,0,0,0);
    /**
     * Checks if the received shape intersects bounds of this Island
     * @param s Shape to check
     * @return weather it intersects or not
     */
    public boolean intersects(Shape s) {
        return s.intersects(bounds);
    }
    
    public static Island addBody(Body b) {
        Island s;
        if(islands.isEmpty()) s = new Island();
        else {
            s = islands.get(islands.size()-1);
            if(s.bodies.size()==size) s = new Island();
        }
        s.bodies.add(b);
        return s;
    }
    
}
