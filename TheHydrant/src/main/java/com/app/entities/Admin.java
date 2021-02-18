package main.java.com.app.entities;

import main.java.com.app.util.PasswordUtil;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="Admin")
public class Admin extends Member{

    @Column(name="admin", nullable=false, columnDefinition="boolean default 'FALSE")
    boolean isAdmin = false;
    public boolean isAdmin() {return this.isAdmin;}
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Column(name="salt", nullable = false)
    String salt;
    public String getSalt() {return this.salt;}
    public void setSalt(String salt) {
        if(this.salt != null) {
            this.salt = salt;
        } else {
            System.err.println("There is already a salt in use.\nCannot manually override the salt used!");
        }
    }

    @Column(name="password", nullable=false)
    String password;
    public String getPassword() {return this.password;}
    public void setPassword(String password) {
        String salt = PasswordUtil.generateSaltString();
        String securePassword = PasswordUtil.hashPassword(password, salt).get();

        if(PasswordUtil.verifyPassword(password, securePassword, salt)) {
            this.password = securePassword;
            this.salt = salt;
        } else {
            System.err.println("There was an error hashing or verifying the hashed password");
        }
    }
}
