<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ page import="com.namastemart.service.impl.*, com.namastemart.service.*" %>
<%@ page import="com.namastemart.beans.*"%>
<!DOCTYPE html>
<html>
<head>
    <title>Profile Details</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <style>
        /* File input styles */
        file-input::file-selector-button {
            margin-right: 15px;
        }
    </style>
</head>
<body style="margin-top: 60px;">

    <%
    // Checking the user credentials
    String userName = (String) session.getAttribute("username");
    String password = (String) session.getAttribute("password");

    if (userName == null || password == null) {
        response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    UserService dao = new UserServiceImpl();
    UserBean user = dao.getUserDetails(userName, password);
    if (user == null) {
        user = new UserBean(); // Use default constructor
        // Set default values
        user.setName("Test User");
        user.setMobile(987654987651L);
        user.setEmail("test@gmail.com");
        user.setAddress("Sector-73, Noida, Uttar Pradesh");
        user.setPinCode(201302);
        user.setPassword("password");
        user.setUserImage(null);
        user.setActive(true);
    }
    %>

    <jsp:include page="profileHeader.jsp" />

    <div class="container">
        <div class="row">
            <div class="col">
                <nav aria-label="breadcrumb" class="bg-light rounded-3 p-3 mb-4">
                    <ol class="breadcrumb mb-0" style="background-color:#f1cdf6;">
                        <li class="breadcrumb-item"><a href="index.jsp">Home</a></li>
                        <li class="breadcrumb-item"><a href="userProfile.jsp">User Profile</a></li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>

    <form action="UpdateProfileServlet" method="post" enctype="multipart/form-data">
        <div class="row">
            <div class="col-lg-4">
                <div class="card mb-4">
                    <div class="card-body text-center">
                        <%
                        byte[] userImage = user.getUserImage();
                        String imageSrc = "images/userprofile.png"; // Default image

                        if (userImage != null && userImage.length > 0) {
                            imageSrc = "data:image/png;base64," + user.getEmail(); // URL to fetch image from servlet
                        }
                        %>
                        <img src="<%=imageSrc%>" alt="Profile Image" class="rounded-circle img-fluid" style="width: 150px;">
                        <input type="file" name="profileImage" class="form-control mt-3 file-input" style="margin-top: 10px; margin-left: 125px; width: 117px;">
                        <h5 class="my-3">Hello, <%=user.getName()%></h5>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="card mb-4 mb-lg-0">
                    <div class="card-body p-0">
                        <ul class="list-group list-group-flush rounded-3">
                            <li style="background-color: #f1cdf6;" class="text-center list-group-item d-flex justify-content-between align-items-center p-3">
                                <h1>My Profile</h1>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Name</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="text" name="name" class="form-control text-success" value="<%=user.getName()%>">
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Email</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="email" name="email" class="form-control text-success" value="<%=user.getEmail()%>">
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Phone</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="text" name="mobile" class="form-control text-success" value="<%=user.getMobile()%>">
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Full Name</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="text" name="name" class="form-control text-success" value="<%=user.getName()%>">
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Email</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="email" name="email" class="form-control text-success" value="<%=user.getEmail()%>">
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Phone</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="text" name="mobile" class="form-control text-success" value="<%=user.getMobile()%>">
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Address</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="text" name="address" class="form-control text-success" value="<%=user.getAddress()%>">
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <p class="mb-0 text-info">Pin Code</p>
                            </div>
                            <div class="col-sm-9">
                                <input type="text" name="pincode" class="form-control text-success" value="<%=user.getPinCode()%>">
                            </div>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-success" style="margin-top: 40px;margin-left: 420px;padding: 10px;font-size: medium;">Save Changes</button>
            </div>
        </div>
    </form>

    <br>
    <br>
    <br>

    <%@ include file="footer.html"%>

</body>
</html>