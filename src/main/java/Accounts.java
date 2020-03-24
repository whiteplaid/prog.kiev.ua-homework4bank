import javax.persistence.*;
import java.util.List;


@Entity (name = "Accounts")
@Table (name = "accounts")
public class Accounts {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    private String currency;

    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    public Accounts() {
    }

    public Accounts(String currency, double amount, Users user) {
        this.currency = currency;
        this.amount = amount;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}
