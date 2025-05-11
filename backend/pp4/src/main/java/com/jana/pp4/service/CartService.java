package com.jana.pp4.service;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.Cart;
import com.jana.pp4.repo.primaryrepo.PrimaryAccountRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryCartRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryAccountRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryCartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
@Service
public class CartService {
    @Autowired
    private PrimaryCartRepo primaryRepo;
    @Autowired
    private SecondaryCartRepo secondaryRepo;
    @Autowired
    private PrimaryAccountRepo primaryAccountRepo;
    @Autowired
    private SecondaryAccountRepo secondaryAccountRepo;

    @Transactional
    public Cart CreateCart(int accId) {
        // Get max IDs to generate new cart ID
        Integer maxPrimaryId = primaryRepo.findMaxId();
        Integer maxSecondaryId = secondaryRepo.findMaxId();
        int newCartId = Math.max(
                maxPrimaryId != null ? maxPrimaryId : 0,
                maxSecondaryId != null ? maxSecondaryId : 0
        ) + 1;

        // Get the account (should exist since we just saved it)
        Account account;
        if (accId % 2 == 0) {
            account = secondaryAccountRepo.findById(accId)
                    .orElseThrow(() -> new IllegalStateException("Account not found in secondary repository"));
        } else {
            account = primaryAccountRepo.findById(accId)
                    .orElseThrow(() -> new IllegalStateException("Account not found in primary repository"));
        }

        // Create new cart
        Cart cart = new Cart();
        cart.setCart_id(newCartId);
        cart.setCartOwner(account);  // This sets the relationship
        cart.setCartItem(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);

        // Save to appropriate repository
        if (accId % 2 == 0) {
            secondaryRepo.save(cart);
        } else {
            primaryRepo.save(cart);
        }

        return cart;
    }

    public int generateNewCartId() {
        Integer maxPrimaryId = primaryRepo.findMaxId();
        Integer maxSecondaryId = secondaryRepo.findMaxId();

        if (maxPrimaryId == null) maxPrimaryId = 0;
        if (maxSecondaryId == null) maxSecondaryId = 0;

        return Math.max(maxPrimaryId, maxSecondaryId) + 1;
    }
}