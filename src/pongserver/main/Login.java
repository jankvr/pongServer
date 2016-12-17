/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Trieda Login spravuje prihlasovanie na strane serveru.
 * @author Jaroslav Fedorčák, Jan Kovář
 */
public class Login {
    private final Map userMap; 
    private final List connectedUsers;
    private static final Logger LOG = Logger.getLogger(Login.class.getName());
    
    
    public Login() {
        userMap = new HashMap<>();//"registrovaní" používatelia
        userMap.put("a", "a");
        userMap.put("b", "b");
        userMap.put("c", "c");
        userMap.put("d", "d");
        userMap.put("e", "e");
        userMap.put("andrej", "hunter2");
        
        connectedUsers = new ArrayList<>();
        
    }
    
    /**
     * Zistí, či je možné prihlásiť daného používateľa
     * @param username používateľské meno prihlasovaného používateľa
     * @param password heslo prihlasovaného používateľa
     * @return "OK" ak bol hráč philásený, "ALREADYCONNECTED" ak už taký hráč prihlásený je, "WRONG" ak je zadané nesprávne meno alebo heslo
     */
    public String check(String username, String password){
       
        if(userMap.containsKey(username) && userMap.get(username).equals(password)
                &&!connectedUsers.contains(username)){
            LOG.info("Received a fine username - username: " + username);
            connectedUsers.add(username);
            return "OK";
        }
        else if(userMap.containsKey(username) && userMap.get(username).equals(password)
                &&connectedUsers.contains(username)){
            System.out.println("Already connected - username: " + username);
            return "ALREADYCONNECTED";
        }
        LOG.info("Bad username: " + username);
        return "WRONG";
    }
    
    /**
     * Odobratie používateľa, ktorý sa odpojil
     * @param username používateľské meno odpojeného používateľa
     */
    public void removeDisconnectedUser(String username){
        if(connectedUsers.contains(username)){
            connectedUsers.remove(username);
        }
    }
    
    
    
}
