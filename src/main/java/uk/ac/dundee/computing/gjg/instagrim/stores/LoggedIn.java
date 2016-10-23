/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.gjg.instagrim.stores;

/**
 *
 * @author Administrator
 */
public class LoggedIn {
    boolean loggedin=false;
    String Username=null;
    String Fname=null;
    String Lname=null;
    
    public void LoggedIn(){
        
    }
    
    public void setUsername(String name){
        this.Username=name;
    }
    public String getUsername(){
        return Username;
    }
    public void setFname(String fname){
        this.Fname=fname;
    }
    public String getFname(){
        return Fname;
    }
    public void setLogedin(){
        loggedin=true;
    }
    public void setLogedout(){
        loggedin=false;
    }
    
    public void setLoginState(boolean loggedin){
        this.loggedin=loggedin;
    }
    public boolean getlogedin(){
        return loggedin;
    }
}
