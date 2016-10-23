/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.gjg.instagrim.servlets;

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
import uk.ac.dundee.computing.gjg.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.gjg.instagrim.models.User;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
    
    Cluster cluster=null;
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        
        //Clear login error if any
        HttpSession session = request.getSession();
        String error = "";
        session.setAttribute("ErrorString", error);
        
        
        RequestDispatcher rd = request.getRequestDispatcher("/register.jsp"); //call up view login jsp
        rd.forward(request, response); //transfer control to login.jsp
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
        String confirmpassword=request.getParameter("confirmpassword");
        String email=request.getParameter("email-address");
        String fname=request.getParameter("first-name");
        String lname=request.getParameter("last-name");
        
        HttpSession session = request.getSession();
        
        //add further validation for user name and password match *******
        boolean validDetails = true;
        
        //if unsuccessful redirect
        
        //Register New User
        User us=new User();
        us.setCluster(cluster);
         if (us.RegisterUser(username, password, email, fname, lname) == true){
             //redirect to login page
            response.sendRedirect("/Instagrim/Login");
             
         } else {
           String error = "Registration failed - username already in use";
           session.setAttribute("RegErrorString", error);
           response.sendRedirect("/Instagrim/Register");  
         }
        
        
        
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
