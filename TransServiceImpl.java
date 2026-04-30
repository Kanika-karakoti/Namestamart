package com.namastemart.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.namastemart.beans.TransactionBean;
import com.namastemart.service.TransService;
import com.namastemart.utility.DBUtil;

public class TransServiceImpl implements TransService {

    @Override
    public String getUserId(String transId) {
        String userId = "";
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT username FROM transactions WHERE transid = ?");
            ps.setString(1, transId);
            rs = ps.executeQuery();

            if (rs.next()) {
                userId = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userId;
    }

    @Override
    public List<TransactionBean> getAllTransactions() {
        List<TransactionBean> transactionList = new ArrayList<>();
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT transid, username, time, amount, status FROM transactions");
            rs = ps.executeQuery();

            while (rs.next()) {
                String transactionId = rs.getString("transid");
                String userName = rs.getString("username");
                Timestamp transDateTime = rs.getTimestamp("time");
                double transAmount = rs.getDouble("amount");
                String status = rs.getString("status");

                TransactionBean transaction = new TransactionBean(transactionId, userName,
                        transDateTime, transAmount);
                transaction.setStatus(status);
                transactionList.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return transactionList;
    }

    @Override
    public String refundTransaction(String transId) {
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        double amount = 0.0;
        String userEmail = "";

        try {
            // Get transaction details
            ps = con.prepareStatement("SELECT amount, username FROM transactions WHERE transid = ?");
            ps.setString(1, transId);
            rs = ps.executeQuery();

            if (rs.next()) {
                amount = rs.getDouble("amount");
                userEmail = rs.getString("username");
            }

            // Subtract the refunded amount
            ps = con.prepareStatement("UPDATE transactions SET amount = amount - ?, status = 'refunded' WHERE transid = ?");
            ps.setDouble(1, amount);
            ps.setString(2, transId);
            ps.executeUpdate();

            // return "Amount " + amount + " has been refunded to " + userEmail;
            return "<span style='color: red;'>Amount " + amount + "</span> has been refunded to " + userEmail;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Refund failed. Please try again.";
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}