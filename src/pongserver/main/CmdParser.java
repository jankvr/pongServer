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
//    private final Game game;
//    
//    
//    public CmdParser(Game game){
//        this.game = game;
//    }
    
    public void parse(PlayerThread player, String input){
        //rozdelenie stringu podľa medzier do poľa
        String backup = input;
        String delims = " ";
        String[] tokens = input.split(delims);  
        

        if(tokens[0].equals("OPPONENTPOSITION")){
            player.setyPosition(Integer.parseInt(tokens[1]));
        }        
        else{
            System.out.println("nesprávny reťazec poslaný na CmdParser:"+backup);
        } 
    }
    
    public double parsePosition(String axis, String input) {
        String delims = " ";
        String[] tokens = input.split(delims);  
        
        if (tokens[0].equals("OPPONENTPOSITION")) {
            if (axis.equals("x")) {
                return Double.parseDouble(tokens[1]);
            }
            else if (axis.equals("y")) {
                return Double.parseDouble(tokens[2]);
            }
            else {
                return 0;
            }
        }        
        else {
            return 0;
        } 
    }
    
    public String parseCommand(String input) {
        String delims = " ";
        String[] tokens = input.split(delims);
        
        if (tokens.length > 0) {
            return tokens[0];
        }
        
        return null;
    }
    
}
