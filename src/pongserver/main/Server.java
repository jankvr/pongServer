/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import pongserver.game.Game;
import pongserver.players.PlayerThread;

/**
 * Serverová trieda spravujúca pripájanie cez sieť a ostatné prípadné potrebné
 * činnosti
 * 
 * @author Jan kovář, Jaroslav Fedorčák
 */
public class Server implements Runnable {
    
    private List<PlayerThread> playerQueue;
    private ServerSocket serverSocket;
    private Map<PlayerThread, DataOutputStream> outputStreams;
    private IGui gui;
    private int index;
    private static final int NEEDED_PLAYERS = 2;
    private Login login;
    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    
    /**
     * Konstruktor.
     * @param port port, na kterem nasloucha server
     * @param gui odkaz na gui okno serveru (pro vypisovani hlasek)
     */
    public Server(int port, IGui gui) {
        
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            this.gui = gui;
            this.outputStreams = new HashMap<>();
            this.playerQueue = new ArrayList<>();
            this.serverSocket = new ServerSocket(port);
            this.login = new Login();
            this.index = 0;

            new Thread(this).start();
        } catch (IOException ex) {
            this.gui.appendMessage(ex.getMessage());
            LOG.fatal(ex.getMessage());
        }
    }
    
    /**
     * prijímanie pripojovaných klientov
     * @throws IOException 
     */
    private void listen() throws IOException {
        gui.appendMessage("Starting to listen on " + serverSocket);
        LOG.info("Starting to listen on " + serverSocket);
        
        //infinite loop
        while (true) {
            Socket socket = serverSocket.accept();
            
            PlayerThread playerThread = new PlayerThread("p" + this.index, socket, this);
            
            this.index++;
            
            playerQueue.add(playerThread);
            gui.appendMessage("Connected on " + socket);
            LOG.info("Connected on " + socket);
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            //save outputstream
            outputStreams.put(playerThread, outStream);
            
            boolean loginIsFine = false;
            while(!loginIsFine){

                String loginMessage = playerThread.getInputStream().readUTF(); 
                String[] tokens = loginMessage.split(" ");  
                
                    if(tokens[0].equals("LOGIN")){
                    switch (login.check(tokens[1], tokens[2])) {
                        case "OK":
                            loginIsFine=true;
                            outStream.writeUTF("LOGIN OK");
                            playerThread.setName(tokens[1]);
                            break;
                        case "ALREADYCONNECTED":
                            outStream.writeUTF("LOGIN ALREADYCONNECTED");
                            break;
                        default:
                            outStream.writeUTF("LOGIN WRONG");
                            break;
                    }

                    }
                    else{
                        LOG.warn("Bad username");
                        
                    }
                
            }
            
            
            serverStatus();
            
            if (playerQueue.size() >= NEEDED_PLAYERS) {
                //start the game
                
                //remove first two players from list
                List<PlayerThread> retrievePlayers = this.retrievePlayers();
                
                Game game = new Game(retrievePlayers.get(0), retrievePlayers.get(1),login);
                new Thread(game).start();
                
                gui.appendMessage("####STARTING_GAME####");
                gui.appendMessage(retrievePlayers.get(0).getName());
                gui.appendMessage(retrievePlayers.get(1).getName());
                gui.appendMessage("#####################");
                
                LOG.info("STARTING THE GAME. PLAYERS: " + retrievePlayers.get(0).getName() + ", " + retrievePlayers.get(1).getName());

                serverStatus();
            }
        }
    }

    /**
     * Hlavni metoda, ktera zajistuje to, ze server zavola metodu listen, coz je cyklus prijimani a odesilani zprav
     */
    @Override
    public void run() {
        try {
            listen();
        } catch (IOException ex) {
            gui.appendMessage(ex.getMessage());
        }
    }
    
    /**
     * Popisuje stav servra
     */
    public void serverStatus() {
        gui.appendMessage("Current player status on server: " + playerQueue.size() + " player(s) waiting");
        LOG.info("Current player status on server: " + playerQueue.size() + " player(s) waiting");
    }
    
    public List<PlayerThread> retrievePlayers() {
        List<PlayerThread> retrieveList = new ArrayList<>();
        
        List<PlayerThread> tempList = playerQueue.subList(0, 2);
        
        tempList.stream().forEach((p) -> {
            retrieveList.add(p);
        });
        
        playerQueue.subList(0, 2).clear();
        
        
        return retrieveList;
    }
    
    /**
     * Spravovanie klienta, po tom, čo sa odpojil
     * @param player hráč, ktorý sa odpojil
     * @throws IOException 
     */
    public void removeConnection(PlayerThread player) throws IOException {
        
        //delete map entry
        outputStreams.remove(player.getSocket());
        playerQueue.remove(player);
        
        //and close socket
        player.getSocket().close();
        
        gui.appendMessage("Socket " + player.getName() + " closed, " + playerQueue.size() + " player(s) waiting");
        LOG.info("Socket " + player.getName() + " closed, " + playerQueue.size() + " player(s) waiting");
    }
    
    
}
