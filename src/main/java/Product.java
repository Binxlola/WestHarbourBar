package main.java;

import javax.persistence.*;

@Entity
@Table(name="Products")
public class Product {

    public Product() {}

    public Product(String name, double cost, Brand brand) {
        setName(name);
        setCost(cost);
        setBrand(brand);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;
    public long getID() {return this.ID;}
    public void setID(long id) {this.ID = id;}

    @Column(name="name", nullable=false, length=20)
    private String name;
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    @Column(name="cost", nullable=false)
    private double cost;
    public double getCost() {return this.cost;}
    public void setCost(double cost) {this.cost = cost;}

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name="brand")
    private Brand brand;
    public Brand getBrand() {return this.brand;}
    public void setBrand(Brand brand) {this.brand = brand;}


}
