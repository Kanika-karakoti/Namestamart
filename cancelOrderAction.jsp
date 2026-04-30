<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.namastemart.service.OrderService, com.namastemart.service.impl.OrderServiceImpl, com.namastemart.beans.OrderDetails, com.namastemart.utility.MailMessage" %>

<%
String orderId = request.getParameter("orderId");

String accountNumber = request.getParameter("accountNumber");
String bank = request.getParameter("bank");
String ifscCode = request.getParameter("ifscCode");

OrderService orderService = new OrderServiceImpl();
OrderDetails orderDetails = orderService.getOrderDetailsById(orderId);

String message;
if (orderDetails != null) {
    try {
        double refundAmount = Double.parseDouble(String.valueOf(orderDetails.getAmount()));

        boolean isCancelled = orderService.cancelOrder(orderId, accountNumber, bank, ifscCode);

        if (isCancelled) {
            String userEmail = (String) session.getAttribute("email");
            MailMessage.orderCancelled(userEmail, orderDetails.getProdName(), orderId, refundAmount);
            message = "Your refund request has been received. It will be processed shortly.";
        } else {
            message = "Failed to cancel the order. Please try again.";
        }
    } catch (NumberFormatException e) {
        message = "Error processing the refund amount. Please contact support.";
    }
} else {
    message = "Order not found.";
}

request.setAttribute("message", message);
request.getRequestDispatcher("orderCancelConfirmation.jsp").forward(request, response);
%>
