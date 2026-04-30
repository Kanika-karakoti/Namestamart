<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Remove Product</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
        }
        
        .content {
            margin-left: 250px;
            padding: 20px;
            transition: margin-left 0.3s;
        }
        
        #sidebar.collapsed ~ .content {
            margin-left: 100px;
        }
        
        form-container {
            background-color: #b341ab;
            border: 2px solid black;
            border-radius: 10px;
            padding: 10px;
            margin-top: 20px;
        }
        
        footer {
            position: fixed;
            left: 250px;
            bottom: 0;
            width: calc(100% - 250px);
            background-color: #333;
            color: white;
            text-align: center;
            padding: 10px;
            transition: left 0.3s, width 0.3s;
        }
        
        #sidebar.collapsed ~ .footer {
            left: 60px;
            width: calc(100% - 60px);
        }
    </style>
</head>
<body>

<%
// Checking user credentials
String userType = (String) session.getAttribute("usertype");
String userName = (String) session.getAttribute("username");
String password = (String) session.getAttribute("password");

if (userType == null || !userType.equals("admin")) {
    response.sendRedirect("login.jsp?message=Access Denied, Login as admin!!");
    return;
} else if (userName == null || password == null) {
    response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    return;
}
%>

<%@ include file="adminHeader.jsp"%>

<%
String message = request.getParameter("message");
%>

<div class="container">
    <div class="form-container col-md-4 col-md-offset-4">
        <form action="./RemoveProductSrv" method="post">
            <div style="font-weight: bold;" class="text-center">
                <h3 style="color: black;">Product Deletion Form</h3>
                <% if (message != null) { %>
                <p style="color: rgb(25, 252, 25);"><%=message%></p>
                <% } %>
            </div>
            <div class="row">
                <div class="col-md-12 form-group">
                    <label for="prodid">Product Id</label>
                    <input type="text" placeholder="Enter Product Id" name="prodid" class="form-control" id="prodid" required>
                </div>
            </div>
            <div class="row text-center">
                <div class="col-md-6" style="margin-bottom: 2px;">
                    <a href="adminViewProduct.jsp" class="btn btn-warning">Cancel</a>
                </div>
                <div class="col-md-6">
                    <button type="submit" class="btn btn-danger">Remove Product</button>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="footer">
    <p>NamasteMart &copy; 2024. All Rights Reserved.</p>
</div>

</body>
</html>