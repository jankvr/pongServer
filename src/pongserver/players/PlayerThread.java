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
 * Trieda reprezentujúca hráča na serverovej strane
 * @author Jan Kovář, Jarovlas Fedorčák
 */
public class PlayerThread implements Runnable {
    private final ReentrantLock lock =  new ReentrantLock(); 
    
    private double xPosition;
    private double yPosition;
    private int length=120;
    private /*final*/ String name;
    private final Socket socket;
    private final Server server;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean alive;
    private CmdParser parser;
    private PlayerThread opponent;
    private Game game;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PlayerThread.class.getName());
    

    /**
     * Konstruktor, ktery prirazuje socket, server a jmeno hrace.
     * @param name jmeno hrace
     * @param socket konkretni socket, ktery se pouziva pro komunikaci se serverem
     * @param server odkaz na hlavni serverovou tridu kvuli pripadnemu odstraneni connection
     */
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
            LOG.fatal(ex.getMessage());
        }
    }
    
    /**
     * Určuje triede, ktorý hráč je jej oponent
     * @param game hra, ktorú hráč hraje
     */
    public void setOpponent(Game game) {
        this.game = game;
        
        if (this.equals(game.getPlayer1())) {
            this.opponent = game.getPlayer2();
        }
        else {
            this.opponent = game.getPlayer1();
        }

    }
    
    /**
     * vracia dĺžku odrážacej plochy hráča
     * @return dĺžka odrážacej plochy hráča
     */
    public double getLength(){
        return length;
    }
    
    /**
     * vracia x-ovú pozíciu hráča
     * @return x-ová pozícia hráča
     */
    public double getxPosition() {
        return xPosition;
    }

    /**
     * nastavuje x-ovú pozíciu hráča
     * @param xPosition x-ová pozícia hráča
     */
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * vracia y-ovú pozíciu hráča
     * @return y-ová pozícia hráča 
     */
    public double getyPosition() {
        return yPosition;
    }

    /**
     * nastavuje y-ovú pozíciu hráča
     * @param yPosition y-ová pozícia hráča
     */
    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }
    
    /**
     * vracia informáciu o tom, či pádlo je v kolízii s loptou
     * @param ball lopta danej hry
     * @return true, pokiaľ sa dotýka lopty
     */
    public boolean meets(Ball ball){
        return (ball.getyPosition()<this.yPosition+length && 
                ball.getyPosition()>this.yPosition)&&
                (ball.getxPosition()==this.xPosition);
    }

    /**
     * vracia meno hráča
     * @return meno hráča
     */
    public String getName() {
        return name;
    }
    
    /**
     * nastavuje meno hráča
     * @param name meno hráča
     */
    public void setName(String name){
        this.name=name;
    }
    
    /**
     * Hlavni metoda player threadu, ktera zajistuje opakovane prijimani zprav ze strany hrace (klienta) na server a odesilani zprav server -> hrac (klient).
     * V pripade chyby se zprava zaloguje a ukonci se spojeni.
     * 
     */
    @Override
    public void run() {
        try {
            
            while (alive) {

                String message = inputStream.readUTF();

                if (message.equals("QUIT")) {
                    alive = false;
                    game.getLogin().removeDisconnectedUser(this.name);
                }
                
                String command = parser.parseCommand(message);
                
                if (command.equals("OPPONENTPOSITION")) {
                    this.setPosition(message);
                }
                
                sendData(message);
                
                Thread.sleep(2);
                }
            }
        
        catch (SocketException ex) {
            LOG.fatal(ex.getMessage());
            //game.getLogin().removeDisconnectedUser(this.name);
            
        } 
        catch (EOFException | InterruptedException ex) {
            LOG.fatal(ex.getMessage());
        }
        catch (IOException ex) {
            LOG.fatal(ex.getMessage());
            game.getLogin().removeDisconnectedUser(this.name); //login potrebuje vedieť, že môže znova pripojiť hráča keď sa ohlási
        }
        finally {
            try {
                server.removeConnection(this);
                game.getLogin().removeDisconnectedUser(name);
                sendData("OPPONENTNNECTED");
            } catch (IOException ex) {
                Logger.getLogger(PlayerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * vracia socket
     * @return socket
     */
    public Socket getSocket() {
        return socket;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Equals metoda pro porovnavani dvou threadu.
     * @param obj porovnavany objekt
     * @return true, pokud jsou dva thready stejne, false v opacnem pripade
     */
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
    
    /**
     * vracia svoj outputstream
     * @return outputstream
     */
    public DataOutputStream getOutputStream() {
        return outputStream;
    }
    
    /**
     * vracia svoj inputstream
     * @return inputstream
     */
    public DataInputStream getInputStream() {
        return inputStream;
    }
    
    /**
     * posiela správu oponentovi
     * @param dataToSend správa oponentovi
     * @throws IOException 
     */
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

    /**
     * nastavuje pozíciu v oboch rozmeroch
     * @param message správa posielaná na parser
     */
    private void setPosition(String message) {
        this.xPosition = this.parser.parsePosition("x",message);
        this.yPosition = this.parser.parsePosition("y",message);
    }
}
