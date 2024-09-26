package main.java.com.banksystem;

import java.util.UUID;

public class Account {
    private String accountID;
    private double balance;
    private String userID;
    private String accountType;

    public Account(String userID, String accountType) {
        this.accountID = UUID.randomUUID().toString();
        this.userID = userID;
        this.accountType = accountType;
        this.balance = 0.0;
    }

    public Account(String accountID, double balance, String userID, String accountType) {
        this.accountID = accountID;
        this.balance = balance;
        this.userID = userID;
        this.accountType = accountType;
    }

    public void deposit(double amount) throws ExceptionHandler {
        if (amount <= 0) {
            throw new ExceptionHandler("Deposit amount must be greater than zero.");
        }
        balance += amount;
    }

    public void withdraw(double amount) throws ExceptionHandler {
        if (amount > balance) {
            throw new ExceptionHandler("Insufficient funds");
        }
        balance -= amount;
    }

    public void transfer(Account recipient, double amount) throws ExceptionHandler {
        if (amount > this.balance) {
            throw new ExceptionHandler("Insufficient funds to complete the transfer.");
        }
        this.withdraw(amount);
        recipient.deposit(amount);
    }

    public String getAccountID() {return accountID;}

    public void setAccountID(String accountID) {this.accountID = accountID;}

    public double getBalance() {return balance;}

    public void setBalance(double balance) {this.balance = balance;}

    public String getAccountType() {return accountType;}

    public void setAccountType(String accountType) {this.accountType = accountType;}

    public String getUserID() {return userID;}
}
