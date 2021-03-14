package main.java.com.app.entities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.app.util.PasswordUtil;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
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

    @Column(name="balance", nullable=false, columnDefinition="Decimal(19,2)")
    @ColumnDefault("0.00")
    private float balance;
    public float getBalance() {return balance;}
    public void setBalance(float balance) {this.balance = balance;}
    public void updateBalance(float updateAmount) {
        setBalance(this.balance + updateAmount);
    }

    @OneToMany(fetch=FetchType.EAGER, mappedBy="member", cascade=CascadeType.PERSIST)
    private final List<Purchase> transactions = new ArrayList<>();
    public ObservableList<Purchase> getTransactions() {return FXCollections.observableList(transactions);}
    public void addTransaction(Purchase purchase) {transactions.add(purchase);}

    @Column(name="admin", nullable=false)
    @ColumnDefault("false")
    boolean isAdmin = false;
    public boolean isAdmin() {return this.isAdmin;}
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Column(name="salt", columnDefinition="mediumblob")
    byte[] salt;
    public byte[] getSalt() {return this.salt;}
    public void setSalt(byte[] salt) {
        if(this.salt != null) {
            this.salt = salt;
        } else {
            System.err.println("There is already a salt in use.\nCannot manually override the salt used!");
        }
    }

    @Column(name="password", columnDefinition="mediumblob")
    byte[] password;
    public byte[] getPassword() {return this.password;}
    public void setPassword(String password) {
        byte[] salt = PasswordUtil.generateSaltByteArray();
        byte[] securePassword = PasswordUtil.hashPassword(password, salt);

        if(PasswordUtil.verifyPassword(password, securePassword, salt)) {
            this.password = securePassword;
            this.salt = salt;
        } else {
            System.err.println("There was an error hashing or verifying the hashed password");
        }
    }
    public boolean hasPassword() {return getPassword() == null;}
}
