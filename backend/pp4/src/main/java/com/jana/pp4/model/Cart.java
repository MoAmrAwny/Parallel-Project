package com.jana.pp4.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Cart {

    @Id
    private int cart_id;

    public static int id_inc;

    @OneToOne(mappedBy = "cart")
    private Account cartOwner;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> cartItem;
    private BigDecimal TotalPrice;


    public Cart(){}

    public Cart(int cart_id, Account cartOwner, List<CartItem> cartItem, BigDecimal totalPrice) {
        this.cart_id = cart_id;
        cartOwner = cartOwner;
        this.cartItem = cartItem;
        TotalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cart_id=" + cart_id +
                ", CartOwner=" + cartOwner +
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
        return cartOwner;
    }

    public void setCartOwner(Account cartOwner) {
        cartOwner = cartOwner;
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

    public void updateTotalPrice(List<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        this.setTotalPrice(total);
    }



}
