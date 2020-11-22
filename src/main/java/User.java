package main.java;

public class User {

    private boolean isAdmin;
    private String firstName;
    private String lastName;
    private String ID;
    private String email;
    private String mobile;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {isAdmin = admin;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getID() {return ID;}

    public void setID(String ID) {this.ID = ID;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}
}
