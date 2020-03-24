import javax.persistence.*;
import java.util.Date;

@Entity(name = "Transactions")
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue
    private int id;

    //private String currency;

    @Column (name = "destinationCurrency")
    private String dCurrency;

    @Column (name = "dateOfOperation")
    private java.sql.Date oDate = new java.sql.Date(System.currentTimeMillis());

    @Column (name = "time")
    private java.sql.Time time = new java.sql.Time(System.currentTimeMillis());

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_sender")
    private Accounts sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_recipient")
    private Accounts recipient;

    private double amount;

    public Transactions(Accounts sender, Accounts recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount=amount;
        this.dCurrency=recipient.getCurrency();
    }

    public Transactions() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
//    public String getCurrency() {
//        return currency;
//    }

//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }
    public String getdCurrency() {
        return dCurrency;
    }

    public void setdCurrency(String dCurrency) {
        this.dCurrency = dCurrency;
    }

    public Date getoDate() {
        return oDate;
    }

//    public void setoDate(Date oDate) {
//        this.oDate = oDate;
//    }

    public Accounts getSender() {
        return sender;
    }

    public void setSender(Accounts sender) {
        this.sender = sender;
    }

    public Accounts getRecipient() {
        return recipient;
    }

    public void setRecipient(Accounts recipient) {
        this.recipient = recipient;
    }
    private Boolean checkAmount (Accounts sender, double amount) {
        boolean result = false;
        if (sender.getAmount() > amount) {
            result = true;
        }
        return result;
    }
}
