package main.java;

import javax.persistence.*;

@Entity
@Table(name="Brands")
public class Brand {

    public Brand() {}

    public Brand(String name) {
        setName(name);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;
    public long getID() {return this.ID;}

    @Column(name="name")
    private String name;
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
}
