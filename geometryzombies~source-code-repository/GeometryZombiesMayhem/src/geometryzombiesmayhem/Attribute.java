package geometryzombiesmayhem;

/**
 * <p>Represents a particular quality or deficiency.</p>
 * 
 * @author Renato Lui Geh
 */
public abstract interface Attribute extends java.io.Serializable {
    /**
     * Represents the type of the attribute.
     */
    public enum Type {DAMAGE, ARMOR, CUSTOM};
    
    /**
     * <p>Modifies the given object.</p>
     * 
     * <p>This is the actual enabler method. One should override this method with 
     * the algorithm for attribute enabling. Take the example as follows:</p>
     * 
     * <i><p>If one has a <code>Gun</code> instance with a certain unique attribute
     * -- such as +10 minimum damage -- one could code something like:</p></i>
     * 
     * <pre><code>
     * > public void modify(Object e) {
     * >    if(!(e instanceof Gun))
     * >        return;
     * >    else
     * >        ((Gun)e).setMinimumDamage(((Gun)e).getMinimumDamage() + 10);
     * > }
     * </pre></code>
     * 
     * <p>As it is explicit in the sample code, the parameter object is the instance
     * to be modified. For more intricate modifications that require real-time 
     * sorting and handling, see <code>runtime(Object...)</code>.</p>
     * 
     * <p>This method should be handled through an <code>Attributes</code> class instance,
     * which handles all attributes instances of its parent class. It should only be called upon construction,
     * since this is a one-time only usage method.</p>
     * 
     * @param e Object to be modified.
     */
    public void modify(Object e);
    
    /**
     * <p>Handles real-time modifications to the parent object, whilst applies or 
     * takes use of fragments of the other instances.</p>
     * 
     * <p>This is the runtime enabler method. One should override this method
     * with the algorithm for runtime attribute enabling. Take the example as follows:</p>
     * 
     * <i><p>A <code>Gun</code> instance or subclass has a special attribute that confers
     * to it a unique attribute that must be applied on runtime, more exactly on projectile
     * hit. That special attribute is, for didactic purposes, a 25% chance of a critical
     * hit.</p></i>
     * 
     * <pre><code>
     * > public void runtime(Object... e) {
     * >    if(!(e[0] instanceof Gun && e[1] instanceof Shot))
     * >        return;
     * >    else {
     * >        if pseudo-random number is n out of 5, being n higher than 0 and lower than or equal to 5  
     * >            set e[1]'s damage to e[0]'s max damage
     * >        else
     * >            return;
     * >    }
     * > }
     * </pre></code>
     * 
     * <p>This method should be handled through an <code>Attributes</code> class instance,
     * which handles all attributes instances of its parent class. It should be called
     * whenever the runtime modification is necessary. No parent class modifications are
     * recommended.</p>
     * 
     * @param e - Objects to be modified or used as resource.
     */
    public void runtime(Object... e);
    
    /**
     * Retrieves the name of the attribute.
     * @return Name of the attribute.
     */
    public String getName();
    
    /**
     * Retrieves the Type of the attribute.
     * @return Type of the attribute.
     */
    public Type getType();
    
    /**
     * Sends out a String containing the basic informations about the attribute -- normally
     * the argument, value and name.
     * @return String format of the attribute.
     */
    @Override
    public String toString();
    
    /**
     * Retrieves the argument of the attribute, that is, a string format text about
     * the value.
     * @return Argument of the attribute.
     */
    public String getArgument();
    
    /**
     * Retrieves the value of the attribute in a compressed string format.
     * @return Value of the attribute.
     */
    public String getValue();
    
    /**
     * Retrieves detailed information about the attribute.
     * @return Various details regarding the attribute.
     */
    public String getDetails();
}