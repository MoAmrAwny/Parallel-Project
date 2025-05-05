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
    private String email;
    private String name;
    private String password;
    private float rating;
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

    public Account(int accId, String email, String name,String password,  float rating, BigDecimal balance) {
        this.accId = accId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.rating = rating;
        Balance = balance;
    }
    public Account(int accId, String email, String name,  float rating, BigDecimal balance) {
        this.accId = accId;
        this.email = email;
        this.name = name;
        this.rating = rating;
        Balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accId=" + accId +
                ", Email='" + email + '\'' +
                ", Name='" + name + '\'' +
//                ", Password='" + Password + '\'' +
                ", Rating=" + rating +
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
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

