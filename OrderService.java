package com.namastemart.service;

import java.util.List;

import com.namastemart.beans.OrderBean;
import com.namastemart.beans.OrderDetails;
import com.namastemart.beans.TransactionBean;

public interface OrderService {

    public String paymentSuccess(String userName, double paidAmount);

    public boolean addOrder(OrderBean order);

    public boolean addTransaction(TransactionBean transaction);

    public int countSoldItem(String prodId);

    public List<OrderBean> getAllOrders();

    public List<OrderBean> getOrdersByUserId(String emailId);

    public List<OrderDetails> getAllOrderDetails(String userEmailId);

    public String shipNow(String orderId, String prodId);

    public int getTotalOrders();

    public double getTotalAmount();

    public List<Integer> getMonthlySales();

    public List<Double> getMonthlyProfit();

    // methods for order cancellation
    public OrderDetails getOrderDetailsById(String orderId);

    public boolean cancelOrder(String orderId, String accountNumber, String bank,
                               String ifscCode, String userEmail, String userName);

    public boolean updateOrderStatus(String orderId, String status);

    public boolean processRefund(String accountNumber, String bank,
                                 String ifscCode, double amount);

    public boolean updateOrderAmount(String orderId, double amountChange);

    public boolean updateProductOnCancel(String orderId);
}
