package main.java.com.banksystem;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String transactionID;
    private String transactionType;
    private double amount;
    private Date date;
    private String sender;
    private String recipient;

    public Transaction(String transactionType, double amount, User sender, User recipient) {
        this.transactionID = "tran-" + UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.amount = amount;
        this.sender = sender.getEmail();
        this.recipient = (recipient != null) ? recipient.getEmail() : null;
        this.date = new Date();
    }

    public Transaction(String transactionID, String transactionType, double amount, Date date, User sender, User recipient) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.sender = sender.getEmail();
        this.recipient = (recipient != null) ? recipient.getEmail() : null;
    }

    public boolean saveTransaction() {
        return MyDatabase.insertTransaction(this);
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    // More methods to access transaction history and printing them
}
