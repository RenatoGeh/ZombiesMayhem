package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Chat Handler.
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class ChatHandler extends Thread {
    public Server server;
    
    public ArrayList<String> messages;
    public ArrayList<String> usernames;
    private ArrayList<ChatThread> clients;
    
    public int clientCount = 0;
    private int port;
    
    public ChatHandler(Server server, int port) {
        this.server = server;
        this.port = port + 1;
        
        server.writeLine("Initializing Chat Handler...");
        
        messages = new ArrayList<>();
        usernames = new ArrayList<>();
        clients = new ArrayList<>();
        
        messages.add("#init");
        messages.add("#init");
    }
    
    private void connect(ServerSocket ss) throws IOException {
        server.writeLine("Listening for connections...");
        
        Socket s = ss.accept();
        ChatThread ct = new ChatThread(s, clientCount, this);
        
        server.writeLine("Connection estabilished with: " + s.getInetAddress().toString());
        server.writeLine("Adding user to clients list...");
        
        clients.add(ct);
        
        server.writeLine("Starting Chat Thread...");
        
        ct.start();
        
        clientCount++;
        
        server.writeLine("Client count modified! "
                + "This server now handles " + clientCount + " connections.");
    }
    
    private void initServer() {
        try {
            server.writeLine("Initializing Chat Server with port " + port + "...");
            ServerSocket ss = new ServerSocket(port);
            server.writeLine("Chat Server initialized!");
            
            while(true)
                connect(ss);
            
        } catch(IOException exc) {
            server.writeLine(exc.getMessage());
            System.err.println(exc.getMessage());
        }
    }
    
    @Override
    public void run() {
        initServer();
    }
}