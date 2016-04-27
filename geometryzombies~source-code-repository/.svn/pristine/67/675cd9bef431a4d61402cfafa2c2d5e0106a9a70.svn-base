package geometryzombiesmayhem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

/**
 * <p>Stats</p>
 * 
 * @author Renato Lui Geh
 */
public class Stats {
    private Person p;
    private Attributes attributes;
    
    private Skills skills;
    private Abilities abilities;
    private Perks perks;
    private Traits traits;
    
    public Stats(Person p) {
        this.p = p;
    }
    
    /**
     * Skills are global and do not require pre-requisites.
     */
    public class Skills {
        private EnumMap<SKILLS_ENUM, Skill> mapper = new EnumMap<>(SKILLS_ENUM.class);
        
        {
            for(SKILLS_ENUM e : SKILLS_ENUM.values())
                mapper.put(e, new Skill());
        }
        
        public class Skill {
            private MutableInteger level;
            private Attributes ats;
            
            private Skill(Attribute... at) {this(50, at);}
            
            private Skill(int level, Attribute... at) {
                this.level = new MutableInteger(level);
                this.ats.add(at);
            }
            
            public int getRawLevel() {return level.getValue();}
            public MutableInteger getLevel() {return level;}
            public Attributes getAttributes() {return ats;}
            
            public void levelUp() {this.level.increment();}
            public void levelDown() {this.level.decrement();}
        }
        
        public Skill getSkill(SKILLS_ENUM e) {return mapper.get(e);}
    }
    
    /**
     * Traits are particular to a given character.
     */
    public class Traits {
        private ArrayList<Trait> traits = new ArrayList<>();
        
        public Traits(Trait... traits) {this.traits.addAll(Arrays.asList(traits));}
        
        public void apply(Skills e) {for(Trait t : traits) t.apply(e);}
    }
    
    /**
     * Abilities can be purchased and evolved with the use of Ability Points,
     * acquired from leveling up.
     */
    public class Abilities {
        
    }
    
    /**
     * Perks are not choosable, but acquired based on your traits and skills balance.
     */
    public class Perks {
        
    }
    
    public static enum SKILLS_ENUM {
        HANDGUNS("Handguns", "Your skill with one handed guns, such as pistols or revolvers."), 
        RIFLES("Rifles", "Your skill with rifle class guns, such as Assault Rifles or Sniper Rifles."), 
        MELEE("Melee", "Your skill with melee weapons, such as knives or swords. This skill also affects unarmed combat."), 
        HEAVY_WEAPONS("Heavy Weapons", "Your skill with heavy weaponry, such as Gatlings, Rocket/Grenade Launchers or Flamethrowers."),
        SURVIVALIST("Survivalist", "This skill measures your aptitude for surviving the wilderness."),
        CRAFTING("Crafting", "This skill measures your capabilities for crafting."),
        SCIENCE("Science", "Your skills in the subject of nature and technology."),
        PHYSICIAN("Physician", "Your skills in the art of healing or poisoning."),
        SPEECH("Speech", "Your skills in the art of persuasion and lying.");
        
        /*
         * Note to self: 
         * 
         * - Compound Crossbows will require both a high Survivalist
         * and a Crafting skill to be crafted.
         * - Drones will require a high Science, (maybe) Hacking and Crafting skill to be crafted.
         */
        
        private String name, description;
        
        private SKILLS_ENUM(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() {return name;}
        public String getDescription() {return description;}
    };
    
    public static enum Trait {
        DO_AMOEBAE_DREAM("Do Amoebae Dream of Eukaryote Sheep?", 
                "By deep meditation and studying, you now understand the complexity of the human mind "
                + "and its connections with nature and its elements. "
                + "You gain +15 to Science and +10 to Survivalist, but at a -5 cost to "
                + "all combat related skills.",
                new SkillChange(SKILLS_ENUM.SCIENCE, 15), new SkillChange(SKILLS_ENUM.SURVIVALIST, 10),
                new SkillChange(SKILLS_ENUM.HANDGUNS, -5), new SkillChange(SKILLS_ENUM.RIFLES, -5),
                new SkillChange(SKILLS_ENUM.HEAVY_WEAPONS, -5), new SkillChange(SKILLS_ENUM.MELEE, -5)),
        DR_STRANGELOVE("Dr. Strangelove", 
                "You love bombs. They are your passion. So, why worry at all if you have the bomb? "
                + "You gain +40 to Heavy Weapons, but at a -50 cost to Speech.", 
                new SkillChange(SKILLS_ENUM.HEAVY_WEAPONS, 40), new SkillChange(SKILLS_ENUM.SPEECH, -50)),
        TRIGGER_HAPPY("Trigger Happy", 
                "Living in the New Jersey hoods left a big scar on you. You've learned that the more you "
                + "shoot the more you hit, causing your accuracy and recoil dampening to drop drastically. "
                + "In the other hand, living in Jersey made you able to survive anywhere, be it a desert or a favela. "
                + "+20 to Survivalist. -25% to accuracy and recoil dampening.",
                new SkillChange(SKILLS_ENUM.SURVIVALIST, 20)),
        MARKSMAN("Marksman",
                "You're an expert sharpshooter. Patience is your virtue. Explosions are your doom."
                + "+50 to Rifles. +50% to accuracy and recoil dampening. -50 to Heavy Weapons.", 
                new SkillChange(SKILLS_ENUM.RIFLES, 50), new SkillChange(SKILLS_ENUM.HEAVY_WEAPONS, -50)),
        ;
        
        
        private String name, description;
        private SkillChange[] e;
        
        private Trait(String name, String description, SkillChange... e) {
            this.name = name;
            this.description = description;
            this.e = e;
        }
        
        public String getName() {return name;}
        public String getDescription() {return description;}
        
        public void apply(Skills skills) {
            for(SkillChange s : e)
                s.apply(skills);
        }
    }
    
    private static class SkillChange {
        private SKILLS_ENUM e;
        int n;
        
        private SkillChange(SKILLS_ENUM e, int n) {
            this.e = e;
            this.n = n;
        }
        
        private void apply(Skills skills) {
            skills.getSkill(e).level.add(n);
        }
    }
}