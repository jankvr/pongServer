/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import pongserver.players.Player;

/**
 * Hlavni trida 
 * 
 * @author User
 */
public class Server implements Runnable {
    
    private List<Player> playerQueue;
    private ServerSocket serverSocket;
    private IGui gui;
    
    
    public Server(int port, IGui gui) {
        this.gui = gui;
        try {
            this.playerQueue = new ArrayList<>();
            this.serverSocket = new ServerSocket(port);
            
            //listen();
            new Thread(this).start();
        } catch (IOException ex) {
            this.gui.appendMessage(ex.getMessage());
        }
    }
    
    private void listen() throws IOException {
        gui.appendMessage("Starting to listen on " + serverSocket);
        
        //infinite loop
        while (true) {
            Socket socket = serverSocket.accept();
            
            gui.appendMessage("Connected on " + socket);

            //playerQueue.add(new Player(playerQueue.size()));

            serverStatus();
            
            if (playerQueue.size() > 1) {
                gui.appendMessage("Starting the game");
                //start the game
                
                //remove first two players from list
                List<Player> retrievePlayers = this.retrievePlayers();
                
                serverStatus();
            }
            
            playerQueue.stream().forEach((p) -> {
                gui.appendMessage(p.toString());
            });
        }
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (IOException ex) {
            gui.appendMessage(ex.getMessage());
        }
    }
    
    public void serverStatus() {
        gui.appendMessage("Current player status on server: " + playerQueue.size() + " player(s) waiting\n");
    }
    
    public List<Player> retrievePlayers() {
        List<Player> retrieveList = playerQueue.subList(0, 2);       
        playerQueue.subList(0, 2).clear();
        return retrieveList;
    }
    
}
