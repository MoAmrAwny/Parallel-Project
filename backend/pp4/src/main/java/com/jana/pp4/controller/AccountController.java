package com.jana.pp4.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jana.pp4.model.*;
import com.jana.pp4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@Transactional
@RequestMapping("/account")
@RestController

@CrossOrigin(origins = "http://localhost:5173")

public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private LogInlogService logInlogService;

    @GetMapping("/{id}/balance/view")  //get balance by id of account
    public BigDecimal viewBalance(@PathVariable Integer id){
        return accountService.ViewBalance(id);
    }

    @PostMapping("/{id}/balance/add/{num}")
    public void depositAmount(@PathVariable BigDecimal num, @PathVariable Integer id){
        accountService.depositAmount(num,id);
    }

    @GetMapping("/{id}/deposit/view")
    public List<AccountService.DepositLogDto> viewDepositLog(@PathVariable Integer id) {
        return accountService.viewDepositLog(id);
    }

    @GetMapping("/{id}/items/purchased")
    public List<TransactionService.PurchasedItemDTO> viewPurchasedItems(@PathVariable Integer id){
        return accountService.viewPurchasedItems(id);
    }

    @GetMapping("/{id}/items/sold")
    public List<TransactionItemService.SoldItemDTO> viewSoldItems(@PathVariable Integer id){
        return accountService.viewSoldItems(id);
    }
    @GetMapping("/{id}/items/notsold")
    public List<ItemService.ItemDTO> ViewAvailableItems(@PathVariable Integer id){
        return accountService.viewNotSoldItems(id);
    }
    @GetMapping("/{id}/transaction/view")
    public List<AccountService.UnifiedTransactionDTO> ViewTransactionHistory(@PathVariable Integer id){
        return accountService.viewTransactionHistory(id);
    }





//---------------------------------------------------------------------------------------

// abdelrahman part


    @Transactional
    @PostMapping("/SignUp")
    public ResponseEntity<Account> createAccount (@RequestBody JsonNode jsonNode){
        String email = jsonNode.get("email").asText();
        String password=jsonNode.get("password").asText();
        String name=jsonNode.get("name").asText();
        Account savedAcc= accountService.saveAccount(email,password,name);
        return new ResponseEntity<>(savedAcc, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/LogIn")
    public ResponseEntity<LogInlogService.LogInLogDTO> saveLoginAttempt(@RequestBody JsonNode jsonNode) {
        String email = jsonNode.get("email").asText();
        String password=jsonNode.get("password").asText();
        LogInlogService.LogInLogDTO savedLog = logInlogService.saveLoginLog(email, password);
        if(savedLog != null){
        return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/{id}/LogInReport")
    public List<LogInlogService.LogInLogDTO> ViewAvailableLogInLogs(@PathVariable Integer id){
        return logInlogService.getLogInLogs(id);
    }
    @GetMapping ("/{accountId}/cart/view")
    AccountService.CartDTO ViewCart(@PathVariable Integer accountId){
        return accountService.viewCart(accountId);
    }

// abdelrahman part
//---------------------------------------------------------------------------------------

    //awny we ganna


    @PostMapping("/{accountId}/cart/add")
    public ResponseEntity<String> addToCart(
            @PathVariable Integer accountId,
            @RequestBody JsonNode jsonNode) {

        Integer id       = jsonNode.get("item_id").asInt();
        int     quantity = jsonNode.get("quantity").asInt();

        accountService.AddToCart(accountId, id, quantity);
        return ResponseEntity.ok("Item added to cart successfully");
    }
    @PostMapping("/{accountId}/cart/clear")
    public ResponseEntity<String> ClearCart(@PathVariable Integer accountId) {

        accountService.ClearCart(accountId);
        return ResponseEntity.ok("Cart cleared successfully<3");
    }
    @PostMapping("/completeOrder")
    public ResponseEntity<ApiResponse> completeOrder(@RequestBody BuyerOrderRequest request) {
        try {
            // Validate request
            if (request.buyerId() == null) {
                throw new IllegalArgumentException("BuyerId is required");
            }

            // Complete the order
            accountService.completeOrder(request.buyerId());

            // Return success response
            return ResponseEntity.ok(
                    new ApiResponse(
                            "success",
                            "Order completed successfully",
                            new BuyerOrderResponse(request.buyerId())
                    )
            );

        } catch (Exception e) {
            // Return error response
            return ResponseEntity.badRequest().body(
                    new ApiResponse("error", e.getMessage(), null)
            );
        }
    }

    // Updated DTO Classes
    public record BuyerOrderRequest(Integer buyerId) {}

    public record BuyerOrderResponse(Integer buyerId) {}

// Keep original DTOs for backward compatibility if needed
// public record OrderRequest(Integer buyerId, Integer sellerId) {}
// public record OrderResponse(Integer buyerId, Integer sellerId) {}

    public record ApiResponse(
            String status,
            String message,
            Object data
    ) {}

    // removeItemFromCart
    @PostMapping("/{accountId}/cart/remove")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Integer accountId,
                                                     @RequestBody JsonNode jsonNode) {
        Integer item_id       = jsonNode.get("item_id").asInt();

        accountService.removeItemFromCart(accountId,item_id);
        return ResponseEntity.ok("item removed from cart successfully<3");
    }





    //awny we ganna
//---------------------------------------------------------------------------------------
    public static class CartRequest {
        private Item item;
        private int quantity;

        // Getters and Setters
        public Item getItem() { return item; }
        public void setItem(Item item) { this.item = item; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

}
