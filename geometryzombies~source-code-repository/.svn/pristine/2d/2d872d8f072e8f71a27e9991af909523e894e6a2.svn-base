package geometryzombiesmayhem;

import java.awt.Image;
import java.util.ArrayList;

/**
 * Interface that handles the sidescrollability of 'n' images.
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public interface Sidescrollable {
    
    public static final int TYPE_SIMPLE_BACK = 0;
    public static final int TYPE_COMPLEX_BACK = 1;
    
    public static final int TYPE_SIMPLE_COMPOSITE = 2;
    public static final int TYPE_COMPLEX_COMPOSITE = 3;
    
    /**
     * Takes an ArrayList of positions and multiple (recommend more than 3) images in ArrayList.
     * Use this to define the scrollability of a given Scene and what it should do after accomplishing a given task.
     * 
     * <p><b>Note: the initial position determined through the parameters should be the one of the first Image.</b</p>
     * 
     * @see Vector2D
     * 
     * @param initialPosition
     * @param images 
     */
    public void checkBounds(ArrayList<Vector2D> initialPosition, ArrayList<Image> images);
    public void followPlayer(Vector2D leftLimit, Vector2D rightLimit, Player player);
    public void followPlayer(float Vx);
    public void checkBounds();
    
}