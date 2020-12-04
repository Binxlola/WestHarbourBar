package main.java;

import javax.persistence.*;

@Entity
@Table(name="Products")
public class Product {

    public Product() {}

    public Product(String name, double cost) {
        setName(name);
        setCost(cost);
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

    @Column(name="quantity")
    private int quantity;
    public int getQuantity() {return this.quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="category", nullable=false)
    private ProductCategory category;
    public ProductCategory getCategory() {return category;}
    public void setCategory(ProductCategory category) {this.category = category;}


}
