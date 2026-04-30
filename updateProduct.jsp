<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="com.namastemart.service.impl.*, com.namastemart.service.*" %>
<%@ page import="com.namastemart.beans.*, java.util.*, javax.servlet.ServletOutputStream, java.io.*" %>
<!DOCTYPE html>
<html>

<head>
    <title>Update Product</title>
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

        .sidebar.collapsed ~ .content {
            margin-left: 100px;
        }

        .form-container {
            background-color: #b341ab;
            border: 2px solid black;
            border-radius: 10px;
            padding: 10px;
            margin-top: 20px;
        }

        footer {
            position: relative;
            left: 250px;
            bottom: 0;
            width: calc(100% - 250px);
            background-color: #333;
            color: white;
            text-align: center;
            padding: 10px;
            margin-top: 20px;
            transition: left 0.3s, width 0.3s;
        }

        .sidebar.collapsed ~ footer {
            left: 60px;
            width: calc(100% - 60px);
        }
    </style>
</head>

<body>
    <%
    /* Checking user credentials */
    String utype = (String) session.getAttribute("usertype");
    String uname = (String) session.getAttribute("username");
    String pwd = (String) session.getAttribute("password");
    String prodid = request.getParameter("prodid");
    ProductBean product = new ProductServiceImpl().getProductDetails(prodid);

    if (prodid == null || product == null) {
        response.sendRedirect("updateProductById.jsp?message=Please Enter a valid product Id");
        return;
    } else if (utype == null || !utype.equals("admin")) {
        response.sendRedirect("login.jsp?message=Access Denied, Login as admin!!");
        return;
    } else if (uname == null || pwd == null) {
        response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
        return;
    }
    %>

    <%@ include file="adminHeader.jsp" %>

    <% String message = request.getParameter("message"); %>
    <div class="content">
        <div class="form-container col-md-6 col-md-offset-3">
            <form action="./UpdateProductSrv" method="post">
                <div style="font-weight: bold;" class="text-center">
                    <div class="form-group">
                        <img src="./ShowImage?pid=<%=product.getProdId()%>" alt="Product Image" height="100px" />
                        <h2 style="color: black;">Product Update Form</h2>
                    </div>
                    <% if (message != null) { %>
                    <p style="color: rgb(25, 252, 25);"><%=message %></p>
                    <% } %>
                </div>
                <input type="hidden" name="pid" class="form-control" value="<%=product.getProdId()%>" required>
                <div class="row">
                    <div class="col-md-6 form-group">
                        <label for="product_name">Product Name</label>
                        <input type="text" placeholder="Enter Product Name" name="name" class="form-control" value="<%=product.getProdName()%>" id="product_name" required>
                    </div>
                    <div class="col-md-6 form-group">
                        <% String ptype = product.getProdType(); %>
                        <label for="producttype">Product Type</label>
                        <select name="type" id="producttype" class="form-control" required>
                            <option value="electronics" <%="electronics".equalsIgnoreCase(ptype) ? "selected" : "" %>>Electronics</option>
                            <option value="appliances" <%="appliances".equalsIgnoreCase(ptype) ? "selected" : "" %>>Appliances</option>
                            <option value="fashion" <%="fashion".equalsIgnoreCase(ptype) ? "selected" : "" %>>Fashion and Apparel</option>
                            <option value="home" <%="home".equalsIgnoreCase(ptype) ? "selected" : "" %>>Home</option>
                            <option value="kitchen" <%="kitchen".equalsIgnoreCase(ptype) ? "selected" : "" %>>Kitchen</option>
                            <option value="fitness" <%="fitness".equalsIgnoreCase(ptype) ? "selected" : "" %>>Fitness</option>
                            <option value="outdoor" <%="outdoor".equalsIgnoreCase(ptype) ? "selected" : "" %>>Outdoor</option>
                            <option value="sports" <%="sports".equalsIgnoreCase(ptype) ? "selected" : "" %>>Sports</option>
                            <option value="recreation" <%="recreation".equalsIgnoreCase(ptype) ? "selected" : "" %>>Recreation</option>
                            <option value="toys" <%="toys".equalsIgnoreCase(ptype) ? "selected" : "" %>>Toys</option>
                            <option value="games" <%="games".equalsIgnoreCase(ptype) ? "selected" : "" %>>Games</option>
                            <option value="supplements" <%="supplements".equalsIgnoreCase(ptype) ? "selected" : "" %>>Supplements</option>
                            <option value="personal_care" <%="personal_care".equalsIgnoreCase(ptype) ? "selected" : "" %>>Personal Care</option>
                            <option value="office" <%="office".equalsIgnoreCase(ptype) ? "selected" : "" %>>Office</option>
                            <option value="stationery" <%="stationery".equalsIgnoreCase(ptype) ? "selected" : "" %>>Stationery</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="product_description">Product Description</label>
                    <textarea name="info" class="form-control" id="product_description" required><%=product.getProdInfo()%></textarea>
                </div>
                <div class="row">
                    <div class="col-md-6 form-group">
                        <label for="product_price">Price per Unit</label>
                        <input type="number" value="<%=product.getProdPrice()%>" placeholder="Enter Unit Price" name="price" class="form-control" id="product_price" required>
                    </div>
                    <div class="col-md-6 form-group">
                        <label for="product_quantity">Stock Quantity</label>
                        <input type="number" value="<%=product.getProdQuantity()%>" placeholder="Enter Stock Quantity" class="form-control" id="product_quantity" name="quantity" required>
                    </div>
                </div>
                <div class="row text-center">
                    <div class="col-md-4" style="margin-bottom: 2px;">
                        <button formaction="adminViewProduct.jsp" class="btn btn-warning">Cancel</button>
                    </div>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-success">Update Product</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

</body>

</html>