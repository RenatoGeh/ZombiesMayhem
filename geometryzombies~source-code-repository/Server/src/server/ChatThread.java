package server;

import geometryzombiesmayhem.ChatClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ChatThread. Behaves like ServerThread.
 * 
 * @see ServerThread
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class ChatThread extends Thread {
    private Socket s;
    private int id;
    private ChatHandler parent;
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private String message = ChatClient.initMessage;
    private String lastMessage = "";
    
    public ChatThread(Socket s, int id, ChatHandler parent) {
        this.s = s;
        this.id = id;
        
        this.parent = parent;
    }
    
    private void initIO() {
        try {
            parent.server.writeLine("Initializing I/O Chat Streams...");
            
            out = new ObjectOutputStream(s.getOutputStream());
            parent.server.writeLine("Output Chat Stream initialized!");
            
            in = new ObjectInputStream(s.getInputStream());
            parent.server.writeLine("Input Chat Stream initialized!");
            
            parent.server.writeLine("Connected to " + s.getInetAddress().toString() + ".");
            
        } catch(IOException exc) {
            parent.server.writeLine("Error on I/O Stream: \n" + exc.getMessage());
        }
    }
    
    private void handleConnection() {
        try {
            while(true) {
                manageChat();
                out.reset();
            }
        } catch(IOException | ClassNotFoundException exc) {
            parent.server.writeLine("I/O Exception or Class Not Found Exception: \n" + exc.getMessage());
        }
    }
    
    private void manageChat() throws IOException, ClassNotFoundException {        
        if(parent.clientCount >= 2) {
            out.writeObject("true");
            
            message = (String)in.readObject();
            
            if(!message.equals("#init")) {
                if(lastMessage.isEmpty()) {
                    lastMessage = message;
                    parent.server.writeOnChat(message);
                }
                else if(!lastMessage.equals(message)) {
                    parent.server.writeOnChat(message);
                    lastMessage = message;
                }
            }

            parent.messages.set(id, message);

            for(int i=0;i<parent.messages.size();i++) {
                if(i != id) {
                    out.writeObject(parent.messages.get(i));
                }
            }
        } else {

        }
    }
    
    @Override
    public void run() {
        initIO();
        handleConnection();
    }
}