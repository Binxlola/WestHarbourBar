package main.java.com.app.entities;

import javax.persistence.*;

@Entity
@Table(name="Products")
public class Product {

    public Product() {}

    public Product(String name, float cost, int quantity, ProductCategory  category, byte[] image) {
        setName(name);
        setCost(cost);
        setQuantity(quantity);
        setCategory(category);
        setImage(image);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public long getID() {return this.id;}
    public void setID(long id) {this.id = id;}

    @Column(name="name", nullable=false, length=20)
    private String name;
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    @Column(name="cost", nullable=false)
    private float cost;
    public float getCost() {return this.cost;}
    public void setCost(float cost) {this.cost = cost;}

    @Column(name="quantity")
    private int quantity;
    public int getQuantity() {return this.quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="category", nullable=false)
    private ProductCategory category;
    public ProductCategory getCategory() {return category;}
    public void setCategory(ProductCategory category) {this.category = category;}

    @Lob
    @Column(name="image", nullable=false, columnDefinition="mediumblob")
    private byte[] image;
    public byte[] getImage() {return image;}
    public void setImage(byte[] image) {this.image = image;}
}