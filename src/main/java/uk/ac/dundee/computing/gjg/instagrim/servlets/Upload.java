/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.gjg.instagrim.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import uk.ac.dundee.computing.gjg.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.gjg.instagrim.models.PicModel;
import uk.ac.dundee.computing.gjg.instagrim.stores.LoggedIn;
import com.datastax.driver.core.Cluster;

/**
 *
 * @author gary-
 */
@WebServlet(name = "Upload", urlPatterns = {"/Upload"})
public class Upload extends HttpServlet {

    Cluster cluster=null;
        
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        
        RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp"); //call up view login jsp
        rd.forward(request, response); //transfer control to login.jsp
    }
    
    

    

    
}
