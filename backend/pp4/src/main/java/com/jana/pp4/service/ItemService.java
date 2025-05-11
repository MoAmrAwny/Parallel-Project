package com.jana.pp4.service;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.Item;
import com.jana.pp4.repo.primaryrepo.PrimaryAccountRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryItemRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryAccountRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryItemRepo;
import jakarta.persistence.Lob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private SecondaryAccountRepo secondaryAccountRepo ;
    @Autowired
    private PrimaryAccountRepo primaryAccountRepo ;



    public List<ItemDTO> getItemById(Integer id) {
        List<Item> items=new ArrayList<>();
        if(id%2==0){
            items=secondaryRepo.findBySeller_AccIdAndAvailable(id,true);
        }
        else{
            items=primaryRepo.findBySeller_AccIdAndAvailable(id,true);
        }
        List<ItemDTO> itemDTOs = items.stream()
                .filter(item -> item.getQuantity() > 0)
                .map(item -> new ItemDTO(
                        item.getItemId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getCategory(),
                        item.getAvailable(),
                        item.getCondition(),
                        item.getQuantity(),
                        item.getImageName(),
                        item.getImageType(),
                        item.getImageData()
                ))
                .collect(Collectors.toList());

        return itemDTOs;
    }
    public static class ItemDTO {

        private int itemId;

        public String getImageType() {
            return ImageType;
        }

        public void setImageType(String imageType) {
            ImageType = imageType;
        }

        public String getImageName() {
            return ImageName;
        }

        public void setImageName(String imageName) {
            ImageName = imageName;
        }

        public byte[] getImageData() {
            return ImageData;
        }

        public void setImageData(byte[] imageData) {
            ImageData = imageData;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        private String name;
        private String description;
        private BigDecimal price;
        private String category;
        private Boolean available;
        private String condition;
        private int quantity;
        private String ImageName;
        private String ImageType;
        @Lob
        private byte[] ImageData;


        public ItemDTO(int itemId, String name, String description, BigDecimal price, String category, Boolean available, String condition, int quantity, String imageName, String imageType, byte[] imageData) {
            this.itemId = itemId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.available = available;
            this.condition = condition;
            this.quantity = quantity;
            ImageName = imageName;
            ImageType = imageType;
            ImageData = imageData;
        }

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


    public ItemDTO_Get getOneItemById(int id) {
        Item item = secondaryRepo.findById(id).orElse(null);

        if (item == null) {
            item = primaryRepo.findById(id).orElse(null);
        }

        if (item != null) {
            // Using an instance just to access the convertToDTO method
            return new ItemDTO_Get(0, null, null, null, null, null, null, 0, null, null, null, 0)
                    .convertToDTO(item);
        } else {
            return null; // or throw new ItemNotFoundException(id);
        }
    }
//    public List<Item> getItems(){     //search all
//        List<Item> mylist =new ArrayList<>();
//        mylist.addAll(primaryRepo.findAll());
//        mylist.addAll(secondaryRepo.findAll());
//        return mylist;
//    }
@Transactional(readOnly = true)
public List<ItemDTO_Get> getItems() {
    List<Item> allItems = new ArrayList<>();
    allItems.addAll(primaryRepo.findAll());
    allItems.addAll(secondaryRepo.findAll());

    return allItems.stream().filter(item -> item.getQuantity() > 0)
            .map(item -> new ItemDTO_Get(
                    item.getItemId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getCategory(),
                    item.getAvailable(),
                    item.getCondition(),
                    item.getQuantity(),
                    item.getImageName(),
                    item.getImageType(),
                    item.getImageData(),
                    item.getSeller().getAccId() // This line can cause LazyInitializationException if session is closed
            ))
            .collect(Collectors.toList());
}


    public Account getAccountById(int id){
        return id % 2 == 0
                ? secondaryAccountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"))
                : primaryAccountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

    }
//    public Item addItem(int id,Item item, MultipartFile image) throws IOException {
//       if(item.getItemId()==0) {
//
//           Integer maxPrimaryId = primaryRepo.findMaxId();
//           Integer maxSecondaryId = secondaryRepo.findMaxId();
//
//           if (maxPrimaryId == null) maxPrimaryId = 0;
//           if (maxSecondaryId == null) maxSecondaryId = 0;
//
//           item.setItemId(Math.max(maxPrimaryId, maxSecondaryId) + 1);
//       }
//
//        item.setImageName(image.getOriginalFilename());
//        item.setImageType(image.getContentType());
//        item.setImageData(image.getBytes());
//        item.setSeller(getAccountById(id));
//        if(item.getSeller().getAccId()%2==0){
//            return secondaryRepo.save(item);
//        }else{
//            return primaryRepo.save(item);
//        }
//    }
public ItemDTO_Get addItem(int id, Item item, MultipartFile image) throws IOException {
    if (item.getItemId() == 0) {
        Integer maxPrimaryId = primaryRepo.findMaxId();
        Integer maxSecondaryId = secondaryRepo.findMaxId();

        if (maxPrimaryId == null) maxPrimaryId = 0;
        if (maxSecondaryId == null) maxSecondaryId = 0;

        item.setItemId(Math.max(maxPrimaryId, maxSecondaryId) + 1);
    }

    item.setImageName(image.getOriginalFilename());
    item.setImageType(image.getContentType());
    item.setImageData(image.getBytes());
    if(id %2==0){
        item.setSeller(secondaryAccountRepo.findById(id).get());
    }
    else{
        item.setSeller(primaryAccountRepo.findById(id).get());
    }

    Item savedItem;
    if (item.getSeller().getAccId() % 2 == 0) {
        savedItem = secondaryRepo.save(item);
    } else {
        savedItem = primaryRepo.save(item);
    }

    return new ItemDTO_Get(
            savedItem.getItemId(),
            savedItem.getName(),
            savedItem.getDescription(),
            savedItem.getPrice(),
            savedItem.getCategory(),
            savedItem.getAvailable(),
            savedItem.getCondition(),
            savedItem.getQuantity(),
            savedItem.getImageName(),
            savedItem.getImageType(),
            savedItem.getImageData(),
            savedItem.getSeller().getAccId()
    );
}

    public void deleteItem(int id){
        ItemDTO_Get item=getOneItemById(id);
        if(item.getSellerId()%2==0){
            secondaryRepo.deleteById(id);
        }else primaryRepo.deleteById(id);
    }
    public ItemDTO_Get updateItem(int accid, int id, Item item, MultipartFile image) throws IOException {
        deleteItem(id);
        item.setItemId(id);
        return addItem(accid,item,image);
    }
//    public List<Item> searchItemsByCategory(String category) {
//        List<Item> mylist =new ArrayList<>( );
//        mylist.addAll(primaryRepo.findByCategory(category));
//        mylist.addAll(secondaryRepo.findByCategory(category));
//        return mylist;
//    }
//    public List<Item> searchItemsByCategoryAndAvailability(String category, Boolean available) {
//        List<Item> mylist =new ArrayList<>( );
//        mylist.addAll(primaryRepo.findByCategoryAndAvailable(category,available));
//        mylist.addAll(secondaryRepo.findByCategoryAndAvailable(category,available));
//        return mylist;
//    }
//    public List<Item> searchItemsByAvailability(Boolean available) {
//        List<Item> mylist =new ArrayList<>( );
//        mylist.addAll(primaryRepo.findByAvailable(available));
//        mylist.addAll(secondaryRepo.findByAvailable(available));
//        return mylist;
//    }
//    public List<Item> searchItems(String keyword) {
//        List<Item> mylist =new ArrayList<>( );
//        mylist.addAll(primaryRepo.searchItems(keyword));
//        mylist.addAll(secondaryRepo.searchItems(keyword));
//        return mylist;
//    }
//    public List<Item> searchItemsByPrice(BigDecimal priceBefore, BigDecimal priceAfter) {
//        List<Item> mylist =new ArrayList<>( );
//        mylist.addAll(primaryRepo.findByPriceBetween(priceBefore,priceAfter));
//        mylist.addAll(secondaryRepo.findByPriceBetween(priceBefore,priceAfter));
//        return mylist;
//    }
//    public List<Item> searchItemsByCondition(String condition) {
//        List<Item> mylist =new ArrayList<>( );
//        mylist.addAll(primaryRepo.findByCondition(condition));
//        mylist.addAll(secondaryRepo.findByCondition(condition));
//        return mylist;
//    }
public List<ItemDTO_Get> searchItemsByCategory(String category) {
    List<ItemDTO_Get> result = new ArrayList<>();
    primaryRepo.findByCategory(category).forEach(item -> result.add(
            new ItemDTO_Get(
                    item.getItemId(), item.getName(), item.getDescription(),
                    item.getPrice(), item.getCategory(), item.getAvailable(),
                    item.getCondition(), item.getQuantity(),
                    item.getImageName(), item.getImageType(), item.getImageData(),
                    item.getSeller() != null ? item.getSeller().getAccId() : 0
            )));
    secondaryRepo.findByCategory(category).forEach(item -> result.add(
            new ItemDTO_Get(
                    item.getItemId(), item.getName(), item.getDescription(),
                    item.getPrice(), item.getCategory(), item.getAvailable(),
                    item.getCondition(), item.getQuantity(),
                    item.getImageName(), item.getImageType(), item.getImageData(),
                    item.getSeller() != null ? item.getSeller().getAccId() : 0
            )));
    return result;
}
    public List<ItemDTO_Get> searchItemsByCategoryAndAvailability(String category, Boolean available) {
        List<ItemDTO_Get> result = new ArrayList<>();
        primaryRepo.findByCategoryAndAvailable(category, available).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        secondaryRepo.findByCategoryAndAvailable(category, available).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        return result;
    }

    public List<ItemDTO_Get> searchItemsByAvailability(Boolean available) {
        List<ItemDTO_Get> result = new ArrayList<>();
        primaryRepo.findByAvailable(available).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        secondaryRepo.findByAvailable(available).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        return result;
    }

    public List<ItemDTO_Get> searchItems(String keyword) {
        List<ItemDTO_Get> result = new ArrayList<>();
        primaryRepo.searchItems(keyword).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        secondaryRepo.searchItems(keyword).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        return result;
    }

    public List<ItemDTO_Get> searchItemsByPrice(BigDecimal priceBefore, BigDecimal priceAfter) {
        List<ItemDTO_Get> result = new ArrayList<>();
        primaryRepo.findByPriceBetween(priceBefore, priceAfter).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        secondaryRepo.findByPriceBetween(priceBefore, priceAfter).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        return result;
    }

    public List<ItemDTO_Get> searchItemsByCondition(String condition) {
        List<ItemDTO_Get> result = new ArrayList<>();
        primaryRepo.findByCondition(condition).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        secondaryRepo.findByCondition(condition).forEach(item -> result.add(
                new ItemDTO_Get(item.getItemId(), item.getName(), item.getDescription(),
                        item.getPrice(), item.getCategory(), item.getAvailable(),
                        item.getCondition(), item.getQuantity(),
                        item.getImageName(), item.getImageType(), item.getImageData(),
                        item.getSeller() != null ? item.getSeller().getAccId() : 0)
        ));
        return result;
    }



    public static class ItemDTO_Get {
        private int itemId;
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
        private Boolean available;
        private String condition;
        private int quantity;
        private String imageName;
        private String imageType;
        private byte[] imageData;
        private int sellerId;

        public ItemDTO_Get(int itemId, String name, String description, BigDecimal price,
                           String category, Boolean available, String condition, int quantity,
                           String imageName, String imageType, byte[] imageData, int sellerId) {
            this.itemId = itemId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.available = available;
            this.condition = condition;
            this.quantity = quantity;
            this.imageName = imageName;
            this.imageType = imageType;
            this.imageData = imageData;
            this.sellerId = sellerId;
        }

        // Getters...
        public int getSellerId() { return sellerId; }

        // Getters for all fields
        public int getItemId() { return itemId; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public String getCategory() { return category; }
        public Boolean getAvailable() { return available; }
        public String getCondition() { return condition; }
        public int getQuantity() { return quantity; }
        public String getImageName() { return imageName; }
        public String getImageType() { return imageType; }
        public byte[] getImageData() { return imageData; }
        private ItemDTO_Get convertToDTO(Item item) {
            return new ItemDTO_Get(
                    item.getItemId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getCategory(),
                    item.getAvailable(),
                    item.getCondition(),
                    item.getQuantity(),
                    item.getImageName(),
                    item.getImageType(),
                    item.getImageData(),
                    item.getSeller().getAccId()// avoid NullPointerException
            );
        }
    }

}