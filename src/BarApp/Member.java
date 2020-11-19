package BarApp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Members")
public class Member {

    @Id private int memberID;

    public int getId() {
        return memberID;
    }
    public void setId(int id) {
        this.memberID = id;
    }
}
