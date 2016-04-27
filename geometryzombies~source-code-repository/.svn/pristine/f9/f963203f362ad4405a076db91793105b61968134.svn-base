package geometryzombiesmayhem;

import geometryzombiesmayhem.ConfigurationFile.Field;
import geometryzombiesmayhem.ConfigurationFile.Section;
import geometryzombiesmayhem.GameSettings.Graphics.Filter;
import geometryzombiesmayhem.GameSettings.Graphics.ScreenMode;
import java.awt.RenderingHints;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Configuration File (.ini) I/O Loader.</p>
 * 
 * @author Renato Lui Geh
 */
public class ConfigLoader {
    public static final String DEFAULT_CONFIG_PATH = AssetManager.DEFAULT_DATA_PATH + "config.ini";
    
    private ConfigurationFile file;
    
    private BufferedReader in;
    private BufferedWriter out;
    
    public ConfigLoader(ConfigurationFile f) {
        this.file = f;
        parse();
    }
    
    public static boolean isFirstRun() {return !AssetManager.existsCannonical(DEFAULT_CONFIG_PATH);}
    
    public void export(String path, ArrayList<Field> fields) {
        try {
            if(!AssetManager.existsCannonical(path))
                AssetManager.createFile(path);
            
            out = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(path))));
        
            for(String s : template) {
                out.write(s);
                out.newLine();
            }            
            
            this.writeContents(out, fields);
            
        } catch (IOException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void export(String path) {
        System.err.println(path);
        try {
            if(!AssetManager.existsCannonical(path))
                AssetManager.createFile(path);
            
            out = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(path))));
        
            for(String s : template) {
                out.write(s);
                out.newLine();
            }            
            
            this.writeContents(out);
            
        } catch (IOException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void writeTemplate(String path) {
        try {
            if(!AssetManager.existsCannonical(path))
                AssetManager.createFile(path);
            
            out = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(path))));
        
            for(String s : template) {
                out.write(s);
                out.newLine();
            }            
        } catch (IOException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void writeContents(BufferedWriter out, ArrayList<Field> oFields) throws IOException {
        ArrayList<Field> fields = oFields;
        Section current = fields.get(0).getSection();
        
        for(int i=0;i<fields.size();i++) {
            if(!fields.get(i).getSection().equals(current) || i==0) {
                current = fields.get(i).getSection();
                
                out.newLine();
                out.write(current.toString());
                out.newLine();
                out.newLine();
            }
            
            out.write(fields.get(i).toString());
            out.newLine();
        }
    }
    
    private void writeContents(BufferedWriter out) throws IOException {writeContents(out, file.getFields());}
    
    private void parse() {
        ArrayList<Field> fields = file.getFields();
        
        if(!fields.isEmpty()) {
            for(int i=0;i<fields.size();i++) {
                Field f = fields.get(i);
                //Check Section:
                switch(f.getSection()) {
                    case Game:
                        checkGame(f);
                        break;
                    case Controls:
                        GameSettings.Controls.saveIndividualKey(
                                GameSettings.Controls.getKey(f.getArg()), f.getVar(), true);
                        break;
                    case Graphics:
                        checkGraphics(f);
                        break;
                    case Audio:
                        checkAudio(f);
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected exception caught: "
                                + "Section " + f.getSection().toString() + " should not exist!");
                }
            }
        } else {
            GameSettings.Graphics.setScreenMode(ScreenMode.Window);
            GameSettings.Graphics.setResolution(GameSettings.Graphics.getDefaultResolution());
            GameSettings.Graphics.setFilter(Filter.Default);
        }
    }
    
    private void checkGame(Field f) {
        String var = f.getVar();
        String arg = f.getArg();
    }

    private void checkGraphics(Field f) {
        String var = f.getVar();
        String arg = f.getArg();
        
        
        if(var.equals("resizable") || var.equals("resolution") || 
                    var.equals("screen_mode") || var.equals("filter")) {
            switch(var) {
                case "screen_mode":
                    GameSettings.Graphics.setScreenMode(ScreenMode.valueOf(arg));
                    break;
                case "filter":
                    GameSettings.Graphics.setFilter(Filter.valueOf(arg));
                    break;
                case "resizable":
                    GameSettings.Graphics.setResizable(Boolean.valueOf(arg));
                    break;
                case "resolution":
                    if(arg.equals("Default"))
                        GameSettings.Graphics.setResolutionRaw(GameSettings.Graphics.getDefaultResolution());
                    else
                        GameSettings.Graphics.setResolutionRaw(Vector2D.parseVector(arg));
            }
        } else {
            RenderingHints.Key key = GameSettings.Graphics.parseKey(var);
            Object value = GameSettings.Graphics.parseValue(key, arg);

            if(key != null)
                GameSettings.Graphics.setIndividualKey(key, value);
        }
    }
    
    private void checkAudio(Field f) {
        String var = f.getVar();
        String arg = f.getArg();
        
        switch(var) {
            case "master_vol":
                GameSettings.Sounds.setVolume(Integer.parseInt(arg));
                break;
            case "music_vol":
                GameSettings.Sounds.setVolume(AudioManager.findChannel("Music"), Integer.parseInt(arg));
                break;
            case "fx_vol":
                int fxVol = Integer.parseInt(arg);
                GameSettings.Sounds.setVolume(AudioManager.findChannel("Player"), fxVol);
                GameSettings.Sounds.setVolume(AudioManager.findChannel("Interface"), fxVol);
                GameSettings.Sounds.setVolume(AudioManager.findChannel("Zombies"), fxVol);
                GameSettings.Sounds.setVolume(AudioManager.findChannel("Environment"), fxVol);
                break;
            case "radio_vol":
                GameSettings.Sounds.setVolume(AudioManager.findChannel("Radio"), Integer.parseInt(arg));
                break;
            case "radio_fader":
                System.err.println("WIP");
                break;
            case "radio_playstyle":
                switch(arg) {
                    case "Normal":
                        GameSettings.Sounds.setRadioPlaystyle(Radio.PlayStyle.NORMAL);
                        break;
                    case "Shuffle Any":
                        GameSettings.Sounds.setRadioPlaystyle(Radio.PlayStyle.SHUFFLE_ANY);
                        break;
                    case "Shuffle No Repeat":
                        GameSettings.Sounds.setRadioPlaystyle(Radio.PlayStyle.SHUFFLE_NO_REPEAT);
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected Exception Caught: No such PlayStyle.");
                }
                break;
            default:
                throw new IllegalArgumentException("No such audio option!");
        }
    }
    
    private static final String[] template = {"#This is a generated template configuration file for Zombies Mayhem!.",
        "#It allows the user to configure the game according to one's taste.",
        "#Note that if you do not know what the argument does, it is best to leave it at that.", "",
        "#The syntax is simple:", "# - '#' stands for a comment mark. Anything that starts with '#' will be ignored by the parser.",
        "# - '[...]' stands for a section mark. Its only function is to organize the file. The parser tracks these sections and marks them.",
        "# - 'var=boolean', where 'var' is the boolean variable to be modified and 'boolean' a true/false argument",
        "# - 'var=\"arg\"', where 'var' is the string/enum variable to be modified and '\"arg\"' is either an enumeration element or a string. Double quotes are not obligatory.",
        "# - 'var=arg', where 'var' is a numeral variable to be modified and 'arg' is a number in any numerical system (i.e.: hexadecimal, binary, decimal).",
        "# - 'var='arg'', where 'var' is a variable to be modified and 'arg' an ASCII character.",
        "#", "#Most of the options listed in this file are already available through the game's menu. Those not listed there should not be modified by the end-user.",
        "", "# @author Renato Lui Geh"};
}