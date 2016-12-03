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
import pongserver.players.PlayerThread;

/**
 * Hlavni trida 
 * 
 * @author User
 */
public class Server implements Runnable {
    
    private List<PlayerThread> playerQueue;
    private ServerSocket serverSocket;
    private Map<PlayerThread, DataOutputStream> outputStreams;
    private IGui gui;
    private int index;
    
    
    public Server(int port, IGui gui) {
        
        try {
            this.gui = gui;
            this.outputStreams = new HashMap<>();
            this.playerQueue = new ArrayList<>();
            this.serverSocket = new ServerSocket(port);
            
            this.index = 0;

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
            
            PlayerThread playerThread = new PlayerThread("p" + this.index, socket, this);
            this.index++;
            
            playerQueue.add(playerThread);
            gui.appendMessage("Connected on " + socket);

            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            //save outputstream
            outputStreams.put(playerThread, outStream);
            
            serverStatus();
            
            if (playerQueue.size() > 1) {
                gui.appendMessage("Starting the game");
                //start the game
                
                //remove first two players from list
                List<PlayerThread> retrievePlayers = this.retrievePlayers();
                
                // we can start the game :)
                outputStreams.entrySet()
                        .stream()
                        .filter((entry) -> (retrievePlayers.contains(entry.getKey())))
                        .forEach((entry) -> {
                    try {

                        gui.appendMessage(" - Sending start message to " + entry.getKey().getName());
                        
                        entry.getValue().writeUTF("START");
                        //this.msg.add(message);
                    } catch (IOException ex) {
                        gui.appendMessage(ex.getMessage());
                    }
                });
                serverStatus();
            }
            
            playerQueue.stream().forEach((p) -> {
                gui.appendMessage(p.getName());
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
        gui.appendMessage("Current player status on server: " + playerQueue.size() + " player(s) waiting");
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
    
    public void removeConnection(PlayerThread player) throws IOException {
        
        //delete map entry
        outputStreams.remove(player.getSocket());
        playerQueue.remove(player);
        
        //and close socket
        player.getSocket().close();
        
        gui.appendMessage("Socket " + player.getName() + "closed");
    }
    
}
