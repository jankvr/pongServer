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

/**
 *
 * @author Jaroslav
 */
public class Login {
    private Map userMap; 
    private List connectedUsers;

    public Login() {
        userMap = new HashMap<String, String>();
        userMap.put("a", "a");
        userMap.put("b", "b");
        userMap.put("c", "c");
        userMap.put("d", "d");
        userMap.put("e", "e");
        userMap.put("andrej", "hunter2");
        
        connectedUsers = new ArrayList<String>();
        
    }
    
    public String check(String username, String password){
       
        if(userMap.containsKey(username) && userMap.get(username).equals(password)
                &&!connectedUsers.contains(username)){
            System.out.println("I received a fine username");
            connectedUsers.add(username);
            return "OK";
        }
        else if(userMap.containsKey(username) && userMap.get(username).equals(password)
                &&connectedUsers.contains(username)){
            System.out.println("Fine username, but already connected m8");
            return "ALREADYCONNECTED";
        }
        System.out.println("This username is bollocks");
        return "WRONG";
    }
    
    
    public void removeDisconnectedUser(String username){
        if(connectedUsers.contains(username)){
            connectedUsers.remove(username);
        }
    }
    
    
    
}
