<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/Styles.css" />
        <script src="jquery-3.1.1.js"></script>
    </head>
    <body>
        <div class="sidebar-container">
            <a href=""><img src="icon.jpg" style="width:200px; height:200px; vertical-align:bottom;"></a>
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
                <h1>Instagrim</h1>
                <%
                    if (lg != null) {
                        if (lg.getlogedin()) {
                %>
                <h2> Your Image Feed </h2>
                <p class="MainText"> This project serves as course work in module AC32007. 
                    <%
                        java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("AllPicStream");

                        if (lsPics != null) {
                            //go through each pic in list
                            Iterator<Pic> iterator;
                            iterator = lsPics.iterator();

                            while (iterator.hasNext()) {
                                Pic p = (Pic) iterator.next();

                    %>
                    <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a>  
                        <%
                                        }
                                    }
                                }
                            }
                        %>
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

            var showlogin = document.getElementById("Login");
            showlogin.onclick = function () {

                return false;
            }

        </script>





    </body>
</html>
