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

    public Transaction(String transactionType,
                       double amount, String sender, String recipient) {
        this.transactionID = "tran-" + UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.date = new Date();
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
