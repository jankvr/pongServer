/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.main;
import org.apache.log4j.Logger;
import pongserver.players.PlayerThread;


/**
 * CmdParser vykonáva činnosti, ktoré určujú príkazy an inpute
 * @author Jaroslav Fedorčák, Jan Kovář
 */
public class CmdParser {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    
    /**
     * Defaultné parsovanie príkazu
     * @param player hráč, ktorého sa text príkazu týka (nie ten, ktorý ho poslal)
     * @param input text príkazu
     */
    public void parse(PlayerThread player, String input){
        //rozdelenie stringu podľa medzier do poľa
        String backup = input;
        String delims = " ";
        String[] tokens = input.split(delims);  
        

        if(tokens[0].equals("OPPONENTPOSITION")){
            player.setyPosition(Integer.parseInt(tokens[1]));
        }        
        else{
            LOG.warn("nesprávny reťazec poslaný na CmdParser:"+backup);
        } 
    }
    
    /**
     * spracovanie pozície, volané z miesta, kde sa očakáva iba poslanie pozície
     * @param axis
     * @param input text príkauzn na inpute
     * @return 
     */
    public double parsePosition(String axis, String input) {
        String delims = " ";
        String[] tokens = input.split(delims);  
        
        if (tokens[0].equals("OPPONENTPOSITION")) {
            switch (axis) {
                case "x":
                    return Double.parseDouble(tokens[1]);
                case "y":
                    return Double.parseDouble(tokens[2]);
                default:
                    return 0;
            }
        }        
        else {
            return 0;
        } 
    }
    

    /**
     * Zjisteni samotneho prikazu (nehlede na parametry).
     * Pouziva se v PlayerThread
     * @param input prijimana zprava
     * @return prikaz
     */
    public String parseCommand(String input) {
        String delims = " ";
        String[] tokens = input.split(delims);
        
        if (tokens.length > 0) {
            return tokens[0];
        }
        
        return null;
    }
    
}
