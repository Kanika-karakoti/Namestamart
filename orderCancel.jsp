<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.namastemart.service.OrderService,
com.namastemart.service.impl.OrderServiceImpl, com.namastemart.beans.OrderDetails,
com.namastemart.utility.MailMessage" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cancel Order</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/changes.css">
    
    <style>
        .btn-danger{
            margin-left: 520px;
            margin-top: 10px;
            font-size: large;
            font-weight: bolder;
        }
        
        .alert-success{
            margin-top: 20px;
            font-size: x-large;
            font-weight: bolder;
        }
    </style>
</head>
<body style="margin-top: 60px;">

<jsp:include page="profileHeader.jsp" />

<div class="container">
    <h2 class="text-center">Cancel Your Order</h2>
    <form action="orderCancel.jsp" method="post">
        <div class="form-group">
            <label for="orderId">Order ID:</label>
            <input type="text" class="form-control" id="orderId" name="orderId" required
            value="<%= request.getParameter("orderId") %>">
        </div>
        <div class="form-group">
            <label for="accountNumber">Account Number:</label>
            <input type="text" class="form-control" id="accountNumber" name="accountNumber" required>
        </div>
        <div class="form-group">
            <label for="bank">Bank Name:</label>
            <input type="text" class="form-control" id="bank" name="bank" required>
        </div>
        <div class="form-group">
            <label for="ifscCode">IFSC Code:</label>
            <input type="text" class="form-control" id="ifscCode" name="ifscCode" required>
        </div>
        <button type="submit" class="btn btn-danger">Submit</button>
    </form>

<%
String message = "";
String orderId = request.getParameter("orderId");
String accountNumber = request.getParameter("accountNumber");
String bank = request.getParameter("bank");
String ifscCode = request.getParameter("ifscCode");

// Retrieve user email and name from session
String userEmail = (String) session.getAttribute("username");
String userName = (String) session.getAttribute("userName");

if (userEmail == null) {
    response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
} else {
    if (orderId != null && !orderId.isEmpty() &&
        accountNumber != null && !accountNumber.isEmpty() &&
        bank != null && !bank.isEmpty() &&
        ifscCode != null && !ifscCode.isEmpty()) {
        
        OrderService orderService = new OrderServiceImpl();
        try {
            OrderDetails orderDetails = orderService.getOrderDetailsById(orderId);
            if (orderDetails != null) {
                boolean isCancelled = orderService.cancelOrder(orderId, accountNumber, bank, ifscCode, userEmail, userName);
                
                if (isCancelled) {
                    message = "Request for cancellation for Order ID " + orderId + " is successful!";
                } else {
                    message = "Failed to cancel the order. Please try again.";
                }
            } else {
                message = "Order not found.";
            }
        } catch (NumberFormatException e) {
            message = "Error processing the refund amount. Please contact support.";
            e.printStackTrace();
        } catch (Exception e) {
            message = "An unexpected error occurred: " + e.getMessage();
            e.printStackTrace();
        }
    } else {
        message = "";
    }
%>

<div class="alert alert-success">
    <%= message %>
</div>
</div>

<jsp:include page="footer.html"/>
</body>
</html>