/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    Cluster cluster=null;


    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        
        
        //User Model
        User us=new User();
        us.setCluster(cluster);
        boolean isValid=us.IsValidUser(username, password); //method that checks if user is valid
        HttpSession session=request.getSession();
        System.out.println("Session in servlet "+session);
        
        if (isValid){
            LoggedIn lg= new LoggedIn();
            lg.setLogedin();
            lg.setUsername(username);
            
            
            //Retrieve First Name of Account
            Session sessionCQL = cluster.connect("instagrim");
            PreparedStatement ps;
            ps = sessionCQL.prepare("select first_name from userprofiles where login =?");
            
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = sessionCQL.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
            for (Row row : rs) {
                String First_Name = row.getString("first_name");
                lg.setFname(First_Name);
            }
        
            
            //lg.setLname(lname);
            //request.setAttribute("LoggedIn", lg);
            
            session.setAttribute("LoggedIn", lg); //contains information on logged in status
            System.out.println("Session in servlet "+session);
            RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    rd.forward(request,response);
            
            
        }else{
            response.sendRedirect("/Instagrim/Login");
        }
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        
        RequestDispatcher rd = request.getRequestDispatcher("/login.jsp"); //call up view login jsp
        rd.forward(request, response); //transfer control to login.jsp
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
