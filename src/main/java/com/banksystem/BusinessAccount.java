package main.java.com.banksystem;

import main.java.com.banksystem.Account;

public class BusinessAccount extends Account {
    private static final double TRANSACTION_FEE = 2.50;

    public BusinessAccount(String userID, String accountType) {
        super(userID, accountType);
    }
    public BusinessAccount(String accountID, double balance, String userID) {
        super(accountID, balance, userID, "Business");
    }

    @Override
    public void withdraw(double amount) throws ExceptionHandler {
        double totalAmount = amount + TRANSACTION_FEE;
        if (totalAmount > getBalance()) {
            throw new ExceptionHandler("Insufficient funds including transaction fee.");
        }
        super.withdraw(totalAmount);
        System.out.println("main.java.com.banksystem.Transaction fee applied: " + TRANSACTION_FEE);
    }

}
