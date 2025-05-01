package com.jana.pp4.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accId", referencedColumnName = "accId")
    private Account CartOwner;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItem;
    private float TotalPrice;
    public Cart(){}

    public Cart(int cartId, Account cartOwner, List<CartItem> cartItem, float totalPrice) {
        this.cartId = cartId;
        CartOwner = cartOwner;
        this.cartItem = cartItem;
        TotalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", CartOwner=" + CartOwner +
                ", cartItem=" + cartItem +
                ", TotalPrice=" + TotalPrice +
                '}';
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        TotalPrice = totalPrice;
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
}
