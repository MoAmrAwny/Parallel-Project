package com.jana.pp4.service;

import com.jana.pp4.model.Item;
import com.jana.pp4.model.TransactionItem;
import com.jana.pp4.model.Transactions;
import com.jana.pp4.repo.primaryrepo.PrimaryItemRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryTransactionRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryTransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private PrimaryTransactionRepo primaryRepo;
    @Autowired
    private SecondaryTransactionRepo secondaryRepo;
    @Autowired
    private PrimaryItemRepo primaryItemRepo;
    @Autowired
    private SecondaryItemRepo secondaryItemRepo;
    public List<PurchasedItemDTO> getTransactionItems(Integer id) {
        List<Transactions> transactions = (id % 2 == 0)
                ? secondaryRepo.findAllByBuyer_AccId(id)
                : primaryRepo.findAllByBuyer_AccId(id);

        List<PurchasedItemDTO> items = new ArrayList<>();

        for (Transactions ts : transactions) {
            for (TransactionItem tsi : ts.getTransactionItems()) {
                Item item;
                if (tsi.getSellerId() % 2 == 0) {
                    item = secondaryItemRepo.findById(tsi.getItemId()).orElse(null);
                    System.out.println(item.getItemId());
                } else {
                    item = primaryItemRepo.findById(tsi.getItemId()).orElse(null);
                }

                if (item != null) {
                    items.add(new PurchasedItemDTO(
                            tsi.getItemId(),
                            tsi.getSellerId(),
                            tsi.getQuantity(),
                            tsi.getPriceAtPurchase(),
                            ts.getTotalAmount(),
                            item.getImageName(),
                            item.getImageType(),
                            item.getImageData()
                    ));
                }
            }
        }

        return items;
    }


    public List<BuyerTransactionDTO> getBuyerTransaction(Integer id) {
        List<Transactions> transactions = new ArrayList<>();

        // Fetch buyer transactions from the correct database
        if (id % 2 == 0) {
            transactions = secondaryRepo.findAllByBuyer_AccId(id);
        } else {
            transactions = primaryRepo.findAllByBuyer_AccId(id);
        }

        List<BuyerTransactionDTO> result = new ArrayList<>();
        for (Transactions tx : transactions) {
            for (TransactionItem item : tx.getTransactionItems()) {
                // Add role info as "buyer"
                BuyerTransactionDTO dto = new BuyerTransactionDTO(
                        tx.getTranId(),
                        item.getSellerId(), // Seller ID (who the buyer bought from)
                        item.getPriceAtPurchase(),
                        item.getQuantity(),
                        tx.getTranTime(),
                        "buyer" // role field added here
                );
                result.add(dto);
            }
        }

        return result;
    }

    //    public static class PurchasedItemDTO {
//        private int itemId;
//        private int sellerId;
//        private int quantity;
//        private BigDecimal priceAtPurchase;
//        private BigDecimal totalTransactionAmount; // Added
//        public PurchasedItemDTO(int itemId, int sellerId, int quantity, BigDecimal priceAtPurchase, BigDecimal totalTransactionAmount) {
//            this.itemId = itemId;
//            this.sellerId = sellerId;
//            this.quantity = quantity;
//            this.priceAtPurchase = priceAtPurchase;
//            this.totalTransactionAmount = totalTransactionAmount;
//        }
//        public int getItemId() {
//            return itemId;
//        }
//        public int getSellerId() {
//            return sellerId;
//        }
//        public int getQuantity() {
//            return quantity;
//        }
//        public BigDecimal getPriceAtPurchase() {
//            return priceAtPurchase;
//        }
//        public BigDecimal getTotalTransactionAmount() {
//            return totalTransactionAmount;
//        }
//        // Getters and setters
//    }
    public static class PurchasedItemDTO {
        private int itemId;
        private int sellerId;
        private int quantity;
        private BigDecimal priceAtPurchase;
        private BigDecimal totalTransactionAmount;

        private String imageName;
        private String imageType;
        private byte[] imageData;

        public PurchasedItemDTO(int itemId, int sellerId, int quantity, BigDecimal priceAtPurchase,
                                BigDecimal totalTransactionAmount, String imageName, String imageType, byte[] imageData) {
            this.itemId = itemId;
            this.sellerId = sellerId;
            this.quantity = quantity;
            this.priceAtPurchase = priceAtPurchase;
            this.totalTransactionAmount = totalTransactionAmount;
            this.imageName = imageName;
            this.imageType = imageType;
            this.imageData = imageData;
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

        public String getImageName() {
            return imageName;
        }

        public String getImageType() {
            return imageType;
        }

        public byte[] getImageData() {
            return imageData;
        }
    }


    public static class BuyerTransactionDTO {
        private Integer transId;
        private Integer sellerId;
        private BigDecimal priceAtPurchase;
        private Integer quantity;
        private Date tranTime;
        private String role;
        private BigDecimal totalAmount; // Add this field for total amount

        // Constructor for Buyer Role
        public BuyerTransactionDTO(Integer transId,Integer sellerId, BigDecimal priceAtPurchase, Integer quantity, Date tranTime, String role) {
            this.sellerId = sellerId;
            this.priceAtPurchase = priceAtPurchase;
            this.quantity = quantity;
            this.tranTime = tranTime;
            this.role = role;
            this.totalAmount = priceAtPurchase.multiply(new BigDecimal(quantity)); // Calculate total amount
            this.transId=transId;
        }

        public Integer getTransId() {
            return transId;
        }

        public Integer getSellerId() {
            return sellerId;
        }

        public BigDecimal getPriceAtPurchase() {
            return priceAtPurchase;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Date getTranTime() {
            return tranTime;
        }

        public String getRole() {
            return role;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }
    }


}