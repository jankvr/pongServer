/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.main;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jaroslav
 */
public class Login {
    private Map userMap; 

    public Login() {
        userMap = new HashMap<String, String>();
        userMap.put("a", "a");
        userMap.put("b", "b");
        userMap.put("c", "c");
        userMap.put("d", "d");
        userMap.put("e", "e");
        userMap.put("andrej", "hunter2");
        
    }
    
    public boolean check(String username, String password){
       
        if(userMap.containsKey(username) && userMap.get(username).equals(password)){
            System.out.println("I received a fine username");
            return true;
        }
        System.out.println("This username is bollocks");
        return false;
    }
    
    
    
}
