package com.jana.pp4.service;

import com.jana.pp4.model.TransactionItem;
import com.jana.pp4.repo.primaryrepo.PrimaryTransactionItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryTransactionItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionItemService {

    @Autowired
    private PrimaryTransactionItemRepo primaryRepo;
    @Autowired
    private SecondaryTransactionItemRepo secondaryRepo;

    public List<SoldItemDTO> getSoldItems(Integer id) {
        List<TransactionItem> firstT = primaryRepo.findBySellerId(id);
        List<TransactionItem> secondT = secondaryRepo.findBySellerId(id);

        List<TransactionItem> allItems = new ArrayList<>();
        allItems.addAll(firstT);
        allItems.addAll(secondT);

        return allItems.stream().map(item ->
                new SoldItemDTO(
                        item.getItemId(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getTransaction().getTranId(),
                        item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()))
                )
        ).collect(Collectors.toList());
    }


    public static class SoldItemDTO {
        private int itemId;
        private int quantity;
        private BigDecimal price;
        private int transactionId;
        private BigDecimal total;

        public SoldItemDTO(int itemId, int quantity, BigDecimal price, int transactionId, BigDecimal total) {
            this.itemId = itemId;
            this.quantity = quantity;
            this.price = price;
            this.transactionId = transactionId;
            this.total = total;
        }

        public int getItemId() {
            return itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public int getTransactionId() {
            return transactionId;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }
}

