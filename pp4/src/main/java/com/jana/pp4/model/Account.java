package com.jana.pp4.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Component
public class Account {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accId")
    private int accId;
    public static Integer id_inc;
    private String Email;
    private String Name;
    //private String Password;
    private float Rating;
    private BigDecimal Balance;
    @OneToMany(mappedBy = "seller")
    private List<Item> item;
    @OneToOne(mappedBy = "CartOwner")
    private Cart cart;

    @OneToMany(mappedBy = "DepositOwner")
    private List<DepositLog> DepositLogs;

    @OneToMany(mappedBy = "LoginOwner")
    private List<LoginLog> LoginLogs;

    public Account(){}

    public Account(int accId, String email, String name,  float rating, BigDecimal balance, List<Item> item, Cart cart,  List<DepositLog> depositLogs, List<LoginLog> loginLogs) {
        this.accId = accId;
        Email = email;
        Name = name;
//        Password = password;
        Rating = rating;
        Balance = balance;
        this.item = item;
        this.cart = cart;
//        this.sellerTransactions = sellerTransactions;
        DepositLogs = depositLogs;
        LoginLogs = loginLogs;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accId=" + accId +
                ", Email='" + Email + '\'' +
                ", Name='" + Name + '\'' +
//                ", Password='" + Password + '\'' +
                ", Rating=" + Rating +
                ", Balance=" + Balance +
                ", item=" + item +
                ", cart=" + cart +
//                ", sellerTransactions=" + sellerTransactions +
                ", DepositLogs=" + DepositLogs +
                ", LoginLogs=" + LoginLogs +
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


    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    public List<Item> getItem() {
        return item;
    }
    public void setItem(List<Item> item) {
        this.item = item;
    }
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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

