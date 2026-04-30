<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ page import="com.namastemart.service.impl.*,
com.namastemart.service.*,
com.namastemart.beans.*,
java.util.*,
javax.servlet.ServletOutputStream,
java.io.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Product Stocks</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
      href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">

<link rel="stylesheet" href="css/changes.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js">
</script>

<style>
body {
    margin: 0;
    font-family: Arial, sans-serif;
}

.content {
    margin-left: 250px; /* Adjust this margin based on sidebar width */
    padding: 20px;
    transition: margin-left 0.3s; /* Smooth transition for margin change */
}

.sidebar.collapsed ~ .content {
    margin-left: 100px; /* Adjust margin when sidebar is collapsed */
}

.table-container {
    background-color: #f1cdf6;
    padding-bottom: 20px;
}

.footer {
    position: relative;
    left: 250px; /* Adjust this based on sidebar width */
    bottom: 0;
    width: calc(100% - 250px); /* Adjust width based on sidebar width */
    background-color: #333;
    color: white;
    text-align: center;
    padding: 10px;
    margin-top: 20px;
    transition: left 0.3s, width 0.3s; /* Smooth transition for footer adjustments */
}

.sidebar.collapsed ~ .footer {
    left: 60px; /* Adjust position when sidebar is collapsed */
    width: calc(100% - 60px); /* Adjust width when sidebar is collapsed */
}

.table>tbody>tr>td,
.table>tbody>tr>th,
.table>tfoot>tr>td,
.table>tfoot>tr>th,
.table>thead>tr>td,
.table>thead>tr>th {
    border: 1px solid black !important;
}
</style>

</head>
<body>
    <%
        // Checking the user credentials
        String userName = (String) session.getAttribute("username");
        String userPassword = (String) session.getAttribute("password");
        if (userType == null || !userType.equals("admin")) {
            response.sendRedirect("login.jsp?message=Access Denied, Login as admin!!");
        } else if (userName == null || password == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
        }
    %>
    <%
       Include file="adminheader.jsp"
    %>
  

    <div class="content">
        <div class="table-container">
            <div class="text-center" style="color: black; font-size: 24px; font-weight: bold;">
                Stock Products
            </div>
        </div>
    </div>

    <div class="container-fluid">
        <div class="table-responsive">
            <table class="table table-hover table-sm">
                <thead style="background-color: #b341ab; color: white; font-size: 18px;">
                    <tr>
                        <th>Image</th>
                        <th>ProductId</th>
                        <th>Name</th>
                        <th>Type</th>
                        <th>Price</th>
                        <th>Sold Qty</th>
                        <th>Stock Qty</th>
                        <th colspan="2" style="text-align: center;">Actions</th>
                    </tr>
                </thead>
                <tbody style="background-color: #f1cdf6; font-size: 16px;">
                    <%
                        ProductServiceImpl productDao = new ProductServiceImpl();
                        List<ProductBean> products = new ArrayList<ProductBean>();
                        products = productDao.getAllProducts();

                        for (ProductBean product : products) {
                    %>
                    <tr>
                        <td><img src="<%=imagePath%>" style="width: 50px; height: 50px;"></td>
                        <td><a href="<%=updateProduct.jsp%>?productId=<%=product.getId()%>"><%=product.getName()%></a></td>
                        <%
                            String name = product.getProductName();
                            name = name.substring(0, Math.min(name.length(), 25)) + "..";
                        %>
                        <td><%=name%></td>
                        <td><%=product.getPrice()%> toUpperCase()</td>
                        <td><%=product.getPrice()*product.getDiscount()%></td>
                        <td><%=new OrderServiceImpl().countSoldItem(product.getProductId())%></td>
                        <td><%=product.getProductQuantity()%></td>
                        <td>
                            <form method="post">
                                <button type="submit" formaction="updateProduct.jsp?productId=<%=product.getId()%>" class="btn btn-success">Update</button>
                            </form>
                        </td>
                        <td>
                            <form method="post">
                                <button type="submit" formaction="/RemoveProductSrv?productId=<%=product.getProdId()%>" class="btn btn-danger">Remove</button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                        if (products.size() == 0) {
                    %>
                    <tr style="background-color: rgba(255, 0, 0, 0.704); color: white;">
                        <td colspan="8" style="text-align: center;">No Items Available</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>

    <div class="footer">
        <p>NamasteMart &copy; 2024. All Rights Reserved.</p>
    </div>
</body>
</html>