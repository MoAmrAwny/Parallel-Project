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
    private float Rating;
    private BigDecimal Balance;
    @OneToMany(mappedBy = "seller")
    private List<Item> item;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id") // The foreign key column in Account pointing to Cart
    private Cart cart;

    @OneToMany(mappedBy = "DepositOwner")
    private List<DepositLog> DepositLogs;

    @OneToMany(mappedBy = "LoginOwner")
    private List<LoginLog> LoginLogs;

    public Account(){}

    public Account(int accId, String email, String name, String password, float rating, BigDecimal balance, List<Item> item, Cart cart, List<DepositLog> depositLogs, List<LoginLog> loginLogs) {
        this.accId = accId;
        this.email = email;
        this.name = name;
        this.password = password;
        Rating = rating;
        Balance = balance;
        this.item = item;
        this.cart = cart;
        DepositLogs = depositLogs;
        LoginLogs = loginLogs;
    }

    public Account(int accId, String email, String name,String password,  float rating, BigDecimal balance) {
        this.accId = accId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.Rating = rating;
        Balance = balance;
    }
    public Account(int accId, String email, String name,  float rating, BigDecimal balance) {
        this.accId = accId;
        this.email = email;
        this.name = name;
        this.Rating = rating;
        Balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accId=" + accId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", Rating=" + Rating +
                ", Balance=" + Balance +
                ", item=" + item +
                ", cart=" + cart +
                ", DepositLogs=" + DepositLogs +
                ", LoginLogs=" + LoginLogs +
                '}';
    }

    public static Integer getId_inc() {
        return id_inc;
    }

    public static void setId_inc(Integer id_inc) {
        Account.id_inc = id_inc;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
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

