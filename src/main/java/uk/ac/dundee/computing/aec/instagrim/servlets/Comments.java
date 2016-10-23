/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author gary-
 */
/**
 *
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Comments",
    "/Comments/*",
})
@MultipartConfig
public class Comments extends HttpServlet {
    
    Cluster cluster=null;

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        
        String args[] = Convertors.SplitRequestPath(request);
        
        //Display Username of Profile in header from URL
        HttpSession session=request.getSession();
        
        java.util.UUID picid = java.util.UUID.fromString(args[2]);
        session.setAttribute("picid", picid);
        
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        
        
        //Get Picture Title
        String title = tm.getPicTitle(picid);
        
        //Remove first four characters (row {
        String substring = title.substring(4);
        
        //Remove last character
	substring= substring.substring(0, substring.length() - 1);
        title = substring;
        session.setAttribute("PicTitle", title);
        
        //Get picid from URL        
        
        
        DisplayPicComments(picid, request, response);
        
        RequestDispatcher rd = request.getRequestDispatcher("/comments.jsp"); //call up view login jsp
        rd.forward(request, response); //transfer control to login.jsp
    }
    
    protected void DisplayPicComments(java.util.UUID picid, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        //Model to access db
        PicModel tm = new PicModel();
        tm.setCluster(cluster); //tells model how to connect to db
        
        java.util.ArrayList<String> alComments = new java.util.ArrayList<String>();
        java.util.List<String> lsComments = tm.getComments(picid); //get all the pictures for profile page
        
        alComments.addAll(lsComments);
        
        
        RequestDispatcher rd = request.getRequestDispatcher("/comments.jsp"); //call up view profile jsp
        request.setAttribute("PicComments", alComments); //send list to profile.jsp
        rd.forward(request, response); //transfer control to profile.jsp.jsp
        
        //call method from PicModel.java (get picture from user)
        
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //Get username
        String username = request.getParameter("username");
        
        //Get comment text
        String comment = request.getParameter("textAreaComment");
        
        //Add username to begining of comment:
        comment = username + ": " + comment;
        
        //Get picture id from URL
        String args[] = Convertors.SplitRequestPath(request);
        String picidString = request.getParameter("picidString");
        java.util.UUID picid = java.util.UUID.fromString(picidString);
        
        //New Picture Model, set commnt
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        tm.setComment(comment, picid);
        
        //Redirect back to comments
        response.sendRedirect("/Instagrim/Comments/"+picid);
        
        
    }
    
}
