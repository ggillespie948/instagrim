<%-- 
    Document   : profile
    Created on : 05-Oct-2016, 17:59:34
    Author     : gary-
--%>

<%-- 
    Document   : logout
    Created on : 03-Oct-2016, 14:31:20
    Author     : gary-
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
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
            <a href="../../Instagrim"><img src="${pageContext.request.contextPath}/icon.jpg" style="width:200px; height:200px; vertical-align:bottom;"></a>
            <div id="banner">
                <h1> Instagrim  </h1>
            </div>
            <div id="sidebar">
                <ul>
                    <%
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {

                            if (lg.getlogedin()) {
                    %>
                    <li><a href="/Instagrim/Upload">Upload</a></li>
                    <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                    <li><a href="/Instagrim/Profile/<%=lg.getUsername()%>">Your Profile</a></li>
                    <li><a href="/Instagrim/Logout">Sign Out</a></li>
                        <%}
                        } else {
                        %>
                    <li><a href="/Instagrim/Login" >Login</a></li>
                    <li><a href="/Instagrim/Register">Register</a></li>
                        <%
                        }%>
                </ul>
            </div>
        </div>
        <div class="main-content">
            <button onclick="myFunction();" class="side-m-button" data-toggle=".main-content" id="sidebar-toggle" > < </button>
            <div class="content">
                <%
                %>
                <h3> ${FirstName}'s Profile - ${ProfileName}  </h3>
                <div class="profile"> 
                    <%--
                    
                    --%>
                    <h2> Profile Picture: </h2>
                    <%
                        Pic ProfilePicture = (Pic) request.getAttribute("ProfilePict");
                    %>
                    <%-- <a href="#"> <img src="/Instagrim/Thumb/<%= ProfilePictureID %>"></a> --%>
                    <a href="#"> <img src="/Instagrim/Thumb/${ProfilePictureID}"></a>
                    <br>
                    <h2> Bio: </h2>
                    <div class="bio">
                        My profile consists of a collection of geometric shapes and patterns. All of which make up part of my portfolio.
                        <br>
                        This is template Bio text, to be added later.
                        <br>
                        Feel free to se any of these pictures.
                    </div>

                    <h2> ${FirstName}'s Uploads: </h2>
                    <%
                        //Recieve UserPicStream from Servlet here 
                        java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("UserPicStream");

                        if (lsPics == null) {

                    %>
                    <p> User is yet to upload </p>
                    <%} else {

                        //go through each pic in list
                        Iterator<Pic> iterator;
                        iterator = lsPics.iterator();
                        while (iterator.hasNext()) {
                            Pic p = (Pic) iterator.next();

                    %>
                    <a href="/Instagrim/Comments/<%=p.getSUUID()%>"><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a>

                    <%
                                }
                            }%>
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
