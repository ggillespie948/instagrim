<%-- 
    Document   : comments
    Created on : 19-Oct-2016, 20:49:46
    Author     : gary-
--%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.gjg.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/css/Styles.css" rel="stylesheet" type="text/css" >
        <script src="${pageContext.request.contextPath}/jquery-3.1.1.js"></script>
    </head>
    <body>
        <div class="sidebar-container">
            <a href="/Instagrim/Login"><img src="${pageContext.request.contextPath}/icon.jpg" style="width:200px; height:200px; vertical-align:bottom;"></a>
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
                <h3> ${PicTitle} - Comments </h3>
                <div class ="profile">
                    <a href="#"> <img src="/Instagrim/Image/${picid}"></a>
                    <h2> User Comments: </h2>  
                    <%
                        // List of picture comments recieved here
                        java.util.ArrayList<String> lsComments = (java.util.ArrayList<String>) request.getAttribute("PicComments");
                        
                        String username = lg.getUsername();
                        
                        if (lsComments == null) {

                    %>
                    <p> No comments </p>
                    <%  } else {
                      //go through each comment in list
                        Iterator<String> iterator;
                        iterator = lsComments.iterator();

                        while (iterator.hasNext()) {
                            String comment = (String) iterator.next();
                            //Take profile name at start of comment for link to profile
                            String commentProfile = comment.substring(0, comment.indexOf(':'));

                    %>
                    <div class="comment">
                        <p> <%= comment%> <a class="commentButton" href="${pageContext.request.contextPath}/Profile/<%= commentProfile %>" > View Profile  </a> </p> 
                    </div>
                    <%
                            }
                        }
                    %>
                    <br>
                    <h2> Post a comment: </h2>
                    <form action="Comments" method="POST">
                        <textarea name="textAreaComment" rows="5" cols="60"> </textarea>
                        <br>
                        <input type="hidden" name="picidString" value="${picid}">
                        <input type="hidden" name="username" value="<%= username %>">
                        <input type="submit" class="button2">
                    </form>    
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
