package geometryzombiesmayhem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Configuration File (.ini).</p>
 * 
 * @author Renato Lui Geh
 */
public class ConfigurationFile {
    private String path;
    private Map<Section, Integer> marks;
    private ArrayList<Field> fields;
    
    public ConfigurationFile(Field[] f) {
        fields.addAll(Arrays.asList(f));
    }
    
    public ConfigurationFile(String path) {
        this(path, false);
    }
    
    public ConfigurationFile(String path, boolean isInside) {
        this.path = path;
        this.marks = new EnumMap<>(Section.class);
        fields = new ArrayList<>();
        
        this.scan(isInside);
    }
    
    private void scan(boolean isInside) {
        try {
            path = path.replaceFirst("file:", "");
            BufferedReader in;
            
            if(isInside)
                in = new BufferedReader(new InputStreamReader(AssetManager.CLASS_LOADER.getResourceAsStream("geometryzombiesmayhem/" + path)));
            else
                in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            
            String line;
            int n = 1;
            Section last = Section.Game;
            
            while((line = in.readLine()) != null) {
                //System.err.println(line);
                if(line.startsWith("#"))
                    continue;
                else if(line.startsWith("[")) {
                    last = Section.valueOf(line.substring(1, line.length()-1));
                    marks.put(last, n);
                } else if(!line.isEmpty()){
                    String[] s = line.split("=");
                    String arg = s[1];
                    if(this.contains(arg, '"'))
                        arg = arg.replace('"', ' ').trim();
                    
                    fields.add(new Field(s[0], arg, last));
                }
                
                n++;       
            }
        
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean contains(String s, char c) {
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) == c)
                return true;
        }
        return false;
    }
    
    public int getSectionLine(Section s) {return marks.get(s);}
    
    public String getPath() {return path;}
    public ArrayList<Field> getFields() {return fields;}
    
    public static enum Section {
        Game, Controls, Graphics, Audio;
        
        @Override
        public String toString() {
            return "[" + this.name() + "]";
        }
    };
    
    public static class Field {  
        private String var;
        private String arg;
        
        private Section sec;
        
        public Field(String var, String arg, Section sec) {
            this.var = var;
            this.arg = arg;
            this.sec = sec;
        }
        
        @Override
        public String toString() {
            if(arg.equals("true") || arg.equals("false") || isNumber(arg) || Vector2D.isVector(arg))
                return var + "=" + arg;
            else
                return var + "=" + "\"" + arg + "\"";
        }
        
        private boolean isNumber(String arg) {
            char[] seq = arg.toCharArray();
            
            for(char c : seq)
                if(!Character.isDigit(c))
                    return false;
            
            return true;
        }
        
        public String getVar() {return var;}
        public String getArg() {return arg;}
        public Section getSection() {return sec;}
    }
}