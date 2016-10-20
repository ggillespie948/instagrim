<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
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
            <a href="/Instagrim"><img src="icon.jpg" style="width:200px; height:200px; vertical-align:bottom;"></a>
            <div id="banner">
                <h1> Instagrim  </h1>
            </div>
            <div id="sidebar">
                <ul>
                    <li><a href="/Instagrim/Login">Login</a></li>
                    <li><a href="/Instagrim/Register">Register</a></li>
                </ul>
            </div>
        </div>
            <div class="main-content">
                <button onclick="myFunction();" class="side-m-button" data-toggle=".main-content" id="sidebar-toggle" > < </button>
                <div class="content">
                    <div class="content-banner"> 
                        <h3> Create Your Account </h3>
                    </div>
                    <div class="formHolder">
                    <form id="registerUser" action="Register" onsubmit="return formValidation()" method="POST">
                        <ul>
                            <label for ="username"> Username </label>
                            <li><input type="text" name="username" required> <span id="usernameErrorMsg"></span> </li>
                            <br>
                            <label for="password"> Password </label>
                            <li><input type="password" name="password" required> <span id="passwordErrorMsg"></span> </li>
                            <br>
                            <label for="confirmpassword"> Confirm Password  </label>
                            <li><input type="password" name="confirmpassword" required> <span id="confirmpasswordErrorMsg"></span></li>
                            <br/>
                            <label for="email-address"> Email </label>
                            <li><input type="email" name="email-address" required></li>
                            <br/>
                            <label for="first-name"> First Name  </label>
                            <li><input type="text" name="first-name" required></li>
                            <br/>
                            <label for="last-name"> Last Name  </label>
                            <li><input type="text" name="last-name" required></li>
                            <br/>
                        <input type="submit" value="Register" class="button"> 
                        </ul>
                        
                    </form>    
                    </div>
                    
                </div>
                
            </div>
                
        <script type="text/javascript">
            //Script for sliding menu
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
                                            
            //Client-side form validation for username and confirm password
            function formValidation(){
                var uservar = document.getElementsByName("username");
                var passvar = document.getElementsByName("password");
                var confirmpassvar = document.getElementsByName("confirmpassword");
                
                
                var isValid = true;
                
                if (uservar[0].value.match(/[^a-z0-9]/i)){
                    document.getElementById("usernameErrorMsg").innerHTML = "User name must consist of letters and digits [a-z] [0-9].";
                    isValid = false;
                } else {
                    document.getElementById("usernameErrorMsg").innerHTML = "";
                }
                
                if (!uservar[0].value.match(/[a-z]/i)){
                    document.getElementById("usernameErrorMsg").innerHTML = "Username must consist of at least one letter.";
                    isValid = false;
                } else {
                    document.getElementById("usernameErrorMsg").innerHTML = "";
                }
                
                if (passvar[0].value.length < 5){
                    document.getElementById("passwordErrorMsg").innerHTML = "Password must be atleast 5 characters in length.";
                    isValid = false;
                } else {
                    document.getElementById("passwordErrorMsg").innerHTML = "";
                    
                }
                
                var passcheck = passvar[0].value.localeCompare(confirmpassvar[0].value);
                
                if(passcheck !== 0){
                    document.getElementById("confirmpasswordErrorMsg").innerHTML = "Passwords do not match.";
                    isValid = false;
                } else {
                    document.getElementById("confirmpasswordErrorMsg").innerHTML = ""
                }
                    
                
                if (isValid === true){
                    return true;
                } else {
                    return false;
                }
                
                
            }
                        
        </script>
    </body>
</html>

