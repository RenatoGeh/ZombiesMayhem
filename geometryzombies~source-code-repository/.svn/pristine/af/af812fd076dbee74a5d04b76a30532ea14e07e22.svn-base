package geometryzombiesmayhem;

/**
 * <p>Defines all methods for Artificial Intelligence management.</p>
 * 
 * @author Renato Lui Geh
 */
public interface ArtificialIntelligence {
    /**
     * <p>Handles the AI movement when chasing or following the player.</p>
     * 
     * <p>This method manages the movement of an AI entity when either
     * chasing (enemy AI) or simply following (friendly AI) the player.</p>
     * 
     * <p>Implementation should contain a search tree to find the best
     * approach to either attack the player or find the best path to
     * meet the player effectively.<p>
     * 
     * <p>The use of tree nodes and graphs should be, preferably, 
     * focused on performance instead of memory usage, such as a
     * breadth node search.</p>
     */
    public void handleMovement();
    /**
     * <p>Handles the AI attack against an enemy entity.</p>
     * 
     * <p>This method manages the attacking procedures against an enemy
     * entity by finding the best approach, guaranteeing the implementing
     * object its survival (high level AI) or not (low level AI).</p>
     */
    public void handleAttack();
    /**
     * <p>Handles idling of the AI.</p>
     * 
     * <p>This method manages the idling procedures, such as patrolling
     * or use of animations during a given period of idling.<p>
     */
    public void handleIdling();
    /**
     * <p>Checks whether any enemies are in movements range.</p>
     * 
     * <p>This method checks regularly whether enemy entities are in movement range
     * of its actions. If <code>true</code>, the implementing object will
     * perform <code>handleMovement</code> followed by <code>handleAttack</code>
     * when in range of weapons. Else, the entity will perform <code>handleIdling</code>.</p>
     * 
     * <p>Note that the terms <code>movement range</code> and <code>weapons range</code>
     * have different meanings. Whilst the latter indicates a possibility of actual attack,
     * triggering the method <code>handleAttack</code>, the first infers that the enemy
     * entity is in range of vision, causing or not the instance to enter <code>weapons range</code>.
     * 
     * @return Whether entities are in movement range.
     */
    public boolean checkMovementsRange();
    /**
     * <p>Checks whether any enemies are in weapons range.</p>
     * 
     * <p>This method checks regularly whether enemy entities are in weapons range. 
     * If <code>true</code>, the implementing object will perform <code>handleAttack</code>
     * Else, the entity will perform <code>handleIdling</code> or <code>handleMovement</code>
     * if the enemy entity goes out of movement range.</p>
     * 
     * <p>Note that the terms <code>movement range</code> and <code>weapons range</code>
     * have different meanings. Whilst the latter indicates a possibility of actual attack,
     * triggering the method <code>handleAttack</code>, the first infers that the enemy
     * entity is in range of vision, causing or not the instance to enter <code>weapons range</code>.
     * 
     * @return Whether entities are in weapons range.
     */
    public boolean checkWeaponsRange();
    
    public void attack();
    
    public void handleBefore();
}