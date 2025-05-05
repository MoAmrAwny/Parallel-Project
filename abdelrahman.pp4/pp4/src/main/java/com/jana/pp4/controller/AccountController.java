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
    //-----------------------------------------------------------------------------------------------
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
        return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
    }
    @GetMapping("/LogInReport")
    public List<LogInlogService.LogInLogDTO> ViewAvailableLogInLogs(){
        return logInlogService.getLogInLogs();
    }

}
