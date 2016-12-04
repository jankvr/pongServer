/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.main;
import pongserver.game.Game;
import pongserver.players.PlayerThread;


/**
 *
 * @author User
 */
public class CmdParser {
    private Game game;
    
    
    public CmdParser(Game game){
        this.game = game;
    }
    
    public void parse(PlayerThread player, String input){
        //rozdelenie stringu podľa medzier do poľa
        String delims = " ";
        String[] tokens = input.split(delims);  
        

        if(tokens[0].equals("PADDLEPOSITION")){
            player.setyPosition(Integer.parseInt(tokens[1]));
        }        
        else{
            System.out.println("nesprávny reťazec poslaný na CmdParser");
        }
        
        
    }
    
}
