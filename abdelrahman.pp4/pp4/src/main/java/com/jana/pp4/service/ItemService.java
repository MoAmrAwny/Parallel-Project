package com.jana.pp4.service;

import com.jana.pp4.model.Item;
import com.jana.pp4.model.TransactionItem;
import com.jana.pp4.repo.primaryrepo.PrimaryItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private PrimaryItemRepo primaryRepo;
    @Autowired
    private SecondaryItemRepo secondaryRepo;
    public List<ItemDTO> getItemById(Integer id) {
            List<Item> items=new ArrayList<>();
            if(id%2==0){
                items=secondaryRepo.findBySeller_AccIdAndAvailable(id,true);
            }
            else{
               items=primaryRepo.findBySeller_AccIdAndAvailable(id,true);
            }
        List<ItemDTO> itemDTOs = items.stream()
                .map(item -> new ItemDTO(
                        item.getItemId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getCategory(),
                        item.getAvailable(),
                        item.getCondition(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return itemDTOs;
    }
    public static class ItemDTO {

        private int itemId;
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
        private Boolean available;
        private String condition;
        private int quantity;

        // Constructor
        public ItemDTO(int itemId, String name, String description, BigDecimal price, String category, Boolean available, String condition, int quantity) {
            this.itemId = itemId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.available = available;
            this.condition = condition;
            this.quantity = quantity;
        }

        public int getItemId() {
            return itemId;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public String getCategory() {
            return category;
        }

        public Boolean getAvailable() {
            return available;
        }

        public String getCondition() {
            return condition;
        }

        public int getQuantity() {
            return quantity;
        }
    }

}
