package main.java;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Purchases")
public class Purchase {

    public Purchase() {}

    public Purchase(Member member, Date dateOf, Product product, float cost) {
        items = new ArrayList<>();
        items.add(product);
        setMember(member);
        setDateOf(dateOf);
        setTotalCost(cost);

    }

    public Purchase(Member member, Date dateOf, float cost, Product... products) {
        items = new ArrayList<>();
        items.addAll(Arrays.asList(products));
        setMember(member);
        setDateOf(dateOf);
        setTotalCost(cost);
    }

    @Id
    private long Id;
    public long getId() {return Id;}
    public void setId(long Id) {this.Id = Id;}

    @OneToOne
    @JoinColumn(name="memberID", referencedColumnName="ID")
    private Member member;
    public Member getMember() {return member;}
    public void setMember(Member member) {this.member = member;}

    @Column(name="dateOf", nullable = false)
    private Date dateOf;
    public Date getDateOf() {return dateOf;}
    public void setDateOf(Date dateOf) {this.dateOf = dateOf;}

    @Column(name="totalCost", nullable = false)
    private float totalCost;
    public float getTotalCost() {return totalCost;}
    public void setTotalCost(float totalCost) {this.totalCost = totalCost;}

    @OneToMany
    @JoinColumn(name="productID", referencedColumnName="ID")
    private List<Product> items;
    public List<Product> getItems() {return this.items;}
    public void setItems(List<Product> items) {this.items = items;}

}
