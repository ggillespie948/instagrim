package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Profile",
    "/Profile/*"
    
})
@MultipartConfig

public class Profile extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {

    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        
        //Split up each element of url and place into array e.g. Instagrim[1]/Profile[1]/User[2]
        String args[] = Convertors.SplitRequestPath(request);
        
        //Display Username of Profile in header from URL
        HttpSession session=request.getSession();
        String ProfileName = args[2];
        session.setAttribute("ProfileName",ProfileName);
        
        //Take the profile name segment for the URL and pass to method which produces users picture stream
        DisplayUserPicStream(args[2], request, response);
        
        
        
    }
    
    
    protected void DisplayUserPicStream(String profile, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        //Model to access db
        PicModel tm = new PicModel();
        tm.setCluster(cluster); //tells model how to connect to db
        
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(profile); //get all the pictures for profile page
        RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp"); //call up view profile jsp
        request.setAttribute("UserPicStream", lsPics); //send list to profile.jsp
        rd.forward(request, response); //transfer control to profile.jsp.jsp
        
        //call method from PicModel.java (get picture from user)
        
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }
    
    //Error Message Method
    private void errorMessage(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a an error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
    
}

