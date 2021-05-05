package main.java.com.app.entities;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    public long getId() {return Id;}
    public void setId(long Id) {this.Id = Id;}

    @Column(name="Transaction_type")
    private TransactionType type;
    public void setType(TransactionType type) {this.type = type;}
    public TransactionType getType() {return type;}

    @ManyToOne(optional = false)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    public Member getMember() {return member;}
    public void setMember(Member member) {this.member = member;}

    @Column(name = "dateOf", nullable = false)
    private Date dateOf;
    public Date getDateOf() {return dateOf;}
    public void setDateOf(Date dateOf) {this.dateOf = dateOf;}
    public String getDateShort() {return new SimpleDateFormat("dd MMM yyyy").format(dateOf); }

    public String getTransactionName() {
        if(this instanceof Purchase) {
            return ((Purchase)this).getProductName();
        } else if(this instanceof BalanceModify) {
            return type.toString();
        }

        return null;
    }

    public Float getTransactionValue() {
        if(this instanceof Purchase) {
            return ((Purchase)this).getProductCost();
        } else if(this instanceof BalanceModify) {
            return ((BalanceModify)this).getAmount();
        }

        return null;
    }


    public enum TransactionType {
        PURCHASE, CASH_IN, CASH_OUT
    }
}
