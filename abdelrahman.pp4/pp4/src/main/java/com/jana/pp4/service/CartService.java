package com.jana.pp4.service;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.Cart;
import com.jana.pp4.repo.primaryrepo.PrimaryCartRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryCartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    PrimaryCartRepo primaryRepo;
    @Autowired
    SecondaryCartRepo secondaryRepo;
    public Cart CreateCart(Account acc) {
        Cart cart;
        //gets max id of cart in each database
        Integer maxPrimaryId = primaryRepo.findMaxId();
        Integer maxSecondaryId = secondaryRepo.findMaxId();

        if (maxPrimaryId == null) maxPrimaryId = 0;
        if (maxSecondaryId == null) maxSecondaryId = 0;

        Cart.id_inc = Math.max(maxPrimaryId, maxSecondaryId) + 1;  //1
        if (acc.getAccId() % 2 == 0) {  //even secondary repo
            cart=new Cart(Cart.id_inc,acc,new ArrayList<>(), BigDecimal.ZERO);
            secondaryRepo.save(cart);
        } else {
            cart=new Cart(Cart.id_inc,acc,new ArrayList<>(), BigDecimal.ZERO);
            primaryRepo.save(cart);
        }
        return cart;
    }
}