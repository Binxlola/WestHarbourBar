package main.java.com.app.entities;

import javax.persistence.*;

@Entity
@Table(name="ProductCategories")
public class ProductCategory {

    public ProductCategory() {}

    public ProductCategory(String name) {
        setName(name);
    }

    @Id
    @GeneratedValue
    private int id;
    public int getId() {return this.id;}

    @Column(name="name", nullable=false, length=32, unique=true)
    private String name;
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return this.name;
    }
}
