package geometryzombiesmayhem;

import geometryzombiesmayhem.ConfigurationFile.Field;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

public final class GameSettings {
    public static class Controls {
        private final static Map<Integer, String> DEFAULT_CONTROLS_MAP = new LinkedHashMap<>();
        
        static {
            initDefault();
        }
        
        public static void reset(Map<Integer, String> controls) {
            controls.clear();
            controls.putAll(DEFAULT_CONTROLS_MAP);
        }
        
        public static void replaceKey(String value, Map<Integer, String> controls) {
            Set<Map.Entry<Integer, String>> entries = DEFAULT_CONTROLS_MAP.entrySet();
            int key = -1;
            
            for(Iterator it = entries.iterator();it.hasNext();) {
                Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>)it.next();
                if(entry.getValue().equals(value)) 
                    key = entry.getKey();
            }
            
            if(key != -1)
                controls.remove(key);
        }
        
        public static void saveIndividualKey(Integer key, String value, boolean replaces) {
            if(!DEFAULT_CONTROLS_MAP.containsValue(value)) {
                throw new IllegalArgumentException("No such key or value!");
            } else {
                if(replaces)
                    GameSettings.Controls.replaceKey(value, DEFAULT_CONTROLS_MAP);
                
                DEFAULT_CONTROLS_MAP.put(key, value);
            }
        }
        
        public static int getKey(String s) {           
            if(s.length() == 1)
                return Character.toTitleCase(s.charAt(0));
            else if(s.startsWith("num"))
                return s.charAt(3);
            else {
                switch(s) {
                    case "button1":
                        return MouseEvent.BUTTON1;
                    case "button2":
                        return MouseEvent.BUTTON2;
                    case "button3":
                        return MouseEvent.BUTTON3;
                    case "shift":
                        return KeyEvent.VK_SHIFT;
                    case "ctrl":
                        return KeyEvent.VK_CONTROL;
                    case "alt":
                        return KeyEvent.VK_ALT;
                    case "space":
                        return KeyEvent.VK_SPACE;
                    case "esc":
                        return KeyEvent.VK_ESCAPE;
                    case "tab":
                        return KeyEvent.VK_TAB;
                    case "backspace":
                        return KeyEvent.VK_BACK_SPACE;
                    case "left_arrow":
                        return KeyEvent.VK_LEFT;
                    case "right_arrow":
                        return KeyEvent.VK_RIGHT;
                    case "up_arrow":
                        return KeyEvent.VK_UP;
                    case "down_arrow":
                        return KeyEvent.VK_DOWN;
                }
            }
            throw new IllegalArgumentException("Character not recognized!");
        }
        
        public static boolean containsValue(String value) {return DEFAULT_CONTROLS_MAP.containsValue(value);}
        public static boolean containsKey(Integer key) {return DEFAULT_CONTROLS_MAP.containsKey(key);}
        public static Map<Integer, String> getMap() {return DEFAULT_CONTROLS_MAP;}
        
        public static String getValue(Integer key) {return DEFAULT_CONTROLS_MAP.get(key);}
        
