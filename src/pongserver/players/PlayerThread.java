/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.players;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import pongserver.game.Ball;
import pongserver.main.Server;

/**
 *
 * @author User
 */
public class PlayerThread implements Runnable {
    private int xPosition;
    private int yPosition;
    private int halfLength=50;
    private final String name;
    private final Socket socket;
    private final Server server;

    public PlayerThread(String name, Socket socket, Server server) {
        this.name = name;
        this.socket = socket;
        this.server = server;
    }
    
    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
    
    public boolean meets(Ball ball){
        if (ball.getyPosition()<this.yPosition+halfLength && 
                ball.getyPosition()>this.yPosition-halfLength){
            return true;
        }
        else{
            return false;
        }
    }

    public String getName() {
        return name;
    }
    
    @Override
    public void run() {
        try {
            // create input stream
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            
            //infinite loop
            while (true) {
                //String message = inputStream.readUTF();
                
                // do the magic
            }
        } 
        catch (SocketException ex) {
            //System.out.println(ex);
        }
        catch (EOFException ex) {
            System.out.println(ex);
        } 
        catch (IOException ex) {
            System.out.println(ex);
        }
        catch (Exception ex) {
            Logger.getLogger(PlayerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                server.removeConnection(this);
            } catch (IOException ex) {
                Logger.getLogger(PlayerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerThread other = (PlayerThread) obj;
        
        return Objects.equals(this.name, other.name);
    }
    
    
}
