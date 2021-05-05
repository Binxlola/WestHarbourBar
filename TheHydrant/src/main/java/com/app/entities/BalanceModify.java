package main.java.com.app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Balance_transactions")
public class BalanceModify extends  Transaction{

    public BalanceModify() {}

    public BalanceModify(Member member, Date dateOf, float amount) {
        setMember(member);
        setDateOf(dateOf);
        setType(amount < 0.0 ? TransactionType.CASH_OUT : TransactionType.CASH_IN);
        setAmount(amount);
    }

    @Column(name = "amount", nullable = false, columnDefinition = "Decimal(19,2)")
    private float amount;
    public float getAmount() {return amount;}
    public void setAmount(float amount) {this.amount = amount;}
}
