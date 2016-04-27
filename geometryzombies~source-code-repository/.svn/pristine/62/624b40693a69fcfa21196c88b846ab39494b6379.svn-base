package server;

import geometryzombiesmayhem.PlayerPackage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * <b>ServerThread</b>
 * 
 * <p>
 *  Handles the I/O Server->Client operations.
 * </p>
 * 
 * @author Renato Lui Geh
 */
public class ServerThread extends Thread {

    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private PlayerPackage pp1;

    private ClientHandler parent;
    
    private int id;

    public ServerThread(Socket s, int id, ClientHandler parent) {
        this.s = s;
        this.id = id;
        
        this.parent = parent;
    }

    public void initIO() {
        try {
            parent.server.writeLine("Initializing I/O Streams...");
            
            out = new ObjectOutputStream(s.getOutputStream());
            parent.server.writeLine("Output Stream initialized!");
            
            in = new ObjectInputStream(s.getInputStream());
            parent.server.writeLine("Input Stream initialized!");

            parent.server.writeLine("Connected to " + s.getInetAddress().toString() + " .");
        } catch(IOException io) {
            parent.server.writeLine("Error on I/O Stream: \n" + io.getMessage());
        }
    }

    public void testConnection() {
        try {
            System.err.println(in.readObject());
            out.writeObject("Testing...");
            out.reset();
        } catch(IOException | ClassNotFoundException exc) {
            parent.server.writeLine(exc.getMessage());
        }
    }

    public void handleConnection() {
        try {          
            while(true) {
                managePackages();
            }
        } catch(IOException | ClassNotFoundException exc) {
            parent.server.writeLine("I/O Exception or Class Not Found Exception:" + exc.getMessage());
        }
    }
    
    private void managePackages() throws IOException, ClassNotFoundException{
        if(parent.clientCount >= 2) {
            out.writeObject("true");

            pp1 = (PlayerPackage)in.readObject();

            parent.packages.set(id, pp1);

            parent.server.writePosition(pp1.getName() + ": " + pp1.getPosition().toString());

            for(int i=0;i<parent.packages.size();i++) {
                if(i != id) 
                    out.writeObject(parent.packages.get(i));
            }
            out.reset();
        } else {
            out.writeObject("false");
        }
    }
    
    public void closeConnection() {
        try {
            parent.server.writeLine("Closing connection with " + s.getInetAddress().toString() + "...");
            s.close();
            parent.server.writeLine("Connection closed");
            parent.server.writeLine("Closing I/O Streams...");
            out.close();
            in.close();
            parent.server.writeLine("I/O Streams closed!");
        } catch (IOException ex) {
            parent.server.writeLine("I/O Exception: " + ex.getMessage());
        }
    }
    
    public PlayerPackage getCurrentPackage() {
        return pp1;
    }

    @Override
    public void run() {
        initIO();
        //testConnection();
        handleConnection();
        closeConnection();
    }
}