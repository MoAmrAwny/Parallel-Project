package com.jana.pp4.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@IdClass(TransactionItem.TransactionItemId.class)  // Link the entity to the composite key class
public class TransactionItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transactions transaction;

    @Id
    private int itemId;

    private int sellerId; // Needed for seller-level queries
    private int quantity;
    private BigDecimal priceAtPurchase;

    public TransactionItem() {}

    public TransactionItem(Transactions transaction, int itemId, int sellerId, int quantity, BigDecimal priceAtPurchase) {
        this.transaction = transaction;
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Transactions getTransaction() {
        return transaction;
    }

    public void setTransaction(Transactions transaction) {
        this.transaction = transaction;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    @Override
    public String toString() {
        return "TransactionItem{" +
                "transaction=" + transaction +
                ", itemId=" + itemId +
                ", sellerId=" + sellerId +
                ", quantity=" + quantity +
                ", priceAtPurchase=" + priceAtPurchase +
                '}';
    }

    // ID Class
    public static class TransactionItemId implements Serializable {
        private int transaction;
        private int itemId;

        public TransactionItemId() {}

        public TransactionItemId(int transaction, int itemId) {
            this.transaction = transaction;
            this.itemId = itemId;
        }

        // Getters and Setters
        public int getTransaction() {
            return transaction;
        }

        public void setTransaction(int transaction) {
            this.transaction = transaction;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        // Override equals and hashCode for proper comparison
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TransactionItemId that = (TransactionItemId) o;
            return transaction == that.transaction &&
                    itemId == that.itemId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(transaction, itemId);
        }
    }
}
