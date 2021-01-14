package main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Members")
public class Member {

    public Member() {}

    public Member(long id, String email, String firstName, String lastName, String phone) {
        setId(id);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
    }

    @Id
    private long Id;
    public long getId() {return Id;}
    public void setId(long id) {this.Id = id;}

    @Column(name="email", length=320)
    private String email;
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    @Column(name="firstName", nullable=false, length=20)
    private String firstName;
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    @Column(name="lastName", nullable = false, length=20)
    private String lastName;
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    @Column(name="phone", length=10)
    private String phone;
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone=phone;}

    @Column(name="balance", nullable=false)
    @ColumnDefault("100")
    private float balance;
    public float getBalance() {return balance;}
    public void setBalance(float balance) {this.balance = balance;}

    @OneToMany(fetch=FetchType.EAGER, mappedBy="member", cascade=CascadeType.PERSIST)
    private final List<Purchase> transactions = new ArrayList<>();
    public ObservableList<Purchase> getTransactions() {return FXCollections.observableList(transactions);}
    public void addTransaction(Purchase purchase) {transactions.add(purchase);}
}
