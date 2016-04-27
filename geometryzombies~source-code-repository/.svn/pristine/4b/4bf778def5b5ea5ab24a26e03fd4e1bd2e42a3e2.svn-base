package geometryzombiesmayhem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * ChatClient. Behaves just like Client.
 * 
 * @see Client
 * 
 * @author Renato Lui Geh
 * @version 0.0.5
 */
public class ChatClient extends Thread {
    private String outcoming;
    private String incoming;
    
    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private boolean isActive = true;
    
    public String host = "localhost";
    public int chatPort = 1235;
    
    public static final String initMessage = "#init";
    private boolean hasNoticed = false;
    
    public ChatClient(String host, int port) {      
        this.host = host;
        this.chatPort = port+1;
        
        incoming = outcoming = initMessage;
        
        System.err.println(incoming);
        
        initClient();
    }
    
    private void initClient() {
        try {
            this.s = getSocket(chatPort);
        
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());
            
        } catch(IOException exc) {
            System.err.println(exc.getMessage());
        }
    }
    
    private Socket getSocket(int port) {
        try {
            return new Socket(InetAddress.getByName(host), chatPort);
        } catch(IOException exc) {
            System.err.println(exc.getMessage());
            return null;
        }
    }
    
    public String getIncomingMessage() {
        return incoming;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean enabled) {
        this.isActive = enabled;
    }
    
    public void toggleActive() {
        this.isActive = !isActive;
    }
    
    public String getOutcomingMessage() {
        return outcoming;
    }
    
    private void handleConnection() {
        try {
            while(true) {
                manageChat();
                out.reset();
            }
        } catch(IOException | ClassNotFoundException exc) {
            System.err.println(exc.getMessage());
        }
    }
    
    private void manageChat() throws IOException, ClassNotFoundException {
//        if(isActive) {
//            outcoming = new Scanner(System.in).nextLine();
//            out.writeObject(outcoming);
//        } else {
//            outcoming = ChatClient.initMessage;
//            out.writeObject(outcoming);
//        }
        
        if(in.readObject().equals("true")) {
            out.writeObject(outcoming);
            incoming = (String)in.readObject(); 
            
        } else {
//            if(!hasNoticed) {
//                System.err.println("Waiting for another player...");
//                hasNoticed = true;
//            }
        }
//        if(!incoming.equals(ChatClient.initMessage)) {
//            incoming = (String)in.readObject();
//        } else {
//            in.readObject();
//        }
    }
    
    public void writeLine(String line) {
        outcoming = line;
    }
    
    @Override
    public void run() {
        handleConnection();
    }
}