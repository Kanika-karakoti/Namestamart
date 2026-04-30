package com.namastemart.utility;

import jakarta.mail.MessagingException;

public class MailMessage {
    public static String Uname = "";

    public static void registrationSuccess(String emailId, String name) {
        String recipient = emailId;
        String subject = "Registration Successful";
        String htmlTextMessage = "<html>" +
                "<body>" +
                "<h2 style='color:green;'>Welcome to NamasteMart</h2>" +
                "Hello " + name + "," +
                "<br><br>Thank you for signing up with NamasteMart." +
                "<br>We're thrilled to have you. Check out our latest collection of products with up to 60% OFF on select items." +
                "<br><br>Enjoy free delivery on all orders and explore a wide range of top brands." +
                "<br>As a welcome gift, enjoy an additional 10% OFF up to 500 on your first purchase with the promo code below." +
                "<br><br><strong>PROMO CODE:<span style=\"color:green;\">NAMASTE500</span></strong>" +
                "<br><br>Happy shopping!<br><br>" +
                "Best regards,<br>" +
                "<strong>NamasteMart Team</strong>" +
                "</body>" +
                "</html>";

        try {
            JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void transactionSuccess(String recipientEmail, String name, String transId,
            double transAmount) {
        String recipient = recipientEmail;
        Uname = name;
        String subject = "Order Placed at NamasteMart";
        String htmlTextMessage = "<html>" +
                "<body>" +
                "<h2 style='color:green;'>Order Placed at NamasteMart</h2>" +
                "Hello " + Uname + "," +
                "<br><br>Thank you for shopping with NamasteMart!" +
                "<br>Your order has been successfully placed and is being processed for shipping." +
                "<br><br>Here are your transaction details:" +
                "<br><br><font style='color:red;font-weight:bold;'>Order ID:</font> <font style='color:green;font-weight:bold;'>" + transId + "</font>" +
                "<br><font style='color:red;font-weight:bold;'>Amount Paid:</font> <font style='color:green;font-weight:bold;'>" + transAmount + "</font>" +
                "<br><br>Thank you for shopping with us! We look forward to serving you again." +
                "<br><br>Best regards,<br>" +
                "<strong>NamasteMart Team</strong>" +
                "</body>" +
                "</html>";

        try {
            JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void orderShipped(String recipientEmail, String name, String transId,
            double transAmount) {
        String recipient = recipientEmail;
        String subject = "Hurray!! Your Order has been Shipped from NamasteMart";
        String htmlTextMessage = "<html>" +
                "<body>" +
                "<h2 style='color:green;'>Your Order is on the Way!</h2>" +
                "Hello " + name + "," +
                "<br><br>We are excited to inform you that your order has been shipped and is on its way to you." +
                "<br><br>Here are your transaction details:" +
                "<br><br><font style='color:red;font-weight:bold;'>Order ID:</font> <font style='color:green;font-weight:bold;'>" + transId + "</font>" +
                "<br><font style='color:red;font-weight:bold;'>Amount Paid:</font> <font style='color:green;font-weight:bold;'>" + transAmount + "</font>" +
                "<br><br>Thank you for shopping with NamasteMart! We hope you enjoy your purchase." +
                "<br><br>Best regards,<br>" +
                "<strong>NamasteMart Team</strong>" +
                "</body>" +
                "</html>";

        try {
            JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void productAvailableNow(String recipientEmail, String name, String prodName, String prodId) {
        String recipient = recipientEmail;
        String subject = "Product " + prodName + " is Now Available at NamasteMart";
        String htmlTextMessage = "<html>" +
                "<body>" +
                "<h2 style='color:green;'>Good News! " + prodName + " is Now Available</h2>" +
                "Hey " + name + "," +
                "<br><br>We noticed that you were interested in a product that was out of stock during your recent visit." +
                "<br>We're excited to inform you that the product <strong>" + prodName +
                "</strong> (Product ID: <strong>" + prodId + "</strong>) is now back in stock and available for purchase!" +
                "<br><br>Product Details:" +
                "<br><font style='color:red;font-weight:bold;'>Product ID:</font> <font style='color:green;font-weight:bold;'>" + prodId + "</font>" +
                "<br><font style='color:red;font-weight:bold;'>Product Name:</font> <font style='color:green;font-weight:bold;'>" + prodName + "</font>" +
                "<br><br>Thank you for choosing NamasteMart! We hope to see you shopping with us soon." +
                "<br><br>Best regards,<br>" +
                "<strong>NamasteMart Team</strong>" +
                "</body>" +
                "</html>";

        try {
            JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void orderCancelled(String recipientEmail, String name, String orderId,
            double refundAmount) {
        String recipient = recipientEmail;
        String subject = "Order Cancellation Confirmation from NamasteMart";
        String htmlTextMessage = "<html>" +
                "<body>" +
                "<h2 style='color:red;'>Order Cancellation Confirmation</h2>" +
                "Hello " + Uname + "," +
                "<br><br>We regret to inform you that your order with Order ID " + orderId +
                " has been cancelled as per your request." +
                "<br><br>A refund of <font style='color:green;font-weight:bold;'>" + refundAmount +
                "</font> will be processed to your account shortly." +
                "<br><br>Thank you for your understanding. If you have any questions, please contact our support team." +
                "<br><br>Best regards,<br>" +
                "<strong>NamasteMart Team</strong>" +
                "</body>" +
                "</html>";

        try {
            JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String sendMessage(String toEmailId, String subject, String htmlTextMessage) {
        try {
            JavaMailUtil.sendMail(toEmailId, subject, htmlTextMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "FAILURE";
        }
        return "SUCCESS";
    }
}