        private static void initDefault() {
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_W, "up");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_A, "left");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_D, "right");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_S, "down");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_SHIFT, "sprint");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_SPACE, "bullet_time");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_F, "fire_mode");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_E, "interact");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_R, "reload");
            DEFAULT_CONTROLS_MAP.put(KeyEvent.VK_V, "flashlight");
            DEFAULT_CONTROLS_MAP.put(MouseEvent.BUTTON3, "aim");
            DEFAULT_CONTROLS_MAP.put(MouseEvent.BUTTON1, "shoot");
            DEFAULT_CONTROLS_MAP.put(MouseEvent.BUTTON2, "mouse_secondary");
        }
    }
    public static class Graphics {
        private static Map<Key, Object> settings = new LinkedHashMap<>();

        private final static Map<Key, Object> DEFAULT_SETTINGS_MAP = new LinkedHashMap<>();
        private final static Map<Key, Object> PERFORMANCE_SETTINGS_MAP = new LinkedHashMap<>();
        private final static Map<Key, Object> BALANCED_SETTINGS_MAP = new LinkedHashMap<>();
        private final static Map<Key, Object> QUALITY_SETTINGS_MAP = new LinkedHashMap<>();
                
        private final static Vector2D DEFAULT_RESOLUTION = new Vector2D(800+17, 650);
        private static Vector2D currentResolution = DEFAULT_RESOLUTION;
        private static Vector2D insets;
        
        private static boolean isResizable = true;
        
        /**
         * Represents the filter according to <code>ImageManipulator</code>'s filter operations.
         */
        public static enum Filter {Default, Blur, Brighten, Darken, Negative, Gray};
        private static Filter currFilter;
        
        public static enum ScreenMode {Window, Borderless, Fullscreen};
        private static ScreenMode currScreen;
        
        static {          
            initDefault();
            initPerformance();
            initBalanced();
            initQuality();
            
            currFilter = Filter.Default;
            
            init();
        }

        /**
        * Initializes the game settings. This method is automatically called upon initialization.
        */
        private static void init() {reset();}
        
        /**
        * Sets a pre-defined settings set.
        * 
        * @param type 
        */
        public static void setSettings(int type) {
            if(!settings.isEmpty())
                settings.clear();

            if(type == SETTINGS_GRAPHICS_PERFORMANCE)
                settings.putAll(PERFORMANCE_SETTINGS_MAP);
            else if(type == SETTINGS_GRAPHICS_BALANCED)
                settings.putAll(BALANCED_SETTINGS_MAP);
            else if(type == SETTINGS_GRAPHICS_QUALITY)
                settings.putAll(QUALITY_SETTINGS_MAP);
            else if(type == SETTINGS_GRAPHICS_DEFAULT)
                settings.putAll(DEFAULT_SETTINGS_MAP);
        }
        
        /**
         * <p>Gets the current filter the renderer is using.</p>
         * 
         * @return The current filter. 
         */
        public static Filter getFilter() {return currFilter;}
        
        /**
         * <p>Sets the current filter to the given element.</p>
         * 
         * @param f Filter to replace.
         */
        public static void setFilter(Filter f) {
            currFilter = f;
            MutationManager.filter(f);
        }
        
        /**
        * Sets an individual settings item to the given value. 
        * If no <code>key</code> or <code>value</code> is found, 
        * <code>IllegalArgumentException</code> is thrown.
        * 
        * @param key
        * @param value 
        */
        public static void setIndividualKey(Key key, Object value) {
            if(!settings.containsKey(key)) {
                throw new IllegalArgumentException("No such key or value!");
            } else {
                settings.put(key, value);
            }    
        }
        
        public static boolean isResizable() {return isResizable;}
        public static void setResizable(boolean resizable) {isResizable = resizable;}
        
        public static void setResolutionRaw(Vector2D res) {currentResolution = res;}
        
        public static void setResolution(Vector2D res) {
            if(ZM.main.isFunctional) 
                setResolution(res, ZM.main);
            else 
                setResolution(res, ZM.gameFrame);
        }
        
        public static void setResolution(Vector2D res, JFrame f) {
            GameSettings.Graphics.currentResolution = res;
            
            f.setSize((int)res.getX(), (int)res.getY());
            centerWindow(f);
        }
        
        public static void centerWindow(Component c) {
            Vector2D screenSize = Vector2D.convertFrom(ZM.TOOLKIT.getScreenSize());
                
            int dx = (int)(screenSize.getX() - currentResolution.getX())/2;
            int dy = (int)(screenSize.getY() - currentResolution.getY())/2;
            
            c.setLocation(dx, dy);
        }

        /**
         * Applies the <code>RenderingHints</code> to the <code>Graphics2D</code> instance.
         * 
         * @param g2 
         */
        public static void apply(Graphics2D g2) {
            g2.addRenderingHints(settings);
        }

        /**
        * Resets the game settings to <b>DEFAULT</b>.
        */
        public static void reset() {
            setSettings(SETTINGS_GRAPHICS_DEFAULT);
        }
        
        public static Vector2D getDefaultResolution() {
            return DEFAULT_RESOLUTION.copy();
        }
        
        public static Vector2D getResolution() {
            return currentResolution;
        }
        
        public static Vector2D getInsets() {return insets;}
        
        public static void handleScreenMode(final JFrame c) {
            c.pack();

            Vector2D inset = new Vector2D(c.getInsets().left + c.getInsets().right, 
                    c.getInsets().top + c.getInsets().bottom);
            insets = inset;
//            System.out.println(inset);
//            DEFAULT_RESOLUTION.subtract(inset);
//            currentResolution.subtract(inset);
            
            c.dispose();
//            c.setVisible(false);
//            c.setEnabled(false);

            switch(currScreen) {
                case Fullscreen:
                    setFullscreen(c, inset);
                    insets.zero();
                    return;
                case Borderless:
                    c.setUndecorated(true);
                    setResolution(getResolution().subtract(0,inset.getY()), c);
                    insets.zero();
                    break;
                default:
                    c.setUndecorated(false);
                    setResolution(getResolution().add(inset.getX(),0), c);
                    break;
            }
            
            AwesomeTimer.addAction(new AwesomeAction() {
                        @Override
                        public void run() {
                            c.setVisible(true);
                            if(GameSettings.Graphics.getScreenMode().equals(GameSettings.Graphics.ScreenMode.Window))
                                GameSettings.Graphics.setInsets(new Vector2D(c.getInsets().left + c.getInsets().right, 
                                    c.getInsets().top + c.getInsets().bottom));
                        }
                    }, 500, false, TimeUnit.MILLISECONDS);
            
            c.setSize((int)GameSettings.Graphics.getResolution().getX(), (int)GameSettings.Graphics.getResolution().getY());
        }
        
        protected static void setInsets(Vector2D inset) {insets = inset;}
        
        public static ScreenMode getScreenMode() {return currScreen;}
        public static void setScreenMode(ScreenMode mode) {currScreen = mode;}
        
        public static void setFullscreen(JFrame c, Vector2D inset) {
            c.setUndecorated(true);
            c.setResizable(false);
            
            c.validate();
            
            if(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported())
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(c);
            else
                throw new IllegalArgumentException("System does not support Fullscreen Mode!");
            
            //GameSettings.Graphics.setResolution(Vector2D.convertFrom(ZM.TOOLKIT.getScreenSize()).subtract(inset), c);
            currentResolution = Vector2D.convertFrom(ZM.TOOLKIT.getScreenSize());
            
            UserInterface.fixResolution((int)currentResolution.getX(), (int)currentResolution.getY(),
                    DEFAULT_RESOLUTION.subtract(inset));
        }
        
        @Deprecated
        public static void setFullscreen(JFrame c) {
            c.setUndecorated(true);
            c.setResizable(false);
            
            c.validate();
            
            if(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported())
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(c);
            else
                throw new IllegalArgumentException("System does not support Fullscreen Mode!");
            
            GameSettings.Graphics.setResolution(Vector2D.convertFrom(ZM.TOOLKIT.getScreenSize()), c);
        
            UserInterface.fixResolution((int)currentResolution.getX(), (int)currentResolution.getY(),
                    DEFAULT_RESOLUTION);
        }
        
        public static RenderingHints getHints() {return new RenderingHints(settings);}
        public static RenderingHints getHints(int type) {
            if(type == SETTINGS_GRAPHICS_PERFORMANCE)
                return new RenderingHints(PERFORMANCE_SETTINGS_MAP);
            else if(type == SETTINGS_GRAPHICS_BALANCED)
                return new RenderingHints(BALANCED_SETTINGS_MAP);
            else if(type == SETTINGS_GRAPHICS_QUALITY)
                return new RenderingHints(QUALITY_SETTINGS_MAP);
            else if(type == SETTINGS_GRAPHICS_DEFAULT)
                return new RenderingHints(DEFAULT_SETTINGS_MAP);
            else
                throw new IllegalArgumentException("No such pre-defined type!");
        }
        
        public static RenderingHints.Key parseKey(String var) {
            switch(var) {
                case "anti_aliasing":
                    return RenderingHints.KEY_ANTIALIASING;
                case "alpha_interpolation":
                    return RenderingHints.KEY_ALPHA_INTERPOLATION;
                case "color_rendering":
                    return RenderingHints.KEY_COLOR_RENDERING;
                case "dithering":
                    return RenderingHints.KEY_DITHERING;
                case "fractional_metrics":
                    return RenderingHints.KEY_FRACTIONALMETRICS;
                case "rendering":
                    return RenderingHints.KEY_RENDERING;
                case "stroke_control":
                    return RenderingHints.KEY_STROKE_CONTROL;
                case "text_anti_aliasing":
                    return RenderingHints.KEY_TEXT_ANTIALIASING;
                case "interpolation":
                    return RenderingHints.KEY_INTERPOLATION;
            }
            
            if(var.equals("resizable") || var.equals("resolution") || 
                    var.equals("screen_mode") || var.equals("filter"))
                return null;
            
            throw new IllegalArgumentException("No such Key name!");
        }
        
        public static Object parseValue(Key var, String arg) {
            if(var.equals(RenderingHints.KEY_ANTIALIASING)) {
                switch(arg) {
                    case "Default":
                        return RenderingHints.VALUE_ANTIALIAS_DEFAULT;
                    case "true":
                        return RenderingHints.VALUE_ANTIALIAS_ON;
                    case "false":
                        return RenderingHints.VALUE_ANTIALIAS_OFF;
                }
            } else if(var.equals(RenderingHints.KEY_ALPHA_INTERPOLATION)) {
                switch(arg) {
                    case "Quality":
                        return RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
                    case "Default":
                        return RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT;
                    case "Speed":
                        return RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
                }
            } else if(var.equals(RenderingHints.KEY_COLOR_RENDERING)) {
                switch(arg) {
                    case "Quality":
                        return RenderingHints.VALUE_COLOR_RENDER_QUALITY;
                    case "Default":
                        return RenderingHints.VALUE_COLOR_RENDER_DEFAULT;
                    case "Speed":
                        return RenderingHints.VALUE_COLOR_RENDER_SPEED;
                }
            } else if(var.equals(RenderingHints.KEY_DITHERING)) {
                switch(arg) {
                    case "Default":
                        return RenderingHints.VALUE_DITHER_DEFAULT;
                    case "true":
                        return RenderingHints.VALUE_DITHER_ENABLE;
                    case "false":
                        return RenderingHints.VALUE_DITHER_DISABLE;
                }
            } else if(var.equals(RenderingHints.KEY_FRACTIONALMETRICS)) {
                switch(arg) {
                    case "Default":
                        return RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
                    case "true":
                        return RenderingHints.VALUE_FRACTIONALMETRICS_ON;
                    case "false":
                        return RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
                }
            } else if(var.equals(RenderingHints.KEY_TEXT_ANTIALIASING)) {
                switch(arg) {
                    case "Default":
                        return RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
                    case "true":
                        return RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
                    case "false":
                        return RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
                }
            } else if(var.equals(RenderingHints.KEY_INTERPOLATION)) {
                switch(arg) {
                    case "Bicubic":
                        return RenderingHints.VALUE_INTERPOLATION_BICUBIC;
                    case "Bilinear":
                        return RenderingHints.VALUE_INTERPOLATION_BILINEAR;
                    case "NN":
                        return RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
                }
            } else if(var.equals(RenderingHints.KEY_RENDERING)) {
                switch(arg) {
                    case "Default":
                        return RenderingHints.VALUE_RENDER_DEFAULT;
                    case "Quality":
                        return RenderingHints.VALUE_RENDER_QUALITY;
                    case "Speed":
                        return RenderingHints.VALUE_RENDER_SPEED;
                }
            } else if(var.equals(RenderingHints.KEY_STROKE_CONTROL)) {
                switch(arg) {
                    case "Default":
                        return RenderingHints.VALUE_STROKE_DEFAULT;
                    case "Normalize":
                        return RenderingHints.VALUE_STROKE_NORMALIZE;
                    case "Pure":
                        return RenderingHints.VALUE_STROKE_PURE;
                }
            }
            
            throw new IllegalArgumentException("No such value name!");
        }
        
        private static void initDefault() {
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
            DEFAULT_SETTINGS_MAP.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }

        private static void initPerformance() {
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            PERFORMANCE_SETTINGS_MAP.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }

        private static void initBalanced() {
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            BALANCED_SETTINGS_MAP.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }

        private static void initQuality() {
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            QUALITY_SETTINGS_MAP.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
    }
    
    public static class Sounds {
        public static void setVolume(int vol) {AudioManager.setVolume(vol);}
        public static void setVolume(SoundChannel sc, int vol) {AudioManager.setVolume(sc, vol);}
        
        public static void setRadioPlaystyle(Radio.PlayStyle ps){Radio.setStyle(ps);}
    }
    
    public static ArrayList<Field> getFields() {
        
        ArrayList<Field> fields = new ArrayList<>();
        
        //Controls:
        Integer[] controlKeys = GameSettings.Controls.DEFAULT_CONTROLS_MAP.keySet().toArray(new Integer[0]);
        for(int i=0;i<controlKeys.length;i++) {
            String s;
            
            switch(controlKeys[i]) {
                case MouseEvent.BUTTON1:
                    s = "button1";
                    break;
                case MouseEvent.BUTTON2:
                    s = "button2";
                    break;
                case MouseEvent.BUTTON3:
                    s = "button3";
                    break;
                case KeyEvent.VK_SHIFT:
                    s = "shift";
                    break;
                case KeyEvent.VK_CONTROL:
                    s = "ctrl";
                    break;
                case KeyEvent.VK_ALT:
                    s = "alt";
                    break;
                case KeyEvent.VK_SPACE:
                    s = "space";
                    break;
                case KeyEvent.VK_ESCAPE:
                    s = "esc";
                    break;
                case KeyEvent.VK_TAB:
                    s = "tab";
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    s = "backspace";
                    break;
                case KeyEvent.VK_LEFT:
                    s = "left_arrow";
                    break;
                case KeyEvent.VK_RIGHT:
                    s = "right_arrow";
                    break;
                case KeyEvent.VK_UP:
                    s = "up_arrow";
                    break;
                case KeyEvent.VK_DOWN:
                    s = "down_arrow";
                    break;
                default:
                    s = String.valueOf((char)controlKeys[i].intValue()).toLowerCase();
                    break;
            }
            
            fields.add(new Field(GameSettings.Controls.getValue(controlKeys[i]), s, ConfigurationFile.Section.Controls));
        }
        
        //Graphics:
        Key[] graphicKeys = GameSettings.Graphics.settings.keySet().toArray(new Key[0]);
        Object[] graphicValues = new Object[graphicKeys.length];
        
        String[] graphicVar = new String[graphicKeys.length];
        String[] graphicArg = new String[graphicKeys.length];
        
        for(int i=0;i<graphicKeys.length;i++) 
            graphicValues[i] = GameSettings.Graphics.settings.get(graphicKeys[i]);
        
        for(int i=0;i<graphicKeys.length;i++) {
            if(graphicKeys[i].equals(RenderingHints.KEY_ALPHA_INTERPOLATION)) {
                graphicVar[i] = "alpha_interpolation";
                if(graphicValues[i].equals(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT))
                    graphicArg[i] = "Default";
                else if(graphicValues[i].equals(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY))
                    graphicArg[i] = "Quality";
                else
                    graphicArg[i] = "Speed";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_ANTIALIASING)) {
                graphicVar[i] = "anti_aliasing";
                if(graphicValues[i].equals(RenderingHints.VALUE_ANTIALIAS_ON))
                    graphicArg[i] = "true";
                else if(graphicValues[i].equals(RenderingHints.VALUE_ANTIALIAS_OFF))
                    graphicArg[i] = "false";
                else
                    graphicArg[i] = "Default";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_COLOR_RENDERING)) {
                graphicVar[i] = "color_rendering";
                if(graphicValues[i].equals(RenderingHints.VALUE_COLOR_RENDER_QUALITY))
                    graphicArg[i] = "Quality";
                else if(graphicValues[i].equals(RenderingHints.VALUE_COLOR_RENDER_SPEED))
                    graphicArg[i] = "Speed";
                else
                    graphicArg[i] = "Default";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_DITHERING)) {
                graphicVar[i] = "dithering";
                if(graphicValues[i].equals(RenderingHints.VALUE_DITHER_ENABLE))
                    graphicArg[i] = "true";
                else if(graphicValues[i].equals(RenderingHints.VALUE_DITHER_DISABLE))
                    graphicArg[i] = "false";
                else
                    graphicArg[i] = "Default";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_FRACTIONALMETRICS)) {
                graphicVar[i] = "fractional_metrics";
                if(graphicValues[i].equals(RenderingHints.VALUE_FRACTIONALMETRICS_ON))
                    graphicArg[i] = "true";
                else if(graphicValues[i].equals(RenderingHints.VALUE_FRACTIONALMETRICS_OFF))
                    graphicArg[i] = "false";
                else
                    graphicArg[i] = "Default";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_INTERPOLATION)) {
                graphicVar[i] = "interpolation";
                if(graphicValues[i].equals(RenderingHints.VALUE_INTERPOLATION_BICUBIC))
                    graphicArg[i] = "Bicubic";
                else if(graphicValues[i].equals(RenderingHints.VALUE_INTERPOLATION_BILINEAR))
                    graphicArg[i] = "Bilinear";
                else 
                    graphicArg[i] = "NN";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_RENDERING)) {
                graphicVar[i] = "rendering";
                if(graphicValues[i].equals(RenderingHints.VALUE_RENDER_QUALITY))
                    graphicArg[i] = "Quality";
                else if(graphicValues[i].equals(RenderingHints.VALUE_RENDER_SPEED))
                    graphicArg[i] = "Speed";
                else
                    graphicArg[i] = "Default";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_STROKE_CONTROL)) {
                graphicVar[i] = "stroke_control";
                if(graphicValues[i].equals(RenderingHints.VALUE_STROKE_NORMALIZE))
                    graphicArg[i] = "Normalize";
                else if(graphicValues[i].equals(RenderingHints.VALUE_STROKE_PURE))
                    graphicArg[i] = "Pure";
                else
                    graphicArg[i] = "Default";
            } else if(graphicKeys[i].equals(RenderingHints.KEY_TEXT_ANTIALIASING)) {
                graphicVar[i] = "text_anti_aliasing";
                if(graphicValues[i].equals(RenderingHints.VALUE_TEXT_ANTIALIAS_ON))
                    graphicArg[i] = "true";
                else if(graphicValues[i].equals(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF))
                    graphicArg[i] = "false";
                else
                    graphicArg[i] = "Default";
            }     
            
            fields.add(new Field(graphicVar[i], graphicArg[i], ConfigurationFile.Section.Graphics));
        }
        
        fields.add(new Field("resizable", String.valueOf(GameSettings.Graphics.isResizable()), ConfigurationFile.Section.Graphics));
        
        Vector2D resV = GameSettings.Graphics.getResolution();
        String res;
        if(resV.equals(GameSettings.Graphics.getDefaultResolution()))
            res = "Default";
        else
            res = GameSettings.Graphics.getResolution().toString();
        fields.add(new Field("resolution", res, ConfigurationFile.Section.Graphics));
        
        fields.add(new Field("screen_mode", GameSettings.Graphics.getScreenMode().name(), ConfigurationFile.Section.Graphics));
        fields.add(new Field("filter", GameSettings.Graphics.getFilter().name(), ConfigurationFile.Section.Graphics));
        
        //Audio:
        String[] audioVar = {"master_vol", "music_vol", "fx_vol", "radio_vol", "radio_fader", "radio_playstyle"};
        String[] audioArg = new String[audioVar.length];
        
        audioArg[0] = String.valueOf((int)AudioManager.getVolume());
        audioArg[1] = String.valueOf((int)AudioManager.findChannel("Music").getVolume());
        audioArg[2] = String.valueOf((int)ImageManipulator.avArithmetic(AudioManager.findChannel("Player").getVolume(), 
                AudioManager.findChannel("Interface").getVolume(), AudioManager.findChannel("Zombies").getVolume(), 
                AudioManager.findChannel("Environment").getVolume()));
        audioArg[3] = String.valueOf((int)AudioManager.findChannel("Radio").getVolume());
        audioArg[4] = "Only Fade Out"; //WIP
        
        String ps = null;
        switch(Radio.getDetails().getPlayStyle()) {
            case NORMAL:
                ps = "Normal";
            break;
            case SHUFFLE_ANY:
                ps = "Shuffle Any";
            break;
            case SHUFFLE_NO_REPEAT:
                ps = "Shuffle No Repeat";
            break;
            default:
                throw new IllegalArgumentException("Unexpected Exception Caught: No such PlayStyle.");
        }
        audioArg[5] = ps;
        
        for(int i=0;i<audioArg.length;i++)
            fields.add(new Field(audioVar[i], audioArg[i], ConfigurationFile.Section.Audio));
        
        return fields;
    }
    
    public static final int SETTINGS_GRAPHICS_PERFORMANCE = 0;
    public static final int SETTINGS_GRAPHICS_BALANCED = 1;
    public static final int SETTINGS_GRAPHICS_QUALITY = 2;
    public static final int SETTINGS_GRAPHICS_DEFAULT = -1;
}