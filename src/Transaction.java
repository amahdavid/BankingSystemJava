import java.util.Date;

public class Transaction {
    private String transactionID;
    private String transactionType;
    private double amount;
    private Date date;
    private String sender;
    private String recipient;

    public Transaction(String transactionID, String transactionType,
                       double amount, String sender, String recipient)
    {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.date = new Date();
    }

    // More methods to access transaction history and printing them
}
