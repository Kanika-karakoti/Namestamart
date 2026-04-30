package com.namastemart.service;

import java.util.List;
import com.namastemart.beans.TransactionBean;

public interface TransService {

    // Method to get the user ID based on the transaction ID
    public String getUserId(String transId);

    // Method to get all transactions
    public List<TransactionBean> getAllTransactions();

    // Method to refund transactions
    public String refundTransaction(String transId);
}
