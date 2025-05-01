package com.jana.pp4.model;
import jakarta.persistence.*;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ManyToOne
    @JoinColumn(name = "cartId", referencedColumnName = "cartId")
    private Cart cart;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_item_id", referencedColumnName = "itemId")
    private Item cartItem;
    private int Quantity;

    public CartItem(){}

    public CartItem(Cart cart, Item cartItem, int quantity) {
        this.cart = cart;
        this.cartItem = cartItem;
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cart=" + cart +
                ", cartItem=" + cartItem +
                ", Quantity=" + Quantity +
                '}';
    }

    public Item getCartItem() {
        return cartItem;
    }

    public void setCartItem(Item cartItem) {
        this.cartItem = cartItem;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

