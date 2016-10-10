<%-- 
    Document   : logout
    Created on : 03-Oct-2016, 14:31:20
    Author     : gary-
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <a href="index.jsp"><img src="icon.jpg" style="width:200px; height:200px; vertical-align:bottom;"></a>
            <div id="banner">
                <h1> Instagrim  </h1>
            </div>
            <div id="sidebar">
                <ul>
                    <li><a href="login.jsp">Login</a></li>
                    <li><a href="register.jsp">Register</a></li>
                </ul>
            </div>
        </div>
            <div class="main-content">
                <button onclick="myFunction();" class="side-m-button" data-toggle=".main-content" id="sidebar-toggle" > < </button>
                <div class="content">
                    <h3> Login  </h3>
                    <form method="POST"  action="Login">
                    <ul>
                        <li>Username <input type="text" name="username" required></li>
                        <li>Password <input type="password" name="password" required></li>
                        <br/>
                        <input type="submit" value="Login"> 
                    </ul>
                    <br/>
                    
                    </form>

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
