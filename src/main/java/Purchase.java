package main.java;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Purchases")
public class Purchase {

    public Purchase() {}

    public Purchase(Member member, Date dateOf, Product product) {
        setMember(member);
        setDateOf(dateOf);
        setProduct(product);
    }

    @Id
    private long Id;
    public long getId() {return Id;}
    public void setId(long Id) {this.Id = Id;}

    @ManyToOne(optional=false)
    @JoinColumn(name="MEMBER_ID")
    private Member member;
    public Member getMember() {return member;}
    public void setMember(Member member) {this.member = member;}

    @Column(name="dateOf", nullable = false)
    private Date dateOf;
    public Date getDateOf() {return dateOf;}
    public void setDateOf(Date dateOf) {this.dateOf = dateOf;}
    public String getDateShort() {return new SimpleDateFormat("dd MMM yyyy").format(dateOf); }

    @OneToOne
    @JoinColumn(name="PRODUCT_ID")
    private Product item;
    public Product getProduct() {return this.item;}
    public void setProduct(Product items) {this.item = items;}
    public String getItemName() {return item.getName();}
    public float getItemCost() {return item.getCost();}

}
