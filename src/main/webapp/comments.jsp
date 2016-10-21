<%-- 
    Document   : comments
    Created on : 19-Oct-2016, 20:49:46
    Author     : gary-
--%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
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
                    <li><a href="${pageContext.request.contextPath}/upload.jsp">Upload</a></li>
                    <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                    <li><a href="/Instagrim/Profile/<%=lg.getUsername()%>">Your Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout.jsp">Sign Out</a></li>
                    <%}
                            }else{
                                        %>
                    <li><a href="login.jsp">Login</a></li>
                    <li><a href="register.jsp">Register</a></li>
                    <% 

                    
                                        }%>
                </ul>
            </div>
        </div>
            <div class="main-content">
                <button onclick="myFunction();" class="side-m-button" data-toggle=".main-content" id="sidebar-toggle" > < </button>
                <div class="content">
                    <h3> Picture Title - Comments </h3>
                    <div class ="profile">
                        <a href="#"> <img src="/Instagrim/Image/54087bd0-9624-11e6-a5b4-461ca8e43e4f"></a>
                        <h2> User Comments: </h2>   
                    </div>
                </div>
                
            </div>
                
        <script>
                        $(document).ready(function(){
                            $("button").click(function(){
                                
                                var elem = document.getElementById("sidebar-toggle");
                                if(elem.textContent == ">"){
                                    
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
