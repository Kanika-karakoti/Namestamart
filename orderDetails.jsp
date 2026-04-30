<%@ page import="java.util.List" %>
<%@ page import="com.namastemart.beans.OrderDetails" %>
<%@ page import="com.namastemart.service.OrderService" %>
<%@ page import="com.namastemart.service.impl.*" %>

<!DOCTYPE html>
<html>
<head>
    <title>Order Details</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/changes.css">
    <style>
        table>tbody>tr>td, table>tbody>tr>th, table>tfoot>tr>td, table>tfoot>tr>th,
        table>thead>tr>td, table>thead>tr>th {
            border: 1px solid black !important;
        }
        
        .cancel-button {
            margin: 20px 0;
        }
        
        .disabled-btn {
            cursor: not-allowed;
            opacity: 0.6;
        }
    </style>
</head>
<body>

<%
// Checking the user credentials
String userName = (String) session.getAttribute("userName");
String password = (String) session.getAttribute("password");

if (userName == null || password == null) {
    response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
}

// Fetch order details for the logged-in user
OrderService dao = new OrderServiceImpl();
List<OrderDetails> orders = dao.getAllOrderDetails(userName);
%>

<jsp:include page="header.jsp" />

<div class="text-center" style="color: black; font-size: 24px; font-weight: bold;">Order Details</div>

<!-- Start of Product Items List -->
<div class="container">
    <div class="table-responsive">
        <table class="table table-hover table-sm">
            <thead style="background-color: #b341ab; color: white; font-size: 14px; font-weight: bold;">
                <tr>
                    <th>Picture</th>
                    <th>Product Name</th>
                    <th>Order ID</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Time</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody style="background-color: #f1cdf6; font-size: 15px; font-weight: bold;">
            <%
            for (OrderDetails order : orders) {
                String orderStatus = "";
                switch (order.getShipped()) {
                    case 0:
                        orderStatus = "ORDER_PLACED";
                        break;
                    case 1:
                        orderStatus = "SHIPPED";
                        break;
                    case 2:
                        orderStatus = "ORDER_CANCELED";
                        break;
                }
            %>
                <tr>
                    <td><img src="./ShowImage?pid=<%=order.getProductId()%>" style="width: 50px; height: 50px;"></td>
                    <td><%=order.getProdName()%></td>
                    <td><%=order.getOrderId()%></td>
                    <td><%=order.getQty()%></td>
                    <td><%=order.getAmount()%></td>
                    <td><%=order.getTime()%></td>
                    <td class="text-success"><%=orderStatus%></td>
                    <td>
                    <%
                    if (order.getShipped() == 0 || order.getShipped() == 1) {
                    %>
                        <a href="orderCancel.jsp?orderId=<%=order.getOrderId()%>" class="btn btn-danger btn-sm">Cancel</a>
                    <%
                    } else {
                    %>
                        <button class="btn btn-danger btn-sm disabled-btn" disabled>Cancelled</button>
                    <%
                    }
                    %>
                    </td>
                </tr>
            <%
            }
            %>
            </tbody>
        </table>
    </div>
</div>
<!-- End of Product Items List -->

<%@ include file="footer.html"%>
</body>
</html>