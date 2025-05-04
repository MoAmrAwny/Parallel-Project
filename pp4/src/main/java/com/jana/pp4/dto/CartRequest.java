package com.jana.pp4.dto;

import com.jana.pp4.model.Item;

public class CartRequest {
    private Item item;
    private int quantity;

    public CartRequest() {}

    public CartRequest(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
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
}

