package geometryzombiesmayhem;

import geometryzombiesmayhem.Attribute.Type;
import java.util.*;

/**
 * <p>Represents the collection of attributes.</p>
 * 
 * @author Renato Lui Geh
 */
public class Attributes implements java.io.Serializable {
    private Map<Attribute.Type, Set<Attribute>> mapper = new HashMap<>();
    
    /**
     * Serialization constructor.
     */
    public Attributes(){};
    
    public Attributes(Attribute... e) {
        this.add(e);
    }
    
    public final void add(Attribute... e) {
        for(int i=0;i<e.length;i++) {
            if(!mapper.containsKey(e[i].getType()))
                mapper.put(e[i].getType(), new HashSet<Attribute>());
            mapper.get(e[i].getType()).add(e[i]);
        }
    }
    
    public final void remove(Attribute... e) {
        for(int i=0;i<e.length;i++) {
            if(!mapper.containsKey(e[i].getType()))
                break;
            mapper.get(e[i].getType()).remove(e[i]);
        }
    }
    
    public final void construct(Object parent) {
        Collection<Set<Attribute>> atts = mapper.values();
        
        for(Set<Attribute> s : atts)
            for(Attribute a : s)
                a.modify(parent);
    }
    
    public final void attribute(Object... e) {
        Collection<Set<Attribute>> atts = mapper.values();
        
        for(Set<Attribute> s : atts)
            for(Attribute a : s)
                a.runtime(e);
    }
    
    public String getArguments() {
        String res = "";
        
        Collection<Set<Attribute>> atts = mapper.values();
        
        for(Set<Attribute> s : atts)
            for(Attribute a : s)
                res += a.getArgument() + "\n";
        
        return res;
    }
    
    public static Attribute test = new Attribute() {

        @Override
        public void modify(Object e) {
            if(e instanceof Gun)
                ((Gun)e).setMinDamage(((Gun)e).getMinDamage() + 10);
            
            //+10 minimum damage
        }

        @Override
        public void runtime(Object... e) {
            if(e[0] instanceof Gun && e[1] instanceof Shot)
                if(ZM.RANDOM.nextInt(5+1) == 2)
                    ((Shot)e[1]).setDamage(((Gun)e[0]).getMaxDamage() + ((Gun)e[0]).getMaxDamage()/5);
            
            //25% chance of a critical hit
        }

        @Override
        public String getName() {
            return "Increase Gun Barrel";
        }

        @Override
        public Type getType() {
            return Attribute.Type.DAMAGE;
        }

        @Override
        public String getArgument() {
            return "+10 to minimum damage output." + "\n" + "25% to get a critical hit.";
        }

        @Override
        public String getValue() {
            return "10 25/100";
        }
        
        @Override
        public String getDetails() {
            return "This gun attachment amplifies the damage output by increasing its barrel. "
                    + "With an increased weight on the front of the weapon, you are able to aim "
                    + "better, giving you a 25% chance of getting a critical hit (max damage + .2*max damage).";
        }
    };
}