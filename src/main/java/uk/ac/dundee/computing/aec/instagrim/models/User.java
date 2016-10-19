/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    public User(){
        
    }
    
    //Reigster user 
    public boolean RegisterUser(String Username, String Password, String email, String Fname, String Lname){
        
         boolean isUnique = existingUserCheck(Username);
        
         if (isUnique == true){
             AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();                      
            String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);                        //encrypt password
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
         
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,first_name,last_name,password) Values(?, ?, ?, ?)");
        BoundStatement boundStatement = new BoundStatement(ps);        
        
        
        
        try{
            session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        Username,Fname,Lname,EncodedPassword));                                     //try execute statement
            
            //We are assuming this always works.  Also a transaction would be good here !
            return true;
            
        }catch (Exception e){
            System.out.println("EXCEPTION EXECUTING QUERY: " + e.getMessage());
            return false;
        }
        
        
             
             
         } else {
             //Else username is not unique
             return false;
         }
        
        
    }
    
    //Method which searches cassandra for a given username, returning true if the username is unique and false if it already exists in the database.
    public boolean existingUserCheck(String username){
        
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select login from userprofiles where login =?");
        
        ResultSet rs = null;
        BoundStatement boundstatement = new BoundStatement(ps);
        
        rs = session.execute(
                boundstatement.bind(
                        username));
        
        if(rs.isExhausted()) {
            //No existing username found, success!
            return true;
        } else {
            System.out.println("Username already in use");
            return false;
        }
    }
    
    //Check if user credentials are valid
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
        
        return false;  
    }
    
    
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}
