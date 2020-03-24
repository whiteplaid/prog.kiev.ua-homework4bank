import javax.persistence.*;

@Entity (name = "Currencies")
@Table (name = "currency")
public class Currencies {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "current")
    private double current;

    public Currencies(String name, double current) {
        this.name = name;
        this.current = current;
    }

    public Currencies() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

}
