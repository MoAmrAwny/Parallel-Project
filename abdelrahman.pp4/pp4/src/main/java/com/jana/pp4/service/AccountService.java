package com.jana.pp4.service;

import com.jana.pp4.model.*;
import com.jana.pp4.repo.primaryrepo.PrimaryAccountRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryCartRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryAccountRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryCartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private PrimaryAccountRepo primaryAccountRepo;
    @Autowired
    private SecondaryAccountRepo secondaryAccountRepo;
    @Autowired
    private DepositLogService depositService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionItemService transactionItemService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;



    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public BigDecimal ViewBalance(Integer id) {
        if(id%2==0){  //even
            Account acc= secondaryAccountRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            return acc.getBalance();
        }
        else{
            Account acc= primaryAccountRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            return acc.getBalance();
        }
    }
    public void depositAmount(BigDecimal num, Integer id) {
        if(id%2==0){
            Account acc= secondaryAccountRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            acc.setBalance(acc.getBalance().add(num));
            secondaryAccountRepo.save(acc);
            depositService.addDepositLog(num,acc);
        }
        else{
            Account acc= primaryAccountRepo.findById(id).
                    orElseThrow(() -> new RuntimeException("Account not found"));
            acc.setBalance(acc.getBalance().add(num));
            primaryAccountRepo.save(acc);
            depositService.addDepositLog(num,acc);
        }
    }
    @Transactional(readOnly = true)
    public List<DepositLogDto> viewDepositLog(Integer id) {
        // 1. Get the account with deposit logs loaded
        Account account = (id % 2 == 0)
                ? secondaryAccountRepo.findAccountWithDepositLogsById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"))
                : primaryAccountRepo.findAccountWithDepositLogsById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        // 2. Convert to DTOs
        return account.getDepositLogs().stream()
                .map(log -> new DepositLogDto(log.getDepositedAmt(), log.getLogTime()))
                .collect(Collectors.toList());
    }
    public List<TransactionService.PurchasedItemDTO> viewPurchasedItems(Integer id) {
        return transactionService.getTransactionItems(id);
    }
    public List<TransactionItemService.SoldItemDTO> viewSoldItems(Integer id) {
        return transactionItemService.getSoldItems(id);
    }
    public List<ItemService.ItemDTO> viewNotSoldItems(Integer id) {
        return itemService.getItemById(id);
    }


    public Account saveAccount (String email,String password,String name){

            if(name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Account name cannot be empty");
        }
        if(email == null || email.trim().isEmpty())
        {
            throw new IllegalArgumentException("Account Email cannot be empty");
        }
        if(password == null || password.trim().isEmpty())
        {
            throw new IllegalArgumentException("Account Password cannot be empty");
        }
        if (primaryAccountRepo.findByEmail(email).isPresent() || secondaryAccountRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
        Account account=new Account((int)(primaryAccountRepo.count()+ secondaryAccountRepo.count()+1),
                email,
                name,
                encoder.encode(password),
                0,
                BigDecimal.valueOf(0));



        account.setCart(cartService.CreateCart(account));
        if (account.getAccId() % 2 == 0) {
            secondaryAccountRepo.save(account);
        } else {
            primaryAccountRepo.save(account);
        }
        return account;
    }

    public int AccountExists (String email){
        Optional<Account> acc1 = primaryAccountRepo.findByEmail(email);
        Optional<Account> acc2 = secondaryAccountRepo.findByEmail(email);
        if(acc1.isPresent()) {
            return acc1.get().getAccId();
        } else if (acc2.isPresent()) {
            return acc2.get().getAccId();
        }else {
            throw new IllegalArgumentException("Email doesn't exist");
        }
    }
    public Account getAccountById(int accId) {
        if(accId%2==0)
            return secondaryAccountRepo.getReferenceById(accId);
        else
            return primaryAccountRepo.getReferenceById(accId);
    }

    @Transactional
    public AccountDTO getAccountDtoById(Integer id) {
        Optional <Account> acc= primaryAccountRepo.findById(id);
        if(acc.isEmpty()) {
            acc = secondaryAccountRepo.findById(id);
        }
        if(acc.isEmpty()) {
            throw new IllegalArgumentException("Account doesn't exist");
        }
        return new AccountDTO(acc.get().getAccId(),acc.get().getEmail(),acc.get().getName(),acc.get().getPassword());
    }





    public static class AccountDTO {
        private Integer id;
        private String email;
        private String name;
        private String password; // Only include if absolutely needed for login

        // Constructors
        public AccountDTO() {}

        public AccountDTO(Integer id, String email, String name, String password) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.password = password;
        }

        // Static factory method
        public static AccountDTO fromEntity(Account account) {
            return new AccountDTO(
                    account.getAccId(),
                    account.getEmail(),
                    account.getName(),
                    account.getPassword() // Get password while session is active
            );
        }

        // Getters and setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    public static class DepositLogDto {
        private BigDecimal depositedAmt;
        private Date logTime;
        public DepositLogDto(BigDecimal depositedAmt, Date logTime) {
            this.depositedAmt = depositedAmt;
            this.logTime = logTime;
        }
        // Getters
        public BigDecimal getDepositedAmt() { return depositedAmt; }
        public Date getLogTime() { return logTime; }
    }

}
