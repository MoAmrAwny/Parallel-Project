package com.jana.pp4.service;

import com.jana.pp4.model.TransactionItem;
import com.jana.pp4.model.Transactions;
import com.jana.pp4.repo.primaryrepo.PrimaryTransactionRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryTransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private PrimaryTransactionRepo primaryRepo;
    @Autowired
    private SecondaryTransactionRepo secondaryRepo;
    public List<PurchasedItemDTO> getTransactionItems(Integer id) {
        List<Transactions>transactions;
        if(id%2==0){ //even
            transactions=secondaryRepo.findAllByBuyer_AccId(id);
        }
        else{
            transactions=primaryRepo.findAllByBuyer_AccId(id);
        }
        List<PurchasedItemDTO> items = new ArrayList<>();
        for(Transactions ts: transactions){
            for(TransactionItem tsi: ts.getTransactionItems()){
                items.add(new PurchasedItemDTO(
                        tsi.getItemId(),
                        tsi.getSellerId(),
                        tsi.getQuantity(),
                        tsi.getPriceAtPurchase(),
                        ts.getTotalAmount() // added here
                ));
            }
        }

        return items;
    }

    public static class PurchasedItemDTO {
        private int itemId;
        private int sellerId;
        private int quantity;
        private BigDecimal priceAtPurchase;
        private BigDecimal totalTransactionAmount; // Added

        public PurchasedItemDTO(int itemId, int sellerId, int quantity, BigDecimal priceAtPurchase, BigDecimal totalTransactionAmount) {
            this.itemId = itemId;
            this.sellerId = sellerId;
            this.quantity = quantity;
            this.priceAtPurchase = priceAtPurchase;
            this.totalTransactionAmount = totalTransactionAmount;
        }

        public int getItemId() {
            return itemId;
        }

        public int getSellerId() {
            return sellerId;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getPriceAtPurchase() {
            return priceAtPurchase;
        }

        public BigDecimal getTotalTransactionAmount() {
            return totalTransactionAmount;
        }
        // Getters and setters
    }
}
