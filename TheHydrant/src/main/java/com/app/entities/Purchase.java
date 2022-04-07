package com.app.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Purchases")
public class Purchase extends Transaction {

    public Purchase() {}

    public Purchase(Member member, Date dateOf, Product product) {
        setMember(member);
        setDateOf(dateOf);
        setProduct(product);
        setType(TransactionType.PURCHASE);
        setBalanceAfter(member.getBalance());
    }

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product item;
    public Product getProduct() {return this.item;}
    public void setProduct(Product items) {this.item = items;}
    public String getProductName() {return item.getName();}
    public float getProductCost() {return item.getCost();}

}
