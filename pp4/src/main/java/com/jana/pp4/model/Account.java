package com.jana.pp4.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Account {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accId")
    private int accId;
    private String Email;
    private String Name;
    private String Password;
    private float Rating;
    private float Balance;
    @OneToOne(mappedBy = "seller")
    private Item item;
    @OneToOne(mappedBy = "CartOwner")
    private Cart cart;

    @OneToMany(mappedBy = "seller")  // "seller" is the field name in the Transaction class
    private List<Transactions> sellerTransactions;

    // One-to-many relationship: an account can have many transactions as a buyer
    @OneToMany(mappedBy = "buyer")  // "buyer" is the field name in the Transaction class
    private List<Transactions> buyerTransactions;

    @OneToMany(mappedBy = "DepositOwner")
    private List<DepositLog> DepositLogs;

    @OneToMany(mappedBy = "LoginOwner")
    private List<LoginLog> LoginLogs;

    public Account(){}
    public Account(int accId, String email, String name, String password, float rating, float balance) {
        this.accId = accId;
        Email = email;
        Name = name;
        Password = password;
        Rating = rating;
        Balance = balance;
    }
    @Override
    public String toString() {
        return "Account{" +
                "accId=" + accId +
                ", Email='" + Email + '\'' +
                ", Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                ", Rating=" + Rating +
                ", Balance=" + Balance +
                '}';
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public float getBalance() {
        return Balance;
    }

    public void setBalance(float balance) {
        Balance = balance;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Transactions> getSellerTransactions() {
        return sellerTransactions;
    }

    public void setSellerTransactions(List<Transactions> sellerTransactions) {
        this.sellerTransactions = sellerTransactions;
    }

    public List<Transactions> getBuyerTransactions() {
        return buyerTransactions;
    }

    public void setBuyerTransactions(List<Transactions> buyerTransactions) {
        this.buyerTransactions = buyerTransactions;
    }

    public List<DepositLog> getDepositLogs() {
        return DepositLogs;
    }

    public void setDepositLogs(List<DepositLog> depositLogs) {
        DepositLogs = depositLogs;
    }

    public List<LoginLog> getLoginLogs() {
        return LoginLogs;
    }

    public void setLoginLogs(List<LoginLog> loginLogs) {
        LoginLogs = loginLogs;
    }
}

