package com.jana.pp4.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
//


@Entity
@IdClass(CartItem.CartItemId.class)
public class CartItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;

    private BigDecimal price;

    public CartItem() {}

    public CartItem(Cart cart, Item item, int quantity, BigDecimal price) {
        this.cart = cart;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
                ", item=" + item +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    // Composite Key Class
    public static class CartItemId implements Serializable {
        private Long cart;  // must match Cart's ID type
        private Long item;  // must match Item's ID type

        public CartItemId() {}

        public CartItemId(Long cart, Long item) {
            this.cart = cart;
            this.item = item;
        }

        public Long getCart() {
            return cart;
        }

        public void setCart(Long cart) {
            this.cart = cart;
        }

        public Long getItem() {
            return item;
        }

        public void setItem(Long item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CartItemId that = (CartItemId) o;
            return Objects.equals(cart, that.cart) &&
                    Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cart, item);
        }
    }
}

