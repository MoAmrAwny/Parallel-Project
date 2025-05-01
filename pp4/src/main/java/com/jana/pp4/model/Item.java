package com.jana.pp4.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", referencedColumnName = "accId")
    private Account seller; //foreign key
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Boolean available;
    private String condition;
    private int quantity;

    private String ImageName;
    private String ImageType;
    private byte[] ImageData;


    @ManyToMany(mappedBy = "items")
    private List<Transactions> transactions;

    @OneToOne(mappedBy = "cartItem")
    private CartItem cartItem;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Item() {
    }

    public Item(int itemId, Account seller, String name, String description, BigDecimal price, String category, Boolean available, String condition, int quantity, String imageName, String imageType, byte[] imageData, List<Transactions> transactions, CartItem cartItem) {
        this.itemId = itemId;
        this.seller = seller;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
        this.condition = condition;
        this.quantity = quantity;
        ImageName = imageName;
        ImageType = imageType;
        ImageData = imageData;
        this.transactions = transactions;
        this.cartItem = cartItem;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", seller=" + seller +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", available=" + available +
                ", condition='" + condition + '\'' +
                ", quantity=" + quantity +
                ", ImageName='" + ImageName + '\'' +
                ", ImageType='" + ImageType + '\'' +
                ", ImageData=" + Arrays.toString(ImageData) +
                ", transactions=" + transactions +
                ", cartItem=" + cartItem +
                '}';
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

//    public int getSellerId() {
//        return sellerId;
//    }
//
//    public void setSellerId(int sellerId) {
//        this.sellerId = sellerId;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Account getSeller() {
        return seller;
    }

    public void setSeller(Account seller) {
        this.seller = seller;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImageType() {
        return ImageType;
    }

    public void setImageType(String imageType) {
        ImageType = imageType;
    }

    public byte[] getImageData() {
        return ImageData;
    }

    public void setImageData(byte[] imageData) {
        ImageData = imageData;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}