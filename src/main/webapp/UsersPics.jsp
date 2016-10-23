<%-- 
    Document   : logout
    Created on : 03-Oct-2016, 14:31:20
    Author     : gary-
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.gjg.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/css/Styles.css" rel="stylesheet" type="text/css" > 
        <script src="${pageContext.request.contextPath}/jquery-3.1.1.js"></script>
    </head>
    <body>
        <div class="sidebar-container">
            <a href="../../Instagrim/"><img src="${pageContext.request.contextPath}/icon.jpg" style="width:200px; height:200px; vertical-align:bottom;"></a>
            <div id="banner">
                <h1> Instagrim  </h1>
            </div>
            <div id="sidebar">
                <ul>
                    <%
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            String UserName = lg.getUsername();
                            if (lg.getlogedin()) {
                    %>
                    <li><a href="/Instagrim/Upload">Upload</a></li>
                    <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                    <li><a href="/Instagrim/Profile/<%=lg.getUsername()%>">Your Profile</a></li>
                    <li><a href="/Instagrim/Logout">Sign Out</a></li>
                        <%}
                        } else {
                        %>
                    <li><a href="/Instagrim/Login">Login</a></li>
                    <li><a href="/Instagrim/Register">Register</a></li>
                        <%
                            }%>
                </ul>
            </div>
        </div>
        <div class="main-content">
            <button onclick="myFunction();" class="side-m-button" data-toggle=".main-content" id="sidebar-toggle" > < </button>
            <div class="content">
                <h3>Your Images </h3>
                <div class ="profile">    

                    <%
                        // List of User pics recived by page here 
                        java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");

                        if (lsPics == null) {


                    %>
                    <p>No Pictures Found or Access Forbidden</p></br>

                    <p> User Required: </p> ${passUser}
                    <p> User Found: </p> ${currentUser}
                    <%                    } else {
                        //go through each picture in the list and generate two buttons for profile picture and comments
                        Iterator<Pic> iterator;
                        iterator = lsPics.iterator();
                        while (iterator.hasNext()) {
                            Pic p = (Pic) iterator.next();

                    %>
                    <a href="/Instagrim/Comments/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a> 
                    <form action="Image" method="POST">
                        <button value="" class="button2"> Make Profile Picture </button>
                        <a class="button2" href="/Instagrim/Comments/<%=p.getSUUID()%>" > View Comments  </a>
                        <input type="hidden" name="PictureID" value="<%=p.getSUUID()%>" required>
                        <input type="hidden" name="PostType" value="ProfilePicture">
                    </form>

                    </br>          
                    <%
                            }
                        }
                    %>
                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                $("button").click(function () {

                    var elem = document.getElementById("sidebar-toggle");
                    if (elem.textContent == ">") {

                        $(".main-content").animate({
                            'marginLeft': '+=200px'}, 500);

                        elem.textContent = "<";

                    } else {

                        $(".main-content").animate({
                            'marginLeft': '-=200px'}, 500);

                        elem.textContent = ">";
                    }

                });

            });
        </script>
    </body>
</html>
