<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1" %>

<%@ page import="com.namastemart.service.impl.*,
    com.namastemart.service.*,
    com.namastemart.beans.*,
    java.util.*,
    javax.servlet.ServletOutputStream,
    java.io.*" %>

<!DOCTYPE html>
<html>
<head>
<title>Cart Details</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
    href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="css/changes.css">

<link rel="stylesheet"
    href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<style>
table tbody>tr>td, table tbody>tr>th,
table tfoot>tr>td, table tfoot>tr>th,
table thead>tr>td, table thead>tr>th {
    border: 1px solid black !important;
}
</style>

<body>
<%
    // Checking the user credentials
    String userName = (String) session.getAttribute("username");
    String password = (String) session.getAttribute("password");

    if (userName == null || password == null) {
        response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    String addS = request.getParameter("add");
    if (addS != null) {
        int add = Integer.parseInt(addS);
        String uid = request.getParameter("uid");
        String pid = request.getParameter("pid");
        int avail = Integer.parseInt(request.getParameter("avail"));
        int cartQty = Integer.parseInt(request.getParameter("qty"));

        CartServiceImpl cartService = new CartServiceImpl(); // ✅ renamed

        if (add == 1) {
            cartQty += 1;
            if (cartQty <= avail) {
                cartService.addProductToCart(uid, pid, 1);
            } else {
                response.sendRedirect("./AddtoCart?pid=" + pid + "&qty=" + cartQty);
            }
        } else if (add == 0) {
            cartService.removeProductFromCart(uid, pid);
        }
    }
%>

<jsp:include page="header.jsp" />

<div class="text-center" style="color:black;font-size:24px;font-weight:bold;">
    Cart Items
</div>

<div class="container">
<table class="table table-hover">
<thead>
<tr>
<th>Picture</th>
<th>Products</th>
<th>Price</th>
<th>Quantity</th>
<th>Add</th>
<th>Remove</th>
<th>Amount</th>
</tr>
</thead>

<tbody style="background-color:#f1cdf6;font-size:15px;font-weight:bold;">
<%
    CartServiceImpl cart = new CartServiceImpl(); // unchanged
    List<CartBean> cartItems = cart.getAllCartItems(userName);
    double totAmount = 0;

    for (CartBean item : cartItems) {
        String prodId = item.getProdId();
        int prodQuantity = item.getQuantity();
        ProductBean product = new ProductServiceImpl().getProductDetails(prodId);

        double currAmount = product.getProdPrice() * prodQuantity;
        totAmount += currAmount;

        if (prodQuantity > 0) {
%>
<tr>
<td><img src="/ShowImage?pid=<%=product.getProdId()%>" style="width:50px;height:50px;"></td>
<td><%=product.getProdName()%></td>
<td><%=product.getProdPrice()%></td>

<td>
<form method="post" action="/UpdateToCart">
<input type="number" name="pgtty" value="<%=prodQuantity%>" style="max-width:70px;" min="0">
<input type="hidden" name="pid" value="<%=product.getProdId()%>">
<input type="submit" value="Update" style="max-width:80px;">
</form>
</td>

<td>
<a href="cartDetails.jsp?add=1&uid=<%=userName%>&pid=<%=product.getProdId()%>&qty=<%=prodQuantity%>&avail=<%=product.getProdQuantity()%>">
<i class="fa fa-plus"></i>
</a>
</td>

<td>
<a href="cartDetails.jsp?add=0&uid=<%=userName%>&pid=<%=product.getProdId()%>&qty=<%=prodQuantity%>">
<i class="fa fa-minus"></i>
</a>
</td>

<td><%=currAmount%></td>
</tr>
<%
        }
    }
%>

<tr style="background-color:#b341ab;color:white;">
<td colspan="6" style="text-align:center;">Total Amount to Pay (in Rupees)</td>
<td><%=totAmount%></td>
</tr>

<%
if (totAmount != 0) {
%>
<tr style="background-color:#b341ab;color:white;">
<td colspan="4" align="center">
<form method="post">
<button formaction="userHome.jsp" style="background-color:red;color:white;">Cancel</button>
</form>
</td>
<td colspan="2" align="center">
<form method="post">
<button formaction="payment.jsp?amount=<%=totAmount%>" style="background-color:rgb(7,178,7);color:white;">Pay Now</button>
</form>
</td>
</tr>
<%
}
%>
</tbody>
</table>
</div>

<%@ include file="footer.html" %>
</body>
</html>
