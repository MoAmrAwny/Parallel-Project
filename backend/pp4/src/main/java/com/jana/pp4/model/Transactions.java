package com.jana.pp4.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Transactions {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tranId;  //will be incremented based on cnt of transactios in both dbs
    //but routing logic based on buyer id
    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "accId")
    private Account buyer;
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<TransactionItem> transactionItems;
    private BigDecimal TotalAmount;
    private Date TranTime;

    public Transactions(){}

    public Transactions(int tranId, Account buyer, List<TransactionItem> transactionItems, BigDecimal totalAmount, Date tranTime) {
        this.tranId = tranId;
        this.buyer = buyer;
        this.transactionItems = transactionItems;
        TotalAmount = totalAmount;
        TranTime = tranTime;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "tranId=" + tranId +
                ", buyer=" + buyer +
                ", transactionItems=" + transactionItems +
                ", TotalAmount=" + TotalAmount +
                ", TranTime=" + TranTime +
                '}';
    }

    public int getTranId() {
        return tranId;
    }

    public void setTranId(int tranId) {
        this.tranId = tranId;
    }

    public Account getBuyer() {
        return buyer;
    }

    public void setBuyer(Account buyer) {
        this.buyer = buyer;
    }

    public List<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }

    public BigDecimal getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        TotalAmount = totalAmount;
    }

    public Date getTranTime() {
        return TranTime;
    }

    public void setTranTime(Date tranTime) {
        TranTime = tranTime;
    }

    public void updateTotalAmount() {
        this.TotalAmount = transactionItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

