package com.namastemart.service.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.namastemart.beans.*;
import com.namastemart.service.OrderService;
import com.namastemart.utility.DBUtil;
import com.namastemart.utility.MailMessage;

public class OrderServiceImpl implements OrderService {

    @Override
    public String paymentSuccess(String userName, double paidAmount) {

        String status = "Order Placement Failed!";
        List<CartBean> cartItems = new CartServiceImpl().getAllCartItems(userName);

        if (cartItems.isEmpty()) return status;

        TransactionBean transaction = new TransactionBean(
                generateTransactionId(),
                userName,
                new Timestamp(System.currentTimeMillis()),
                paidAmount
        );

        boolean ordered = true;
        String transactionId = transaction.getTransactionId();

        for (CartBean item : cartItems) {
            double amount = new ProductServiceImpl()
                    .getProductPrice(item.getProdId()) * item.getQuantity();

            OrderBean order = new OrderBean(
                    transactionId,
                    item.getProdId(),
                    item.getQuantity(),
                    amount
            );

            ordered = addOrder(order);
            if (!ordered) break;

            ordered = new CartServiceImpl().removeAProduct(item.getUserId(), item.getProdId());
            if (!ordered) break;

            ordered = new ProductServiceImpl().sellNProduct(item.getProdId(), item.getQuantity());
            if (!ordered) break;
        }

        if (ordered && addTransaction(transaction)) {
            MailMessage.transactionSuccess(
                    userName,
                    new UserServiceImpl().getFName(userName),
                    transaction.getTransactionId(),
                    transaction.getTransAmount()
            );
            status = "Order Placed Successfully!";
        }

        return status;
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }

