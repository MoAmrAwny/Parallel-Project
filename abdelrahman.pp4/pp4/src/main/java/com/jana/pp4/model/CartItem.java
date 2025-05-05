package com.jana.pp4.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@IdClass(CartItem.CartItemId.class)  // Link the entity to the composite key class
public class CartItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;  // This is the cart object
    @Id
    @Column(name = "item_id")
    private int item_id;  // Store these item ids in transaction items
    private int quantity;
    private BigDecimal price;

    public CartItem() {
    }

    public CartItem(Cart cart, int item_id, int quantity, BigDecimal price) {
        this.cart = cart;
        this.item_id = item_id;
        this.quantity = quantity;
        this.price = price;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cart=" + cart +
                ", item_id=" + item_id +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    // ID Class for composite primary key
    public static class CartItemId implements Serializable {
        private int cart;  // matches Cart's id (accId)
        private int item_id; // matches item_id in CartItem

        public CartItemId() {}

        public CartItemId(int cartId, int item_id) {
            this.cart = cartId;
            this.item_id = item_id;
        }

        // Getters and Setters

        public int getCart() {
            return cart;
        }

        public void setCart(int cart) {
            this.cart = cart;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        // Override equals and hashCode for proper comparison
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CartItemId that = (CartItemId) o;
            return cart == that.cart && item_id == that.item_id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(cart, item_id);
        }
    }
}
