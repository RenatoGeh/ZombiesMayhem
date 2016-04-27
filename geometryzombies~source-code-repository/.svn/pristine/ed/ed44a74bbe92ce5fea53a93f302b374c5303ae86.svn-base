package server;

import geometryzombiesmayhem.Client;
import geometryzombiesmayhem.PlayerPackage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * <b>ServerHandler</b>
 * 
 * <p>
 *  Manages the oncoming connections via sockets.
 * </p>
 * 
 * @see Client
 * @author Renato Lui Geh
 * @version 0.0.5
 */
public class ClientHandler extends Thread {

    private ArrayList<ServerThread> clients = new ArrayList<>();
    public ArrayList<PlayerPackage> packages = new ArrayList<>();
    
    public int clientCount = 0;
    private boolean hasClients = false;
    
    public Server server;
    private int port;

    public ClientHandler(Server server, int port) {
        this.server = server;
        this.port = port;
        
        server.writeLine("Creating Default Packages...");
        packages.add(new PlayerPackage());
        packages.add(new PlayerPackage());
    }

    public void connect(ServerSocket ss) throws java.io.IOException {
        server.writeLine("Listening for connections...");
        
        Socket s = ss.accept();
        ServerThread st = new ServerThread(s, clientCount, this);

        server.writeLine("Connection estabilished with: " + s.getInetAddress().toString());
        server.writeLine("Adding user to clients list...");
        
        clients.add(st);

        server.writeLine("Starting Server Thread...");
        
        st.start();

        clientCount++;
        
        server.writeLine("Client count modified! "
                + "This server now handles " + clientCount + " connections.");
   }

    public void disconnect(int id) {
        server.writeLine("Disconnecting user " + id + "...");
        
        server.writeLine("Interrupting thread...");
        clients.get(id).interrupt();
        server.writeLine("Removing thread from clients list...");        
        clients.remove(id);
        
        server.writeLine("Client removed!");
    }
    
    public void disconnectAll() {
        server.writeLine("Preparing to disconnect ALL users...");
        
        for(int i=0;i<clients.size();i++) {
            disconnect(i);
        }
    }

    private void initServer() {
        try {                     
            server.writeLine("Initializing Server with port " + port + "...");
            ServerSocket ss = new ServerSocket(port);
            server.writeLine("Server initialized!");
            
            while(true)                
                connect(ss);

        } catch(IOException io) {
            System.err.println(io.getMessage());
        }
    }

    public int getClientCount() {
        return clientCount;
    }
    
    @Override
    public void run() {
        initServer();
    }
}