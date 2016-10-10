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
            <a href="${pageContext.request.contextPath}/index.jsp"><img src="${pageContext.request.contextPath}/icon.jpg" style="width:200px; height:200px; vertical-align:bottom;"></a>
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
                    <li><a href="${pageContext.request.contextPath}/upload.jsp">Upload</a></li>
                    <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile.jsp">Your Profile</a></li>
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
                    <%
                        String user = lg.getUsername();
                        
                        
                        %>
                    <h3><%= user.toUpperCase() %>'s Profile </h3>
                    <div class="profile"> 
                    <%--
                        icon, bio, link to images, firends??????????
                    --%>
                    
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

                        var showlogin = document.getElementById("Login");
                        showlogin.onclick = function(){
                            
                            return false;
                        }
                        
        </script>
    </body>
</html>