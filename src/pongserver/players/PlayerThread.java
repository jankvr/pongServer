/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.players;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import pongserver.game.Ball;
import pongserver.game.Game;
import pongserver.main.CmdParser;
import pongserver.main.Server;

/**
 *
 * @author User
 */
public class PlayerThread implements Runnable {
    private final ReentrantLock lock =  new ReentrantLock(); 
    
    private int xPosition;
    private int yPosition;
    private int halfLength=50;
    private final String name;
    private final Socket socket;
    private final Server server;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean alive;
    private CmdParser parser;
    private PlayerThread opponent;


    public PlayerThread(String name, Socket socket, Server server) {
        this.name = name;
        this.socket = socket;
        this.server = server;
        this.alive = true;
                this.parser = new CmdParser();
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(PlayerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setOpponent(Game game) {
        if (this.equals(game.getPlayer1())) {
            this.opponent = game.getPlayer2();
        }
        else {
            this.opponent = game.getPlayer1();
        }

    }
    
    public double getLength(){
        return halfLength*2;
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
        System.out.println("----------------");
        System.out.println("ball xposition = "+ball.getxPosition());
        System.out.println("ball yposition = "+ball.getyPosition());
        System.out.println("this xposition = "+this.xPosition);
        System.out.println("this yposition = "+this.yPosition);
        System.out.println("this yposition+halflength = "+(this.yPosition+halfLength));
        System.out.println("this yposition-halflength = "+(this.yPosition-halfLength));
        
        if ((ball.getyPosition()<this.yPosition+halfLength && 
                ball.getyPosition()>this.yPosition-halfLength)&&
                (ball.getxPosition()<=this.xPosition)){
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
            
            while (alive) {

                String message = inputStream.readUTF();

                if (message.equals("QUIT")) {
                    alive = false;
                }
                
                sendData(message);
                
                Thread.sleep(2);
                }
            }
        
        catch (SocketException ex) {
            System.out.println(ex);
        }
        catch (EOFException ex) {
            System.out.println(ex);
        } 
        catch (IOException ex) {
            System.out.println(ex);
        }
        catch (InterruptedException ex) {
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
    
    public DataOutputStream getOutputStream() {
        return outputStream;
    }
    
    public DataInputStream getInputStream() {
        return inputStream;
    }
    
    public void sendData(String dataToSend) throws IOException {
        // zamknuti posilani zpravy oponentovi
        try {
            lock.lock();

            opponent.getOutputStream().writeUTF(dataToSend);
            opponent.getOutputStream().flush();
        }
        finally {
             lock.unlock();
        }
    }
    //Jaro: prípadne vymazať
    public void receiveDataFromClient() throws IOException{
        try{
            lock.lock();
            String message1 = inputStream.readUTF();
            parser.parse(this, message1);
        }
        finally{
            lock.unlock();
        }
    }
}
