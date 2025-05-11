package com.jana.pp4.service;

import com.jana.pp4.model.Cart;
import com.jana.pp4.model.CartItem;
import com.jana.pp4.model.Item;
import com.jana.pp4.repo.primaryrepo.PrimaryAccountRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryCartItemRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryAccountRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryCartItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    PrimaryCartItemRepo primaryRepo;
    @Autowired
    SecondaryCartItemRepo secondaryRepo;
    @Autowired
    SecondaryItemRepo secondaryItemRepo;
    @Autowired
    PrimaryItemRepo primaryItemRepo;

    @Autowired
    SecondaryAccountRepo secondaryAccountRepo;
    @Autowired
    PrimaryAccountRepo primaryAccountRepo;


//    public CartItem createCartItem(Integer accId, Item item, int quantity, Cart cart) {
//        // 1. Check if item already exists in cart
//        Optional<CartItem> existingItem = cart.getCartItem().stream()
//                .filter(ci -> ci.getItem().getItemId() == item.getItemId())  // Using == since IDs are int
//                .findFirst();
//
//        if (existingItem.isPresent()) {
//            // Update quantity if exists
//            CartItem ci = existingItem.get();
//            ci.setQuantity(ci.getQuantity() + quantity);
//            ci.setPrice(item.getPrice()); // Update price in case it changed
//            cart.updateTotalPrice(); // Update cart total
//            return ci;
//        }
//
//        // 2. Create new CartItem
//        CartItem cartItem = new CartItem();
//        cartItem.setCart(cart);  // This sets one part of the composite key
//        cartItem.setItem(item);  // This sets the other part of the composite key
//        cartItem.setQuantity(quantity);
//        cartItem.setPrice(item.getPrice());
//
//        // 3. Add to cart's collection
//        cart.getCartItem().add(cartItem);
//        cart.updateTotalPrice(); // Update cart total
//
//        // 4. Save through cart owner (let cascade handle it)
//        if (accId % 2 == 0) {
//            secondaryAccountRepo.save(cart.getCartOwner());
//        } else {
//            primaryAccountRepo.save(cart.getCartOwner());
//        }
//
//        return cartItem;
//    }
}
