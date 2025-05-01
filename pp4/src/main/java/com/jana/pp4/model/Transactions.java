package com.jana.pp4.model;


import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tranId;
    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "accId")
    private Account seller;
    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "accId")
    private Account buyer;
    @ManyToMany
    @JoinTable(
            name = "transaction_items", // Join table name
            joinColumns = @JoinColumn(name = "transaction_id"), // FK to Transaction
            inverseJoinColumns = @JoinColumn(name = "item_id") // FK to Item
    )
    private List<Item> items;
    private int Quantity;
    private float TotalAmount;
    private Date TranTime;

    public Transactions(){}


    public Transactions(int tranId, Account seller, Account buyer, List<Item> items, int quantity, float totalAmount, Date tranTime) {
        this.tranId = tranId;
        this.seller = seller;
        this.buyer = buyer;
        this.items = items;
        Quantity = quantity;
        TotalAmount = totalAmount;
        TranTime = tranTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "tranId=" + tranId +
                ", seller=" + seller.getAccId() +
                ", buyer=" + buyer.getAccId() +
                ", items=" + items +
                ", Quantity=" + Quantity +
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

    public Account getSeller() {
        return seller;
    }

    public void setSeller(Account seller) {
        this.seller = seller;
    }

    public Account getBuyer() {
        return buyer;
    }

    public void setBuyer(Account buyer) {
        this.buyer = buyer;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        TotalAmount = totalAmount;
    }

    public Date getTranTime() {
        return TranTime;
    }

    public void setTranTime(Date tranTime) {
        TranTime = tranTime;
    }
}

