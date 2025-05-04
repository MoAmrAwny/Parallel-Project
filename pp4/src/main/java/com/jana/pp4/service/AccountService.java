package com.jana.pp4.service;

import com.jana.pp4.model.*;
import com.jana.pp4.repo.primaryrepo.PrimaryAccountRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private PrimaryAccountRepo primaryRepo;
    @Autowired
    private SecondaryAccountRepo secondaryRepo;
    @Autowired
    private DepositLogService depositService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionItemService transactionItemService;
    @Autowired
    private ItemService itemService;

    public BigDecimal ViewBalance(Integer id) {
        if(id%2==0){  //even
            Account acc=secondaryRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            return acc.getBalance();
        }
        else{
            Account acc=primaryRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            return acc.getBalance();
        }
    }
    public void depositAmount(BigDecimal num, Integer id) {
        if(id%2==0){
            Account acc=secondaryRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            acc.setBalance(acc.getBalance().add(num));
            secondaryRepo.save(acc);
            depositService.addDepositLog(num,acc);
        }
        else{
            Account acc=primaryRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            acc.setBalance(acc.getBalance().add(num));
            primaryRepo.save(acc);
            depositService.addDepositLog(num,acc);
        }
    }
    @Transactional(readOnly = true)
    public List<DepositLogDto> viewDepositLog(Integer id) {
        // 1. Get the account with deposit logs loaded
        Account account = (id % 2 == 0)
                ? secondaryRepo.findAccountWithDepositLogsById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"))
                : primaryRepo.findAccountWithDepositLogsById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        // 2. Convert to DTOs
        return account.getDepositLogs().stream()
                .map(log -> new DepositLogDto(log.getDepositedAmt(), log.getLogTime()))
                .collect(Collectors.toList());
    }
    public List<TransactionService.PurchasedItemDTO> viewPurchasedItems(Integer id) {
        return transactionService.getTransactionItems(id);
    }
    public List<TransactionItemService.SoldItemDTO> viewSoldItems(Integer id) {
        return transactionItemService.getSoldItems(id);
    }
    public List<ItemService.ItemDTO> viewNotSoldItems(Integer id) {
        return itemService.getItemById(id);
    }

    public static class DepositLogDto {
        private BigDecimal depositedAmt;
        private Date logTime;
        public DepositLogDto(BigDecimal depositedAmt, Date logTime) {
            this.depositedAmt = depositedAmt;
            this.logTime = logTime;
        }
        // Getters
        public BigDecimal getDepositedAmt() { return depositedAmt; }
        public Date getLogTime() { return logTime; }
    }



    public void AddToCart(Integer accountId, Item item, int quantity) {
        Account acc = (accountId % 2 == 0)
                ? secondaryRepo.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"))
                : primaryRepo.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

        if (acc.getCart() == null) {
            acc.setCart(new Cart());
        }

        List<CartItem> cartItems = acc.getCart().getCartItem();
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        boolean itemFound = false;
        for (CartItem existingItem : cartItems) {
            if (existingItem.getItem().equals(item)) {

                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                itemFound = true;
                break;
            }
        }

        // If item was not found in cart, create new cart item
        if (!itemFound) {
            CartItem cartItem = new CartItem(acc.getCart(), item, quantity, item.getPrice());
            cartItems.add(cartItem);
        }

        acc.getCart().setCartItem(cartItems);

        if (accountId % 2 == 0) {
            secondaryRepo.save(acc);
        } else {
            primaryRepo.save(acc);
        }
    }
    public void completeOrder(Account buyer, Account seller) {
        Cart cart = buyer.getCart();
        BigDecimal totalFees = BigDecimal.ZERO;

        if (cart != null && cart.getCartItem() != null) {
            List<Item> newItems = new ArrayList<>();

            for (CartItem cartItem : cart.getCartItem()) {
                Item item = cartItem.getItem();
                newItems.add(item);
                // Updated to multiply price by quantity
                totalFees = totalFees.add(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }

            // Check buyer has enough balance
            if (buyer.getBalance().compareTo(totalFees) < 0) {
                throw new RuntimeException("Insufficient balance for transfer.");
            }

            // Transfer money
            buyer.setBalance(buyer.getBalance().subtract(totalFees));
            seller.setBalance(seller.getBalance().add(totalFees));

            // Transfer items to buyer
            buyer.getItem().addAll(newItems);

            cart.getCartItem().clear();

        } else {
            throw new RuntimeException("Cart is empty.");
        }
    }

//    public List<CartItem> getCartItemsByAccountId(Integer accountId) {
//        Account account = (accountId % 2 == 0)
//                ? secondaryRepo.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"))
//                : primaryRepo.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
//
//        Cart cart = account.getCart();
//        if (cart != null && cart.getCartItem() != null) {
//            return cart.getCartItem();
//        } else {
//            return new ArrayList<>();
//        }
//    }



}
