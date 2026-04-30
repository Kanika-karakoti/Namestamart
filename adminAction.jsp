<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.namastemat.service.impl.UserServiceImpl" %>
<%@ page import="com.namastemat.beans.UserBean" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin User Management</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
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
        }

        .table-container {
            background-color: #f1cdf6;
            padding: 20px;
        }

        .footer {
            position: fixed;
            left: 250px;
            bottom: 0;
            width: calc(100% - 250px);
            background-color: #333;
            color: white;
            text-align: center;
            padding: 10px;
        }

        .btn {
            margin: 5px;
        }

        .user-image {
            width: 50px;
            height: 50px;
            border-radius: 50%;
        }

        .table-bordered th,
        .table-bordered td {
            border: 1px solid black !important;
        }
    </style>
</head>

<body>

<%
    String userType = (String) session.getAttribute("usertype");
    String userName = (String) session.getAttribute("username");
    String password = (String) session.getAttribute("password");

    if (userType == null || !userType.equals("admin")) {
        response.sendRedirect("login.jsp?message=Access Denied, Login as admin!!");
        return;
    }

    if (userName == null || password == null) {
        response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
        return;
    }

    UserServiceImpl userService = new UserServiceImpl();

    String action = request.getParameter("action");
    String userId = request.getParameter("userId");

    if ("activate".equals(action)) {
        userService.activateUser(userId);
    } else if ("deactivate".equals(action)) {
        userService.deactivateUser(userId);
    } else if ("delete".equals(action)) {
        userService.deleteUser(userId);
    }

    List<UserBean> userList = userService.getAllUsers();
%>

<%@ include file="adminheader.jsp" %>

<div class="content">
    <div class="table-container">
        <h2 class="text-center" style="font-weight:bold;">Manage Users</h2>

        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead style="background-color:#b341ab;color:white;">
                <tr>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Mobile</th>
                    <th>Email</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody style="background-color:#f1cdf6;">

                <%
                    if (userList != null && userList.size() > 0) {
                        for (UserBean user : userList) {

                            String status = user.isActive() ? "Active" : "Inactive";
                            String actionLabel = user.isActive() ? "Deactivate" : "Activate";
                            String actionValue = user.isActive() ? "deactivate" : "activate";

                            String imageSrc = (user.getUserImage() != null)
                                    ? "ImageServlet?email=" + user.getEmail()
                                    : "images/userprofile.png";
                %>

                <tr>
                    <td>
                        <img src="<%= imageSrc %>" class="user-image" alt="User Image">
                    </td>
                    <td><%= user.getName() %></td>
                    <td><%= user.getMobile() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><%= status %></td>
                    <td>

                        <form method="get" style="display:inline;">
                            <input type="hidden" name="userId" value="<%= user.getEmail() %>">
                            <button type="submit" name="action" value="<%= actionValue %>"
                                    class="btn <%= user.isActive() ? "btn-warning" : "btn-success" %>">
                                <%= actionLabel %>
                            </button>
                        </form>

                        <form method="get" style="display:inline;">
                            <input type="hidden" name="userId" value="<%= user.getEmail() %>">
                            <button type="submit" name="action" value="delete"
                                    class="btn btn-danger"
                                    onclick="return confirm('Are you sure you want to delete this user?');">
                                Remove
                            </button>
                        </form>

                    </td>
                </tr>

                <%
                        }
                    } else {
                %>

                <tr style="background-color:red;color:white;">
                    <td colspan="6" class="text-center">No Users Available</td>
                </tr>

                <% } %>

                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="footer">
    <p>NamasteMart © 2024. All Rights Reserved</p>
</div>

</body>
</html>
