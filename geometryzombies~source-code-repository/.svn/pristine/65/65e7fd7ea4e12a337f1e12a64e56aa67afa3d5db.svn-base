package geometryzombiesmayhem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

/**
 * Chat Frame. Paint it.
 * 
 * @author Renato Lui Geh
 * @version 0.0.5
 */
public class ChatFrame {
    private Rectangle2D.Float mainRect, inputRect;
    
    private Vector2D position;
    private Vector2D size;
    
    private String input = "";
    private String chatText = "";
    
    private String lastMessage = "";
    
    private boolean denyInit = true;
    
    private Color defaultColor, mainColor, inputColor, textColor;
    
    public static final int CHAR_ENTER = 10;
    public static final int CHAR_BACKSPACE = 8;
    private ChatClient client;
    
    private int maxLength;
    
    private String username;
    
    public ChatFrame(String username, String host, int port) {
        initUsername(username);
        initColors();
        initFrame();
        initClient(host, port);
    }
    
    private void initUsername(String username) {
        this.username = username;
        
        maxLength = 35 + (username.length() + 2);
    }
    
    private void initColors() {
        mainColor = new Color(Color.BLUE.getRed(),
                Color.BLUE.getGreen(), Color.BLUE.getBlue(),
                100);
        mainColor.brighter();
        
        inputColor = new Color(Color.BLUE.getRed(),
                Color.BLUE.getGreen(), Color.BLUE.getBlue(),
                200);
        inputColor.darker();
        
//        mainColor = new Color(0, 0, 0, 100);
//        inputColor = new Color(0, 0, 0, 200);
        
        textColor = Color.WHITE;
    }
    
    private void initFrame() {
        position = Vector2D.TOP_LEFT.copy().add(10, 80);
        size = new Vector2D(320, 350);
        
        mainRect = new Rectangle2D.Float(position.getX(), 
                position.getY(), 
                size.getX(), size.getY());
        inputRect = new Rectangle2D.Float(position.getX(), 
                (float)(position.getY() + mainRect.getHeight()), 
                size.getX(), 20);
    }
    
    private void initClient(String host, int port) {
        client = new ChatClient(host, port);
        client.start();
    }
    
    private void readMessage() {
        String message = client.getIncomingMessage();
        
        if(!message.equals("#init")) {
            if(lastMessage.isEmpty()) {
                lastMessage = message;
                chatText += message;
            }
            else if(!lastMessage.equals(message)) {
                chatText += message;
                lastMessage = message;
            }
        }
    }
    
    private void sendMessage(String s) {
        client.writeLine(s);
    }
    
    public void paint(Graphics2D g2, Component c) {
        defaultColor = g2.getColor();
        g2.setStroke(new BasicStroke(1));
        
        g2.setColor(mainColor);
            g2.draw(mainRect);
            g2.fill(mainRect);
        
        g2.setColor(inputColor);
            g2.draw(inputRect);
            g2.fill(inputRect);
        
        g2.setColor(textColor);
            for(int i=0;i<chatText.length()/maxLength;i++) {
                g2.drawString(chatText.substring(maxLength*i, maxLength*(i+1)), (float)mainRect.getX() + 5,(float)mainRect.getY() + (i+1)*(g2.getFont().getSize2D() + 5));
                
                if(i >= 16)
                    chatText = chatText.substring(maxLength);
            }
            g2.drawString(input, (float)inputRect.getX() + 5, (float)inputRect.getY() + g2.getFont().getSize2D() + 5);
            
        g2.setColor(defaultColor);
        
        readMessage();
    }
    
    public boolean onlyWhitespace(String s) {
        int n = 0;
        
        for(int i=0;i<s.length();i++) {
            if(Character.isSpaceChar(s.charAt(i))) {
                n++;
            }
        }
        
        if(n == s.length())
            return true;
        else
            return false;
    }
    
    public String noInitSpace(String s) {
        int i=0;
        
        while(Character.isSpaceChar(s.charAt(i))) {
            i++;
        }
        
        return s.substring(i);
    }
    
    public boolean keyTyped(KeyEvent event) {       
        if(!denyInit) {
            if(input.length() <= 34) {

                if(event.getKeyChar() == CHAR_BACKSPACE) {
                    try {
                        if(!input.isEmpty()) {
                            input = input.subSequence(0, input.length()-1).toString();
                        }
                    } catch(StringIndexOutOfBoundsException exc) {}
                }

                if(event.getKeyChar() == CHAR_ENTER) {    
                    if(!input.isEmpty() || !onlyWhitespace(input)) {

                        input = username + ": " + input;
                        
                        input = noInitSpace(input);

                        int length = input.length();
                        for(int i=0;i<(maxLength - length);i++) {
                            input += " ";
                        }

                        sendMessage(input);
                        
                        chatText += input;
                        input = "";
                        
                        denyInit = true;
                        
                        return false;
                    } else {
                        input = "";
                        denyInit = true;
                        return false;
                    }
                }

                if(event.getKeyChar() != CHAR_BACKSPACE
                && event.getKeyChar() != CHAR_ENTER)
                    input += event.getKeyChar();

                return true;
            } else {
                if(event.getKeyChar() == CHAR_ENTER) {
                    input = username + ": " + input;
                    
                    sendMessage(input);
                    
                    chatText += input;
                    input = "";
                    return false;
                }
                if(event.getKeyChar() == CHAR_BACKSPACE) {
                    try {
                        if(!input.isEmpty()) {
                            input = input.subSequence(0, input.length()-1).toString();
                        }
                    } catch(StringIndexOutOfBoundsException exc) {}
                }
                return true;
            }
        } else {
            denyInit = false;
            return true;
        }
    }
    
    private static ChatFrame chat;
    private static boolean isActive = false;
    private static boolean isVisible = false;
    
    public static void initialize(String username, String host, int port) {
        chat = new ChatFrame(username, host, port);
        isActive = true;
    } 
    
    public static void setVisible(boolean visible) {isVisible = visible;}
    
    public static ChatFrame getChat() {return chat;}    
    public static boolean isActive() {return isActive;}
    public static boolean isVisible() {return isVisible;}
}