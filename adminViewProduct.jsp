<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<%@ page import="com.namastamart.service.impl.*,
                 com.namastamart.service.*,
                 com.namastamart.beans.*,
                 java.util.*,
                 javax.servlet.ServletOutputStream,
                 java.io.*" %>

<!DOCTYPE html>
<html>
<head>
    <title>View Products</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="css/changes.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<style>
body{
	margin:0;
	font-family:Arial,sans-serif;
}
.content {
    margin-left: 250px;
    padding: 20px;
    transition: margin-left 0.3s;
}
.sidebar.collapsed ~ .content {
    margin-left: 100px;
}
.product-container {
    background-color: #f1cdf6;
    min-height: 100vh;
    padding-bottom: 20px;
}
.product-name {
    font-weight: bold;
}
.thumbnail {
    height: 350px;
    padding: 15px;
    margin-bottom: 30px;
}
.footer {
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
.sidebar.collapsed ~ .footer {
    left: 60px;
    width: calc(100% - 60px);
}
</style>
</head>

<body>

<%@ include file="adminheader.jsp" %>

<div class="content">
    <div class="text-center" style="color:black;font-size:14px;font-weight:bold;">
        <%
            String userName = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");

            if (userName == null || password == null) {
                response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            }

            ProductServiceImpl prodDao = new ProductServiceImpl();
            List<ProductBean> products = new ArrayList<ProductBean>();

            String search = request.getParameter("search");
            String type = request.getParameter("type");
            String message = "All Products";

            if (search != null) {
                products = prodDao.searchAllProducts(search);
                message = "Showing Results for '" + search + "'";
            } else if (type != null) {
                products = prodDao.getAllProductsByType(type);
                message = "Showing Results for '" + type + "'";
            } else {
                products = prodDao.getAllProducts();
            }

            if (products.isEmpty()) {
                message = "No items found for the search '" + (search != null ? search : type) + "'";
            } else {
                products = prodDao.getAllProducts();
            }
        %>
        <%= message %>
    </div>

    <!-- Start of Product Items List -->
    <div class="container product-container">
        <div class="row text-center">
            <% for (ProductBean product : products) { %>
            <div class="col-sm-4">
                <div class="thumbnail">
                    <img src="./ShowImage?pid=<%= product.getProdId() %>" 
                         alt="Product" style="height:150px; max-width:180px;">

                    <p class="product-name"><%= product.getName() %></p>
                    <p>ID: <%= product.getProdId() %></p>
                    <p class="productinfo"><%= product.getProdInfo() %></p>
                    <p class="price">Rs <%= product.getProdPrice() %></p>

                    <form method="post">
                        <button type="submit"
                                formaction="/RemoveProductSrv?prodid=<%= product.getProdId() %>"
                                class="btn btn-danger">Remove</button>

                        <button type="submit"
                                formaction="updateProduct.jsp?prodid=<%= product.getProdId() %>"
                                class="btn btn-primary">Update Product</button>
                    </form>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>

</body>
</html>
