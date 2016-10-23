package uk.ac.dundee.computing.gjg.instagrim.servlets;

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
import uk.ac.dundee.computing.gjg.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.gjg.instagrim.lib.Convertors;
import uk.ac.dundee.computing.gjg.instagrim.models.PicModel;
import uk.ac.dundee.computing.gjg.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.gjg.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Images",
    "/Images/*",})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);

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

        //Split up each element of url and place into array e.g. abc.com[1]/images[1]/something[2]
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            //look up hash map above image1, images2, thumb3
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        switch (command) {
            case 1:
                DisplayImage(Convertors.DISPLAY_PROCESSED, args[2], response);
                break;
            case 2:
                DisplayImageList(args[2], request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB, args[2], response);
                break;
            default:
                error("Bad Operator", response);
        }
    }

    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //get login session,
        //if login session = String user
        //continue else, redirect to forbidden/unauthorised message
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        String currentUser = ".";
        if (lg.getlogedin()) {
            currentUser = lg.getUsername();
        }

        //Remove these when done
        session.setAttribute("currentUser", currentUser);
        session.setAttribute("passUser", User);

        if (currentUser.equals(User)) {
            PicModel tm = new PicModel();
            tm.setCluster(cluster); //tells model how to connect to db
            java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User); //get all the pictures for User
            RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp"); //call up view UserPics jsp
            request.setAttribute("Pics", lsPics); //send list to Userpics.jsp
            rd.forward(request, response); //transfer control to userpics.jsp
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp"); //call up view UserPics jsp
            rd.forward(request, response); //transfer control to userpics.jsp
        }

    }

    private void DisplayImage(int type, String Image, HttpServletResponse response) throws ServletException, IOException {

        PicModel tm = new PicModel();
        tm.setCluster(cluster);

        Pic p = tm.getPic(type, java.util.UUID.fromString(Image));

        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }

    //Do Post wich handles file upload
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //Identifier used to distinguish between uploading picture and setting profile picture
        String PostType = request.getParameter("PostType");

        if (PostType.equals("ProfilePicture")) {

            String picUUID = request.getParameter("PictureID");

            HttpSession session = request.getSession();
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
            String currentUser = "#";
            if (lg.getlogedin()) {
                currentUser = lg.getUsername();
            }

            //add ' ' for insersion into db
            String user = "'" + currentUser + "'";

            //Pass User and PictureID to Set Profile Picture Method
            PicModel tm = new PicModel();
            tm.setCluster(cluster);
            tm.setProfilePicID(user, picUUID);

            response.sendRedirect("/Instagrim/Profile/" + currentUser);

        } else {
            response.sendRedirect("/Instagrim/");
            for (Part part : request.getParts()) {

                System.out.println("Part Name " + part.getName());

                String type = part.getContentType();
                String filename = part.getSubmittedFileName();

                String title = request.getParameter("Title");

                //Some form of validation on title?
                InputStream is = request.getPart(part.getName()).getInputStream();
                int i = is.available();
                HttpSession session = request.getSession();
                LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                String username = "majed";
                if (lg.getlogedin()) {
                    username = lg.getUsername();
                }
                if (i > 0) {
                    byte[] b = new byte[i + 1];
                    is.read(b);
                    System.out.println("Length : " + b.length);
                    PicModel tm = new PicModel();
                    tm.setCluster(cluster);

                    tm.insertPic(b, type, filename, username, title);

                    is.close();

                }

            }

        }

    }

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a an error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}
