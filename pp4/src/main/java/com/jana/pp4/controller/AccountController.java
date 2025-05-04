package com.jana.pp4.controller;

import com.jana.pp4.dto.AccountRequest;
import com.jana.pp4.model.*;
import com.jana.pp4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.jana.pp4.dto.CartRequest;
import java.math.BigDecimal;
import java.util.List;
@Transactional
@RequestMapping("/account")
@RestController
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping("/{id}/balance/view")  //get balance by id of account
    public BigDecimal viewBalance(@PathVariable Integer id){
        return service.ViewBalance(id);
    }

    @PostMapping("/{id}/balance/add/{num}")
    public void depositAmount(@PathVariable BigDecimal num, @PathVariable Integer id){
        service.depositAmount(num,id);
    }

    @GetMapping("/{id}/deposit/view")
    public List<AccountService.DepositLogDto> viewDepositLog(@PathVariable Integer id) {
        return service.viewDepositLog(id);
    }

    @GetMapping("/{id}/items/purchased")
    public List<TransactionService.PurchasedItemDTO> viewPurchasedItems(@PathVariable Integer id){
        return service.viewPurchasedItems(id);
    }

    @GetMapping("/{id}/items/sold")
    public List<TransactionItemService.SoldItemDTO> viewSoldItems(@PathVariable Integer id){
        return service.viewSoldItems(id);
    }
    @GetMapping("/{id}/items/notsold")
    public List<ItemService.ItemDTO> ViewAvailableItems(@PathVariable Integer id){
        return service.viewNotSoldItems(id);
    }
    public class CartRequest {
        private Item item;
        private int quantity;

        // Getters and Setters
        public Item getItem() { return item; }
        public void setItem(Item item) { this.item = item; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    @PostMapping("/{accountId}/cart/add")
    public ResponseEntity<String> addToCart(@PathVariable Integer accountId, @RequestBody CartRequest request) {
        service.AddToCart(accountId, request.getItem(), request.getQuantity());
        return ResponseEntity.ok("Item added to cart successfully");
    }



//    @GetMapping("/{accountId}/cart")
//    public ResponseEntity<List<CartItem>> viewCartItems(@PathVariable Integer accountId) {
//        List<CartItem> cartItems = service.getCartItemsByAccountId(accountId);
//        return ResponseEntity.ok(cartItems);
//    }




}
