package com.jana.pp4.service;

import com.jana.pp4.model.Item;
import com.jana.pp4.model.TransactionItem;
import com.jana.pp4.model.Transactions;
import com.jana.pp4.repo.primaryrepo.PrimaryItemRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryTransactionItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryTransactionItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionItemService {

    @Autowired
    private PrimaryTransactionItemRepo primaryRepo;
    @Autowired
    private SecondaryTransactionItemRepo secondaryRepo;
    @Autowired
    private PrimaryItemRepo primaryItemRepo;
    @Autowired
    private SecondaryItemRepo secondaryItemRepo;

    public List<SoldItemDTO> getSoldItems(Integer id) {
        List<TransactionItem> firstT = primaryRepo.findBySellerId(id);
        List<TransactionItem> secondT = secondaryRepo.findBySellerId(id);

        List<TransactionItem> allItems = new ArrayList<>();
        allItems.addAll(firstT);
        allItems.addAll(secondT);

        return allItems.stream()
                .map(item -> {
                    int itemId = item.getItemId();
                    Optional<Item> optionalItem = (id % 2 == 0)
                            ? secondaryItemRepo.findById(itemId)
                            : primaryItemRepo.findById(itemId);

                    Item fullItem = optionalItem.orElse(null); // Or throw if image is critical

                    return new SoldItemDTO(
                            itemId,
                            fullItem.getName(),
                            fullItem.getCategory(),
                            fullItem.getDescription(),
                            item.getQuantity(),
                            item.getPriceAtPurchase(),
                            item.getTransaction().getTranId(),
                            item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())),
                            fullItem.getImageName(),
                            fullItem.getImageType(),
                            fullItem.getImageData(),
                            fullItem.getCondition()
                    );
                }).collect(Collectors.toList());
    }


    public List<SellerTransactionDTO> getSellerTransaction(Integer id) {
        List<TransactionItem> firstT = primaryRepo.findBySellerId(id);
        List<TransactionItem> secondT = secondaryRepo.findBySellerId(id);

        List<TransactionItem> allItems = new ArrayList<>();
        allItems.addAll(firstT);
        allItems.addAll(secondT);

        List<SellerTransactionDTO> result = new ArrayList<>();
        for (TransactionItem item : allItems) {
            Transactions transaction = item.getTransaction(); // Assumes relationship is set

            // Add role info as "seller"
            SellerTransactionDTO dto = new SellerTransactionDTO(
                    transaction.getTranId(),
                    transaction.getBuyer().getAccId(), // Buyer ID (who the seller sold to)
                    item.getPriceAtPurchase(),
                    item.getQuantity(),
                    transaction.getTranTime(),
                    "seller" // role field added here
            );
            result.add(dto);
        }

        return result;

    }
    public static class SoldItemDTO {
        private int itemId;
        private int quantity;
        private BigDecimal price;
        private int transactionId;
        private BigDecimal total;
        private String Name;
        private String Description;
        private String Category;
        private String imageName;
        private String imageType;
        private byte[] imageData;
        private String Condition;

        public String getCondition() {
            return Condition;
        }

        public void setCondition(String condition) {
            Condition = condition;
        }


        public SoldItemDTO(int itemId,String Name,String Category,String Description, int quantity, BigDecimal price, int transactionId,
                           BigDecimal total, String imageName, String imageType, byte[] imageData , String Condition) {
            this.itemId = itemId;
            this.Name=Name;
            this.Category=Category;
            this.Description=Description;
            this.quantity = quantity;
            this.price = price;
            this.transactionId = transactionId;
            this.total = total;
            this.imageName = imageName;
            this.imageType = imageType;
            this.imageData = imageData;
            this.Condition = Condition;
        }

        public String getCategory() {
            return Category;
        }

        public String getName() {
            return Name;
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

        public String getImageName() {
            return imageName;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getImageType() {
            return imageType;
        }

        public byte[] getImageData() {
            return imageData;
        }
    }

    public class SellerTransactionDTO {
        private Integer transId;
        private Integer buyerId;
        private BigDecimal priceAtPurchase;
        private Integer quantity;
        private Date tranTime;
        private String role;
        private BigDecimal totalAmount; // Add this field for total amount

        // Constructor for Seller Role
        public SellerTransactionDTO(Integer transId,Integer buyerId, BigDecimal priceAtPurchase, Integer quantity, Date tranTime, String role) {
            this.buyerId = buyerId;
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

        public Integer getBuyerId() {
            return buyerId;
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
