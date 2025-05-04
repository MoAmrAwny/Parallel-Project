package com.jana.pp4.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Cart {

    @Id
    private int cart_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accId", referencedColumnName = "accId")
    private Account CartOwner;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItem;
    private BigDecimal TotalPrice;


    public Cart(){}

    public Cart(int cart_id, Account cartOwner, List<CartItem> cartItem, BigDecimal totalPrice) {
        this.cart_id = cart_id;
        CartOwner = cartOwner;
        this.cartItem = cartItem;
        TotalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cart_id=" + cart_id +
                ", CartOwner=" + CartOwner +
                ", cartItem=" + cartItem +
                ", TotalPrice=" + TotalPrice +
                '}';
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public Account getCartOwner() {
        return CartOwner;
    }

    public void setCartOwner(Account cartOwner) {
        CartOwner = cartOwner;
    }

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public BigDecimal getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        TotalPrice = totalPrice;
    }

    public void updateTotalPrice() {
        this.TotalPrice = cartItem.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))) // Calculate item total (price * quantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all item totals to get cart's total price
    }

}
