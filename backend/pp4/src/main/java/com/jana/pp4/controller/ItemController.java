package com.jana.pp4.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jana.pp4.model.Account;
import com.jana.pp4.service.ItemService;
import com.jana.pp4.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.jana.pp4.dto.ItemRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@RestController

@CrossOrigin(origins = "http://localhost:5173")

public class ItemController {
    @Autowired
    private ItemService itemService;



    @GetMapping("/items")
    public ResponseEntity<List<ItemService.ItemDTO_Get>> getItems() {
        return new ResponseEntity<>(itemService.getItems(), HttpStatus.OK);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemService.ItemDTO_Get> getOneItemById(@PathVariable int id) {
        if (itemService.getOneItemById(id) != null)
            return new ResponseEntity<>(itemService.getOneItemById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/items/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addItem(@PathVariable int id,@RequestPart("item") String itemJson,
                                     @RequestPart("image") MultipartFile image) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            ItemRequest itemRequest = mapper.readValue(itemJson, ItemRequest.class);
            // Create Item entity from DTO
            Item item = new Item();
            item.setName(itemRequest.getName());
            item.setDescription(itemRequest.getDescription());
            item.setPrice(itemRequest.getPrice());
            item.setCategory(itemRequest.getCategory());
            item.setAvailable(itemRequest.getAvailable());
            item.setCondition(itemRequest.getCondition());
            item.setQuantity(itemRequest.getQuantity());

            // Save item
            ItemService.ItemDTO_Get savedItem = itemService.addItem(id,item, image);
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



//    @GetMapping("/items/{id}/image")
//    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
//        Item item = itemService.getOneItemById(id);
//        byte[] image = item.getImageData();
//        if (image != null)
//            return ResponseEntity.ok().contentType(MediaType.valueOf(item.getImageType())).body(image);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @PutMapping("/items/{accid}/{id}")
    public ResponseEntity<String> updateItem(@PathVariable int accid,@PathVariable int id,
                                             @RequestPart("item") String itemJson,
                                             @RequestPart("image") MultipartFile image) throws IOException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            ItemRequest itemRequest = mapper.readValue(itemJson, ItemRequest.class);
            // Create Item entity from DTO
            Item item = new Item();
            item.setItemId(itemRequest.getItemId());
            item.setSeller(itemRequest.getSeller());
            item.setName(itemRequest.getName());
            item.setDescription(itemRequest.getDescription());
            item.setPrice(itemRequest.getPrice());
            item.setCategory(itemRequest.getCategory());
            item.setAvailable(itemRequest.getAvailable());
            item.setCondition(itemRequest.getCondition());
            item.setQuantity(itemRequest.getQuantity());
            // Save item
            ItemService.ItemDTO_Get savedItem = itemService.updateItem(accid,id,item, image);
            return new ResponseEntity<>("updated", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id) {
        ItemService.ItemDTO_Get item = itemService.getOneItemById(id);
        itemService.deleteItem(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);

    }

    @GetMapping("/items/search")
    public ResponseEntity<List<ItemService.ItemDTO_Get>> searchItems(@RequestParam("keyword") String keyword) {
        List<ItemService.ItemDTO_Get> items = itemService.searchItems(keyword);
        if (!items.isEmpty())
            return new ResponseEntity<>(items, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/items/search/category")
    public ResponseEntity<List<ItemService.ItemDTO_Get>> searchItemsByCategory(@RequestParam("category") String category) {
        List<ItemService.ItemDTO_Get> items = itemService.searchItemsByCategory(category);
        if (!items.isEmpty())
            return new ResponseEntity<>(items, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/items/search/availability")
    public ResponseEntity<List<ItemService.ItemDTO_Get>> searchItemsByAvailability(@RequestParam("availability") Boolean availability) {
        List<ItemService.ItemDTO_Get> items = itemService.searchItemsByAvailability(availability);
        if (!items.isEmpty())
            return new ResponseEntity<>(items, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/items/search/condition")
    public ResponseEntity<List<ItemService.ItemDTO_Get>> searchItemsByCondition(@RequestParam("condition") String condition) {
        List<ItemService.ItemDTO_Get> items = itemService.searchItemsByCondition(condition);
        if (!items.isEmpty())
            return new ResponseEntity<>(items, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/items/search/price")
    public ResponseEntity<List<ItemService.ItemDTO_Get>> searchItemsByPrice(@RequestParam("priceBefore") BigDecimal priceBefore, @RequestParam("priceAfter") BigDecimal priceAfter) {
        List<ItemService.ItemDTO_Get> items = itemService.searchItemsByPrice(priceBefore,priceAfter);
        if (!items.isEmpty())
            return new ResponseEntity<>(items, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}