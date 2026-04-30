/*package com.namastemart.bean;

import java.sql.Timestamp;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import com.namastemart.utility.IDUtil;

@SuppressWarnings("serial")
public class TransactionBean implements Serializable {
	
      private String transactionId;
	private String userName;
	private Timestamp transDateTime;
	private double transAmount;
    private String status;//field for status
	
	public TransactionBean(){
		super();
		this.transactionId = IDUtil.generateTransId();
		SimpleDataFormate sdf = new SimpleDataFormate("YYYY-MM-DD hh:mm:ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sdf.format(timestamp);
		this.transDateTime = timestamp;
		this.status = "paid";//default status
	}
	
	public TransactionBean(String userName, double transAmount) {
        super();
        
        this.userName = userName;
        this.transAmount = transAmount;
		this.transactionId = IDUtil.generateTransId();
		SimpleDataFormate sdf = new SimpleDataFormate("YYYY-MM-DD hh:mm:ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sdf.format(timestamp);
		this.transDateTime = timestamp;
		
        this.status = "paid";
    }
	public TransactionBean(String transactionId, String userName, double transAmount) {
        super();
        this.transactionId = transactionId;
        this.userName = userName;
        this.transAmount = transAmount;
        SimpleDataFormate sdf = new SimpleDataFormate("YYYY-MM-DD hh:mm:ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sdf.format(timestamp);
		this.transDateTime = timestamp;
		
        this.status = "paid";
    }
	
    public TransactionBean( String userName, Timestamp transDateTime, double transAmount) {
        super();
        
        this.userName = userName;
        this.transDateTime = transDateTime;
		this.transactionId = IDUtil.generateTransId();
        this.transAmount = transAmount;
        this.status = "paid"; // Default status
    }
    public TransactionBean(String transactionId, String userName, Timestamp transDateTime, double transAmount) {
		super();
		this.userName = userName; 
		this.transAmount = transAmount;
		SimpleDataFormate sdf = new SimpleDataFormate("YYYY-MM-DD hh:mm:ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sdf.format(timestamp);
		this.transDateTime = timestamp;
		
        this.status = "paid";//default
	}
    public TransactionBean(String transactionId, String userName,  double transAmount) {
		super();
		this.userName = userName; 
		this.transAmount = transAmount;
		SimpleDataFormate sdf = new SimpleDataFormate("YYYY-MM-DD hh:mm:ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sdf.format(timestamp);
		this.transDateTime = timestamp;
		
        this.status = "paid";//default
	}
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Timestamp getTransDateTime() {
        return transDateTime;
    }
    
    public void setTransDateTime(Timestamp transDateTime) {
        this.transDateTime = transDateTime;
    }
    
    public double getTransAmount() {
        return transAmount;
    }
    
    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
   
}
*/
package com.namastemart.beans;

import java.sql.Timestamp;
import java.io.Serializable;

public class TransactionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String userName;
    private Timestamp transDateTime;
    private double transAmount;
    private String status;

    public TransactionBean(String transactionId, String userName,
                           Timestamp transDateTime, double transAmount) {
        this.transactionId = transactionId;
        this.userName = userName;
        this.transDateTime = transDateTime;
        this.transAmount = transAmount;
        this.status = "paid";
    }

    public TransactionBean(String transactionId, String userName,
                           Timestamp transDateTime, double transAmount,
                           String status) {
        this.transactionId = transactionId;
        this.userName = userName;
        this.transDateTime = transDateTime;
        this.transAmount = transAmount;
        this.status = status;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Timestamp getTransDateTime() { return transDateTime; }
    public void setTransDateTime(Timestamp transDateTime) { this.transDateTime = transDateTime; }

    public double getTransAmount() { return transAmount; }
    public void setTransAmount(double transAmount) { this.transAmount = transAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}