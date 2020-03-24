import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity (name = "Users")
@Table (name = "users")
public class Users implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL)
    private List<Accounts> accounts = new ArrayList<>();

    @Column (nullable = false)
    private String name;

    @Column
    private long phone;

    @Column
    private String email;

    public Users(String name, long phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Users() {
    }

    public long getId() {
        return id;
    }

     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void addAccount (Accounts account) {
        account.setUser(this);
        accounts.add(account);
    }
}
