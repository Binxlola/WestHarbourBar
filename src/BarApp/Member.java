package BarApp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Members")
public class Member {

    public Member() {}

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
}
