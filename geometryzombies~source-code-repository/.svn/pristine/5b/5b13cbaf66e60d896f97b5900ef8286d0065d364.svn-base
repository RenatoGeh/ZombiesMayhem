package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * A Terminal style command console.
 * 
 * @see InputBox
 * @see TextBox
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class Console {
    private final int MAX_CHARS = 100;
    
    private TextBox textBox;
    private String text;
    private InputBox input;
    private Font font;
    
    public Console(String initialText, int frameWidth) {
        this.text = initialText;
        
        font = new Font(Font.DIALOG_INPUT, Font.PLAIN, 12);
        
        input = new InputBox(new Vector2D(20, 120), 50, font);
        textBox = new TextBox(new Vector2D(0, 0), text + "\n", Color.BLACK, 200, font, MAX_CHARS);
    
        textBox.setFixedSize(frameWidth, 150);
        textBox.setFontColor(Color.WHITE);
        textBox.pagesDown(true);
        
        input.setColor(Color.WHITE);
        
        initCommands();
    }
    
    public void handleText(KeyEvent event) {        
        if(event.getKeyChar() != '`' && event.getKeyChar() != ChatFrame.CHAR_ENTER)
            input.handleText(event);
        
        if(!input.getText().isEmpty()) {
            if(event.getKeyChar() == ChatFrame.CHAR_ENTER) {
                String cmd = input.getText();
                
                textBox.writeLine(cmd);
                try {
                    interpretCmd(cmd);
                } catch(IllegalArgumentException exc) {
                    textBox.writeLine(exc.getMessage());
                }
                
                input.resetText();
            }
        }
    }
    
    public void interpretCmd(String line) {
        if(this.checkComplex(line, COMMAND_HELP) || line.equalsIgnoreCase(COMMAND_HELP)) {
            String list = "List of commands: ";
            
            if(line.split(" ").length <= 1) {
                for(int i=0;i<COMMANDS.size();i++) {
                    if(i < COMMANDS.size() - 1)
                        list += COMMANDS.get(i) + ", ";
                    else
                        list += COMMANDS.get(i);
                }

                textBox.writeLine(list);
                return;
            } else {
                String value = line.split(" ")[1].trim();
                value = value.toLowerCase();
                
                switch(value) {
                    case COMMAND_HELP:
                        textBox.writeLine("If used with no arguments, the 'help' "
                                + "command should list all commands available to "
                                + "the user, including their arguments (parameters)"
                                + ", syntax, how they work and what it does. If the argument "
                                + "is 'help', then this message is shown.");
                        return;
                    case COMMAND_SUICIDE:
                        textBox.writeLine("Syntax: 'suicide'\nCauses the current player to suicide. "
                                + "This command does not accept any parameters. "
                                + "'suicide' is achieved through setting the player's "
                                + "health to 0.");
                        return;
                    case COMMAND_EXIT:
                        textBox.writeLine("Syntax: 'exit'\nCauses the game to exit "
                                + "abnormally. No arguments are necessary for this "
                                + "command. Note that this command should not be "
                                + "used unless extremelly necessary, since it causes "
                                + "the JVM to exit at once.");
                        return;
                    case COMMAND_SPAWN:
                        textBox.writeLine("Syntax: 'spawn <object> <position>'\n"
                                + "Spawns in a given object to the given position. "
                                + "The arguments must be a pre-defined object, such "
                                + "as an Item or a Zombie, and a position in Vector2D "
                                + "parsed format (e.g. [x, y]; x, y; x y or [x y]). ");
                        return;
                    case COMMAND_HEALTH:
                        textBox.writeLine("Syntax: 'health <integer>'\n"
                                + "Sets the current player's health to the amount "
                                + "given by the user. The argument must be an "
                                + "integer from (inclusively) 0 to 100 (inclusively).");
                        return;
                    case COMMAND_SETTINGS:
                        textBox.writeLine("Syntax: 'settings <pre-defined set>'\n"
                                + "Sets the current settings map to the pre-defined set "
                                + "of rendering hints. Parameters can be the following "
                                + "rendering hints sets: 'performance', 'balanced', "
                                + "'quality'. No arguments are case-sensitive.");
                        return;
                    case COMMAND_STAMINA:
                        textBox.writeLine("Syntax: 'stamina <integer>'\n"
                                + "Sets the current player's stamina to the amount "
                                + "given by the user. The argument must be an "
                                + "integer from (inclusively) 0 to 100 (inclusively).");
                        return;
                    case COMMAND_CLEAR:
                        textBox.writeLine("Syntax: 'clear'\n"
                                + "Clears this console completely. No arguments "
                                + "are necessary.");
                    case COMMAND_NOGRAV:
                        textBox.writeLine("Syntax: 'nograv <boolean>'\n"
                                + "Causes the player to ignore gravity. "
                                + "A boolean indicates whether there is no "
                                + "gravity or not.");
                }
            }
            return;
        } if(line.equalsIgnoreCase(COMMAND_SUICIDE)) {
            Player.getDefaultPlayer().kill();
            return;
        } if(line.equalsIgnoreCase(COMMAND_EXIT)) {
            System.exit(0);
            return;
        } if(this.checkComplex(line, COMMAND_SPAWN)) {
            String[] subcoms = {" zombie ", " health ", " ammo ", " crawler "};
            
            String value = line.split(" ")[1];
            int n = -1;

            for(int i=0;i<subcoms.length;i++)
                n = ((subcoms[i].trim().equalsIgnoreCase(value))? i : n);

            String position = line.substring(line.split(" ")[0].length() + subcoms[n].length(), line.length());

            switch(n) {
                case 0:
                    Vector2D v = Vector2D.parseVector(position);

                    Zombie z = new Zombie("Walker", v, Zombie.getDefaultSprite(), 100f);

                    Body.register(z);
                    break;
                case 1:
                    Item.register(Item.createHealthItem(Vector2D.parseVector(position)));
                    break;
                case 2:
                    Item.register(Item.createAmmoItem(Vector2D.parseVector(position), ProjectileType.BULLET));
                    break;
                case 3:
                    Vector2D v1 = Vector2D.parseVector(position);
                    Crawler c = new Crawler("Crawler", v1, Crawler.getDefaultIdleSprite(), 100f);
                    Body.register(c);
                    break;
                default:
                    throw new IllegalArgumentException("No such 'spawn' argument!");
            }
            
            return;
        } if(this.checkComplex(line, COMMAND_HEALTH)) {
            Player.getDefaultPlayer().setHealth(Integer.parseInt(this.getComplexValue(line, COMMAND_HEALTH).trim()));
            return;
        } if(this.checkComplex(line, COMMAND_SETTINGS)) {
            String s = this.getComplexValue(line, COMMAND_SETTINGS);

            if(s.equalsIgnoreCase("performance"))
                GameSettings.Graphics.setSettings(GameSettings.SETTINGS_GRAPHICS_PERFORMANCE);
            else if(s.equalsIgnoreCase("balanced"))
                GameSettings.Graphics.setSettings(GameSettings.SETTINGS_GRAPHICS_BALANCED);
            else if(s.equalsIgnoreCase("quality"))
                GameSettings.Graphics.setSettings(GameSettings.SETTINGS_GRAPHICS_QUALITY);
            else if(s.equalsIgnoreCase("default"))
                GameSettings.Graphics.setSettings(GameSettings.SETTINGS_GRAPHICS_DEFAULT);

            return;
        } if(this.checkComplex(line, COMMAND_STAMINA)) {
            Player.getDefaultPlayer().setStamina(Integer.parseInt(this.getComplexValue(line, COMMAND_STAMINA)));
            return;
        } if(line.equalsIgnoreCase(COMMAND_CLEAR)) {
            this.textBox.clear();
            return;
        } if(this.checkComplex(line, COMMAND_TP)) {
            String pos = this.getComplexValue(line, COMMAND_TP);
            Vector2D newPos = Vector2D.parseVector(pos);
            
            Player.getDefaultPlayer().setPosition(newPos);
            return;
        } if(this.checkComplex(line, COMMAND_NOGRAV)) {
            String bool = this.getComplexValue(line, COMMAND_NOGRAV);
            Player.getDefaultPlayer().setMass(bool.equalsIgnoreCase("true")? 0 : 
                    bool.equalsIgnoreCase("false")? 100F : Player.getDefaultPlayer().getMass());
            return;
        }
        
        throw new IllegalArgumentException("Command '" + line + "' not found! "
                + "Type '" + COMMAND_HELP + "' for available commands.");
    }
    
    private boolean checkComplex(String line, String cmd) {
        if(line.length() > cmd.length()) 
            if(line.substring(0, cmd.length()).equalsIgnoreCase(cmd))
                return true;
        
        return false;
    }
    
    private String getComplexValue(String line, String cmd) {return line.substring(cmd.length()+1, line.length());}
    
    public void paint(Graphics2D g2, Component c) {        
        textBox.paint(g2, c);
        input.paint(g2, c);
        
        Font defaultFont = g2.getFont();
        Color defaultColor = g2.getColor();
        
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        
        g2.drawString("> ", (int)input.getPosition().getX() - 15, (int)input.getPosition().getY());
       
        g2.setFont(defaultFont);
        g2.setColor(defaultColor);
    }
    
    private void initCommands() {
        COMMANDS.add(COMMAND_HELP);COMMANDS.add(COMMAND_SPAWN);
        COMMANDS.add(COMMAND_SUICIDE);COMMANDS.add(COMMAND_KILL);
        COMMANDS.add(COMMAND_EXIT);COMMANDS.add(COMMAND_DISCONNECT);
        COMMANDS.add(COMMAND_GIVE);COMMANDS.add(COMMAND_HEALTH);
        COMMANDS.add(COMMAND_SETTINGS); COMMANDS.add(COMMAND_STAMINA);
        COMMANDS.add(COMMAND_CLEAR); COMMANDS.add(COMMAND_TP);
        COMMANDS.add(COMMAND_NOGRAV);
    }
    
    private final ArrayList<String> COMMANDS = new ArrayList<>();
    
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_SPAWN = "spawn";
    private static final String COMMAND_SUICIDE = "suicide";
    private static final String COMMAND_KILL = "kill";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_DISCONNECT = "disconnect";
    private static final String COMMAND_GIVE = "give";
    private static final String COMMAND_HEALTH = "health";
    private static final String COMMAND_SETTINGS = "settings";
    private static final String COMMAND_STAMINA = "stamina";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_TP ="tp";
    private static final String COMMAND_NOGRAV = "nograv";
    
    private static Console console;
    private static boolean isActive = false;
    private static boolean isVisible = false;
    
    public static void initialize(String initialText, int frameWidth) {
        console = new Console(initialText, frameWidth);
        isActive = true;
    }
    
    public static void setVisible(boolean visible) {isVisible = visible;}
    
    public static boolean isActive() {return isActive;}
    public static boolean isVisible() {return isVisible;}
    public static Console getConsole() {return console;}
}