    @Override
    public boolean addOrder(OrderBean order) {

        String sql = "INSERT INTO orders (orderid, prodid, quantity, amount, shipped, order_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, order.getTransactionId());
            ps.setString(2, order.getProductId());
            ps.setInt(3, order.getQuantity());
            ps.setDouble(4, order.getAmount());
            ps.setInt(5, 0);
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean addTransaction(TransactionBean transaction) {

        String sql = "INSERT INTO transactions (transid, username, time, amount, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, transaction.getTransactionId());
            ps.setString(2, transaction.getUserName());
            ps.setTimestamp(3, transaction.getTransDateTime());
            ps.setDouble(4, transaction.getTransAmount());
            ps.setString(5, "completed");

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int countSoldItem(String prodId) {
        int count = 0;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT SUM(quantity) FROM orders WHERE prodid = ?")) {

            ps.setString(1, prodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public List<OrderBean> getAllOrders() {
        List<OrderBean> orderList = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM orders");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderBean order = new OrderBean(
                        rs.getString("orderid"),
                        rs.getString("prodid"),
                        rs.getInt("quantity"),
                        rs.getDouble("amount"),
                        rs.getInt("shipped")
                );
                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    @Override
    public List<OrderBean> getOrdersByUserId(String emailId) {
        List<OrderBean> orderList = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT o.orderid, o.prodid, o.quantity, o.amount, o.shipped " +
                     "FROM orders o " +
                     "INNER JOIN transactions t ON o.orderid = t.transid " +
                     "WHERE t.username = ?")) {

            ps.setString(1, emailId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderBean order = new OrderBean(
                            rs.getString("orderid"),
                            rs.getString("prodid"),
                            rs.getInt("quantity"),
                            rs.getDouble("amount"),
                            rs.getInt("shipped")
                    );
                    orderList.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    @Override
    public List<OrderDetails> getAllOrderDetails(String userEmailId) {
        List<OrderDetails> orderList = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.prod_id AS prodid, o.orderid AS orderid, o.shipped AS shipped, " +
                     "p.prod_image AS image, " +
                     "p.prod_name AS pname, o.quantity AS qty, o.amount AS amount, t.time AS time " +
                     "FROM orders o " +
                     "INNER JOIN products p ON p.prod_id = o.prodid " +
                     "INNER JOIN transactions t ON t.transid = o.orderid " +
                     "WHERE t.username = ?")) {

            ps.setString(1, userEmailId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetails order = new OrderDetails();
                    order.setOrderId(rs.getString("orderid"));
                    order.setProdImage(rs.getAsciiStream("image"));
                    order.setProdName(rs.getString("pname"));
                    order.setQty(rs.getString("qty"));
                    order.setAmount(rs.getString("amount"));
                    order.setTime(rs.getTimestamp("time"));
                    order.setProductId(rs.getString("prodid"));
                    order.setShipped(rs.getInt("shipped"));
                    orderList.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    @Override
    public String shipNow(String orderId, String prodId) {
        String status = "FAILURE";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE orders SET shipped = 1 WHERE orderid = ? AND prodid = ? AND shipped = 0")) {

            ps.setString(1, orderId);
            ps.setString(2, prodId);

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "Order Has been shipped successfully!!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public int getTotalOrders() {

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM orders");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public double getTotalAmount() {

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT SUM(amount) FROM transactions");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    @Override
    public List<Integer> getMonthlySales() {
        List<Integer> monthlySales = new ArrayList<>(Collections.nCopies(12, 0));
        String query = "SELECT COUNT(*) AS total_sales, MONTH(order_date) AS month " +
                "FROM orders " +
                "WHERE YEAR(order_date) = YEAR(CURDATE()) " +
                "GROUP BY MONTH(order_date) " +
                "ORDER BY month";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int month = rs.getInt("month");
                int totalSales = rs.getInt("total_sales");
                monthlySales.set(month - 1, totalSales);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return monthlySales;
    }

    @Override
    public List<Double> getMonthlyProfit() {
        List<Double> monthlyProfit = new ArrayList<>(Collections.nCopies(12, 0.0));
        String query = "SELECT SUM(amount) AS total_profit, MONTH(order_date) AS month " +
                "FROM orders " +
                "WHERE YEAR(order_date) = YEAR(CURDATE()) " +
                "GROUP BY MONTH(order_date) " +
                "ORDER BY month";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int month = rs.getInt("month");
                double totalProfit = rs.getDouble("total_profit");
                monthlyProfit.set(month - 1, totalProfit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return monthlyProfit;
    }

    @Override
    public OrderDetails getOrderDetailsById(String orderId) {
        OrderDetails orderDetails = null;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM orders WHERE orderid = ?")) {

            ps.setString(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    orderDetails = new OrderDetails();
                    orderDetails.setOrderId(rs.getString("orderid"));
                    orderDetails.setProductId(rs.getString("prodid"));
                    orderDetails.setQty(rs.getString("quantity"));
                    orderDetails.setAmount(rs.getString("amount"));
                    orderDetails.setShipped(rs.getInt("shipped"));
                    orderDetails.setTime(rs.getTimestamp("order_date"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

    @Override
    public boolean cancelOrder(String orderId, String accountNumber, String bank,
                               String ifscCode, String userEmail, String userName) {

        OrderDetails orderDetails = getOrderDetailsById(orderId);
        if (orderDetails == null) return false;

        double refundAmount = Double.parseDouble(orderDetails.getAmount());

        boolean refundProcessed = processRefund(accountNumber, bank, ifscCode, refundAmount);
        if (!refundProcessed) return false;

        boolean statusUpdated = updateOrderStatus(orderId, "CANCELLED");
        if (!statusUpdated) return false;

        boolean amountUpdated = updateOrderAmount(orderId, -refundAmount);
        if (!amountUpdated) return false;

        if (userEmail == null) userEmail = "default@example.com";
        if (userName == null) userName = "Valued Customer";
        MailMessage.orderCancelled(userEmail, userName, orderId, refundAmount);

        return true;
    }

    @Override
    public boolean updateOrderStatus(String orderId, String status) {

        String sql = "UPDATE orders SET shipped = ? WHERE orderid = ?";
        int statusCode;

        if ("CANCELLED".equalsIgnoreCase(status)) {
            statusCode = 2;
        } else if ("SHIPPED".equalsIgnoreCase(status)) {
            statusCode = 1;
        } else {
            statusCode = 0;
        }

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, statusCode);
            ps.setString(2, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean processRefund(String accountNumber, String bank,
                                 String ifscCode, double amount) {
        System.out.println("Processing refund for account: " + accountNumber + ", amount: " + amount);
        return true;
    }

    @Override
    public boolean updateOrderAmount(String orderId, double amountChange) {
        String updateOrderQuery = "UPDATE orders SET amount = amount + ? WHERE orderid = ?";
        String updateTransactionQuery = "UPDATE transactions SET amount = amount + ?, status = 'refunded' WHERE transid = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement updateOrderStmt = con.prepareStatement(updateOrderQuery);
             PreparedStatement updateTransStmt = con.prepareStatement(updateTransactionQuery)) {

            updateOrderStmt.setDouble(1, amountChange);
            updateOrderStmt.setString(2, orderId);
            int rowsOrder = updateOrderStmt.executeUpdate();

            updateTransStmt.setDouble(1, amountChange);
            updateTransStmt.setString(2, orderId);
            int rowsTrans = updateTransStmt.executeUpdate();

            if (rowsOrder > 0 && rowsTrans > 0) {
                return updateProductOnCancel(orderId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateProductOnCancel(String orderId) {
        String updateProductQuery =
                "UPDATE products SET prod_quantity = prod_quantity + " +
                "(SELECT quantity FROM orders WHERE orderid = ?) " +
                "WHERE prod_id = (SELECT prodid FROM orders WHERE orderid = ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement checkStmt = con.prepareStatement(
                     "SELECT shipped FROM orders WHERE orderid = ?");
             PreparedStatement updateStmt = con.prepareStatement(updateProductQuery)) {

            checkStmt.setString(1, orderId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt("shipped") == 2) {
                    updateStmt.setString(1, orderId);
                    updateStmt.setString(2, orderId);
                    return updateStmt.executeUpdate() > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}