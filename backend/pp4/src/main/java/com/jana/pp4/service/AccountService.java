package com.jana.pp4.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jana.pp4.model.*;
import com.jana.pp4.repo.primaryrepo.*;
import com.jana.pp4.repo.secondaryrepo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
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
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private PrimaryItemRepo primaryItemRepo;
    @Autowired
    private SecondaryItemRepo secondaryItemRepo;

    @Autowired
    private PrimaryCartRepo primaryCartRepo;
    @Autowired
    private SecondaryCartRepo secondaryCartRepo;
    @Autowired
    private PrimaryCartItemRepo primaryCartItemRepo;
    @Autowired
    private SecondaryCartItemRepo secondaryCartItemRepo;
    @Autowired
    private PrimaryTransactionRepo primaryTransactionRepo;
    @Autowired
    private SecondaryTransactionRepo secondaryTransactionRepo;
    @Autowired
    private SecondaryTransactionItemRepo secondaryTransactionItemRepo;
    @Autowired
    private PrimaryTransactionItemRepo primaryTransactionItemRepo;

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

    //---------------------------------------------------------------------------------------
    //awny we ganna

    public void AddToCart(Integer accountId, Integer itemId, int quantity) {
        if (accountId % 2 == 0) {
            addToSecondaryCart(accountId, itemId, quantity);
        } else {
            addToPrimaryCart(accountId, itemId, quantity);
        }
    }

    @Transactional("transactionManager")    // primary TM
    public void addToPrimaryCart(Integer accountId, Integer itemId, int quantity) {
        // 1) Try primary first
        Optional<Item> opt = primaryItemRepo.findById(itemId);
        boolean copied = false;
        // 2) Fallback to secondary
        if (opt.isEmpty()) {
            Item fromOther = secondaryItemRepo.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found anywhere"));

            // 3) Copy into primary
            Item copy = new Item();
            copy.setName(fromOther.getName());
            copy.setPrice(fromOther.getPrice());
            copy.setQuantity(fromOther.getQuantity());
            copy.setItemId(fromOther.getItemId());
//            copy = primaryItemRepo.save(copy);

            opt = Optional.of(copy);
            copied = true;
        }

        Item item = opt.get();
        if(quantity > item.getQuantity())
            throw new RuntimeException("sorry, we don't have this quantity in stock");


        Cart cart = primaryCartRepo.findByCartOwner_AccId(accountId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Optional<CartItem> existingOpt =
                primaryCartItemRepo.findByCartIdAndItemId(cart.getCart_id(), itemId);

        if (existingOpt.isPresent()) {
            // 2) Merge quantities
            CartItem existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + quantity);
            if(existing.getQuantity() > item.getQuantity())
                throw new RuntimeException("sorry, we don't have this quantity in stock");
            primaryCartItemRepo.save(existing);
        } else {
            // 3) Create new
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setItemId(item.getItemId());
            newItem.setPrice(item.getPrice());
            newItem.setQuantity(quantity);
            primaryCartItemRepo.save(newItem);
        }


        List<CartItem> cartItems = primaryCartItemRepo.findByCartId(cart.getCart_id());
        cart.updateTotalPrice(cartItems);
        primaryCartRepo.save(cart);
    }

    @Transactional("bookTransactionManager")
    public void addToSecondaryCart(Integer accountId, Integer itemId, int quantity) {
        // 1) Try to load from secondary
        Optional<Item> opt = secondaryItemRepo.findById(itemId);

        // 2) If not in secondary, fall back to primary
        if (opt.isEmpty()) {
            Item fromOther = primaryItemRepo.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found in either database"));

            // 3) Copy into secondary
            Item copy = new Item();
            copy.setName(fromOther.getName());
            copy.setPrice(fromOther.getPrice());
            copy.setQuantity(fromOther.getQuantity());
            copy.setItemId(fromOther.getItemId());
//            copy = secondaryItemRepo.save(copy);

            opt = Optional.of(copy);

        }

        Item item = opt.get();
        if(quantity > item.getQuantity())
            throw new RuntimeException("sorry, we don't have this quantity in stock");


        Cart cart = secondaryCartRepo.findByCartOwner_AccId(accountId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Optional<CartItem> existingOpt =
                secondaryCartItemRepo.findByCartIdAndItemId(cart.getCart_id(), itemId);

        if (existingOpt.isPresent()) {
            // 2) Merge quantities
            CartItem existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + quantity);
            if(existing.getQuantity() > item.getQuantity())
                throw new RuntimeException("sorry, we don't have this quantity in stock");
            secondaryCartItemRepo.save(existing);
        } else {
            // 3) Create new
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setItemId(item.getItemId());
            newItem.setPrice(item.getPrice());
            newItem.setQuantity(quantity);
            secondaryCartItemRepo.save(newItem);
        }

        // 4) Recalculate total & save cart
        List<CartItem> cartItems = secondaryCartItemRepo.findByCartId(cart.getCart_id());
        cart.updateTotalPrice(cartItems);
        secondaryCartRepo.save(cart);
    }


    public void ClearCart(Integer accountId) {
        if (accountId % 2 == 0) {
            Cart cart = secondaryCartRepo.findByCartOwner_AccId(accountId).orElseThrow(() -> new RuntimeException("Cart not found ya bro"));
            Clear_secondarycart(cart);
        } else {
            Cart cart = primaryCartRepo.findByCartOwner_AccId(accountId).orElseThrow(() -> new RuntimeException("Cart not found ya bro"));
            Clear_primarycart(cart);
        }
    }


    @Transactional("transactionManager")
    public void Clear_primarycart(Cart my_cart){
        primaryCartItemRepo.deleteByCartId(my_cart.getCart_id());
        my_cart.setCartItem(primaryCartItemRepo.findByCartId(my_cart.getCart_id()));
        my_cart.updateTotalPrice(my_cart.getCartItem());
        primaryCartRepo.save(my_cart);
    }
    @Transactional("bookTransactionManager")
    public void Clear_secondarycart(Cart my_cart){
        secondaryCartItemRepo.deleteByCartId(my_cart.getCart_id());
        my_cart.setCartItem(secondaryCartItemRepo.findByCartId(my_cart.getCart_id()));
        my_cart.updateTotalPrice(my_cart.getCartItem());
        secondaryCartRepo.save(my_cart);

    }
    public void saveTransactionItems(Integer buyerId,Integer sellerId, List<Item> items, List<Integer> quantityItems) {

        Account buyer = (buyerId % 2 == 0)
                ? secondaryAccountRepo.findById(buyerId).orElseThrow(() -> new RuntimeException("Account not found"))
                : primaryAccountRepo.findById(buyerId).orElseThrow(() -> new RuntimeException("Account not found"));

        Transactions transaction = new Transactions();
        int trans_id = (int)(primaryTransactionRepo.count()+secondaryTransactionRepo.count()+1);
        transaction.setTranId(trans_id);
        transaction.setBuyer(buyer);
        transaction.setTranTime(new Date());


        if (buyerId % 2 == 0) {
            secondaryTransactionRepo.save(transaction);
        } else {
            primaryTransactionRepo.save(transaction);
        }


        List<TransactionItem> transactionItems = new ArrayList<>();
        int n=items.size();
        for (int i = 0; i < n; i++) {
            Item item = items.get(i);
            Integer quantity = quantityItems.get(i);

            TransactionItem transactionItem = new TransactionItem(item.getItemId());
//            transactionItem.setItemId(item.getItemId());
            transactionItem.setQuantity(quantity);
            transactionItem.setPriceAtPurchase(item.getPrice());

            // Set the transaction reference
            transactionItem.setTransaction(transaction);
            transactionItem.setSellerId(sellerId);
            transactionItems.add(transactionItem);
            if (buyerId % 2 == 0) {
                secondaryTransactionItemRepo.save(transactionItem);
            } else {
                primaryTransactionItemRepo.save(transactionItem);
            }

        }
        transaction.setTransactionItems(transactionItems);
        transaction.updateTotalAmount();

        if (buyerId % 2 == 0) {
            secondaryTransactionRepo.save(transaction);
        } else {
            primaryTransactionRepo.save(transaction);
        }
    }
    public void completeOrder(int buyerId) {
        // Get buyer account from the appropriate repository based on ID parity
        Account buyer = getAccountById(buyerId);

        // Get cart and validate it's not empty
        Cart cart = buyer.getCart();
        if (cart == null || cart.getCartItem() == null || cart.getCartItem().isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }

        // Group cart items by seller
        Map<Integer, List<CartItem>> itemsBySeller = new HashMap<>();

        // Validate all items first to ensure quantities are available
        for (CartItem cartItem : cart.getCartItem()) {
            Integer itemId = cartItem.getItemId();

            // Find the item in repositories
            Optional<Item> itemOpt = primaryItemRepo.findById(itemId);
            if (itemOpt.isEmpty()) {
                itemOpt = secondaryItemRepo.findById(itemId);
            }

            if (itemOpt.isEmpty()) {
                throw new RuntimeException("Item doesn't exist");
            }

            Item item = itemOpt.get();

            // Check if we have enough quantity available
            if (item.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient quantity available for item: " +
                        item.getName() + ". Available: " + item.getQuantity() +
                        ", Requested: " + cartItem.getQuantity());
            }

            // Group by seller ID
            int sellerId = item.getSeller().getAccId(); // Assuming Item has a seller field
            if (!itemsBySeller.containsKey(sellerId)) {
                itemsBySeller.put(sellerId, new ArrayList<>());
            }
            itemsBySeller.get(sellerId).add(cartItem);
        }

        // Calculate total fees for validation
        BigDecimal totalFees = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItem()) {
            totalFees = totalFees.add(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }

        // Check if buyer has enough balance
        if (buyer.getBalance().compareTo(totalFees) < 0) {
            throw new RuntimeException("Insufficient balance for transfer.");
        }

        // Process each seller's items
        for (Map.Entry<Integer, List<CartItem>> entry : itemsBySeller.entrySet()) {
            int sellerId = entry.getKey();
            List<CartItem> sellerItems = entry.getValue();

            // Get seller account from the appropriate repository based on ID parity
            Account seller = getAccountById(sellerId);

            // Calculate seller's total
            BigDecimal sellerTotal = BigDecimal.ZERO;
            List<Item> itemsToUpdate = new ArrayList<>();
            List<Integer> itemQuantities = new ArrayList<>();

            for (CartItem cartItem : sellerItems) {
                sellerTotal = sellerTotal.add(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));

                // Find the item to update
                Optional<Item> itemOpt = primaryItemRepo.findById(cartItem.getItemId());
                if (itemOpt.isEmpty()) {
                    itemOpt = secondaryItemRepo.findById(cartItem.getItemId());
                }

                Item item = itemOpt.get(); // Safe because we checked earlier
                itemsToUpdate.add(item);
                itemQuantities.add(cartItem.getQuantity());
            }

            // Transfer money
            buyer.setBalance(buyer.getBalance().subtract(sellerTotal));
            seller.setBalance(seller.getBalance().add(sellerTotal));

            // Save seller account
            if (sellerId % 2 == 0) {
                secondaryAccountRepo.save(seller);
            } else {
                primaryAccountRepo.save(seller);
            }

            // Update item quantities
            for (int i = 0; i < itemsToUpdate.size(); i++) {
                Item item = itemsToUpdate.get(i);
                int newQuantity = item.getQuantity() - itemQuantities.get(i);

                // This check is redundant now as we validated earlier, but keeping for safety
                if (newQuantity < 0) {
                    throw new RuntimeException("Cannot fulfill order: negative inventory for item " +
                            item.getName());
                }

                item.setQuantity(newQuantity);

                if (sellerId % 2 == 0) {
                    secondaryItemRepo.save(item);
                } else {
                    primaryItemRepo.save(item);
                }
            }

            // Save transaction items for this seller
            saveTransactionItems(buyerId, sellerId, itemsToUpdate, itemQuantities);
        }

        // Save buyer account once after all deductions
        if (buyerId % 2 == 0) {
            secondaryAccountRepo.save(buyer);
        } else {
            primaryAccountRepo.save(buyer);
        }

        // Clear cart
        ClearCart(buyerId);
    }
    public Account getAccountById(int id){
        return id % 2 == 0
                ? secondaryAccountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"))
                : primaryAccountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

    }
    public List<CartItem> getCartItemsByAccountId(Integer accountId) {
        Account account = (accountId % 2 == 0)
                ? secondaryAccountRepo.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"))
                : primaryAccountRepo.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

        Cart cart = account.getCart();
        if (cart != null && cart.getCartItem() != null) {
            return cart.getCartItem();
        } else {
            return new ArrayList<>();
        }
    }
    //awny we ganna
    //----------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    //abdelrahman part

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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


        if (account.getAccId() % 2 == 0) {
            secondaryAccountRepo.save(account);
        } else {
            primaryAccountRepo.save(account);
        }
        Cart mycart = new Cart();
        mycart.setCart_id(account.getAccId());
        mycart.setCartOwner(account);
        mycart.setTotalPrice(BigDecimal.ZERO);


        account.setCart(mycart);
        if (account.getAccId() % 2 == 0) {
            secondaryAccountRepo.save(account);
        } else {
            primaryAccountRepo.save(account);
        }
        return account;
    }

    public int AccountExists (String email){
//        Optional<Account> acc1 = primaryAccountRepo.findByEmail(email);
//        Optional<Account> acc2 = secondaryAccountRepo.findByEmail(email);
//        if(acc1.isPresent()) {
//            return acc1.get().getAccId();
//        }
//        else if(acc2.isPresent()) {
//            return acc2.get().getAccId();
//        }
//        else {
//            throw new IllegalArgumentException("Email doesn't exist");
//        }
        Optional<Account> acc1 = secondaryAccountRepo.findByEmail(email);
        Optional<Account> acc2 = Optional.empty();

        if (acc1.isEmpty()) {
            acc2 = primaryAccountRepo.findByEmail(email);
        }

        if (acc1.isPresent()) {
            return acc1.get().getAccId();
        } else if (acc2.isPresent()) {
            return acc2.get().getAccId();
        } else {
            throw new IllegalArgumentException("Email doesn't exist");
        }


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

// abdelrahman part
//---------------------------------------------------------------------------------------


//---------------------------------------------------------------------------------------
// jana part

    public List<UnifiedTransactionDTO> viewTransactionHistory(Integer id) {
        // Fetch buyer and seller transactions
        List<TransactionService.BuyerTransactionDTO> buyerTransactions = transactionService.getBuyerTransaction(id);
        List<TransactionItemService.SellerTransactionDTO> sellerTransactions = transactionItemService.getSellerTransaction(id);


        Map<Integer, UnifiedTransactionDTO> groupedByTxId = new HashMap<>();

        for (TransactionService.BuyerTransactionDTO buyer : buyerTransactions) {
            BigDecimal total = buyer.getPriceAtPurchase().multiply(BigDecimal.valueOf(buyer.getQuantity()));
            groupedByTxId.merge(
                    buyer.getTransId(),
                    new UnifiedTransactionDTO(
                            buyer.getSellerId(),
                            buyer.getTranTime(), // convert LocalDateTime to Date
                            "buyer",
                            total
                    ),
                    (oldVal, newVal) -> {
                        oldVal.setTotalAmount(oldVal.getTotalAmount().add(newVal.getTotalAmount()));
                        return oldVal;
                    }
            );
        }

        for (TransactionItemService.SellerTransactionDTO seller : sellerTransactions) {
            BigDecimal total = seller.getPriceAtPurchase().multiply(BigDecimal.valueOf(seller.getQuantity()));
            groupedByTxId.merge(
                    seller.getTransId(),
                    new UnifiedTransactionDTO(
                            seller.getBuyerId(),
                            seller.getTranTime(), // convert LocalDateTime to Date
                            "seller",
                            total
                    ),
                    (oldVal, newVal) -> {
                        oldVal.setTotalAmount(oldVal.getTotalAmount().add(newVal.getTotalAmount()));
                        return oldVal;
                    }
            );
        }

        List<UnifiedTransactionDTO> result = new ArrayList<>(groupedByTxId.values());
        result.sort((a, b) -> b.getTranTime().compareTo(a.getTranTime()));
        return result;

    }
    public class UnifiedTransactionDTO {
        private Integer otherAccountId;
        private Date tranTime;
        private String role;
        private BigDecimal totalAmount;

        // Constructor for Unified DTO
        public UnifiedTransactionDTO(Integer otherAccountId, Date tranTime, String role, BigDecimal totalAmount) {
            this.otherAccountId = otherAccountId;
            this.tranTime = tranTime;
            this.role = role;
            this.totalAmount = totalAmount;
        }

        public Integer getOtherAccountId() {
            return otherAccountId;
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

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

// jana part

//---------------------------------------------------------------------------------------
public static class CartDTO {
    private int cartId;
    private String ownerUsername;
    private List<CartItemDTO> items;
    private BigDecimal totalPrice;

    public CartDTO() {}

    public CartDTO(int cartId, String ownerUsername, List<CartItemDTO> items, BigDecimal totalPrice) {
        this.cartId = cartId;
        this.ownerUsername = ownerUsername;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
    @Transactional(readOnly = true)
    public CartDTO viewCart(Integer accountId) {
        boolean useSecondaryCart = (accountId % 2 == 0);
        Cart cart = useSecondaryCart
                ? secondaryCartRepo.findByCartOwner_AccId(accountId)
                .orElseThrow(() -> new RuntimeException("Cart not found for account: " + accountId))
                : primaryCartRepo.findByCartOwner_AccId(accountId)
                .orElseThrow(() -> new RuntimeException("Cart not found for account: " + accountId));

        List<CartItemDTO> itemDTOs = cart.getCartItem().stream()
                .map(ci -> {
                    Integer itemId = ci.getItemId();
                    // Try fetching from secondary, then primary if not found
                    Optional<com.jana.pp4.model.Item> itemOpt = secondaryItemRepo.findById(itemId);
                    if (itemOpt.isEmpty()) {
                        itemOpt = primaryItemRepo.findById(itemId);
                    }
                    var item = itemOpt.orElseThrow(() ->
                            new RuntimeException("Item not found for id: " + itemId));

                    ItemService.ItemDTO_Get dto = new ItemService.ItemDTO_Get(
                            item.getItemId(), item.getName(), item.getDescription(),
                            item.getPrice(), item.getCategory(), item.getAvailable(),
                            item.getCondition(), item.getQuantity(), item.getImageName(),
                            item.getImageType(), item.getImageData(),
                            item.getSeller().getAccId());
                    return new CartItemDTO(dto, ci.getQuantity());
                })
                .collect(Collectors.toList());

        BigDecimal total = itemDTOs.stream()
                .map(CartItemDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String username = cart.getCartOwner().getName();
        return new CartDTO(cart.getCart_id(), username, itemDTOs, total);
    }
    public static class CartItemDTO {
        private ItemService.ItemDTO_Get item;
        private int quantity;
        private BigDecimal price;
        private BigDecimal total;

        public CartItemDTO() {}

        public CartItemDTO(ItemService.ItemDTO_Get item, int quantity) {
            this.item = item;
            this.quantity = quantity;
            this.price = item.getPrice();
            this.total = this.price.multiply(BigDecimal.valueOf(quantity));
        }

        public ItemService.ItemDTO_Get getItem() {
            return item;
        }

        public void setItem(ItemService.ItemDTO_Get item) {
            this.item = item;
            this.price = item.getPrice();
            this.total = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
            this.total = this.price.multiply(BigDecimal.valueOf(quantity));
        }

        public BigDecimal getPrice() {
            return price;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }


    public void removeItemFromCart(Integer accId, Integer itemId) {
        // 1) find the cart
        Optional<Cart> optCart = (accId % 2 == 0)
                ? secondaryCartRepo.findByCartOwner_AccId(accId)
                : primaryCartRepo.findByCartOwner_AccId(accId);

        if (!optCart.isPresent()) {
            throw new RuntimeException("Cart not found ");
        }
        Cart cart = optCart.get();
        Integer cartId = cart.getCart_id();

        // 2) delete the CartItem row in the DB
        if (accId % 2 == 0) {
            Optional<CartItem> cart_item = secondaryCartItemRepo.findByCartIdAndItemId(cartId,itemId);
            if(cart_item.isEmpty())
                throw new RuntimeException("this item is not in your cart");
            secondaryCartItemRepo.deleteByCartIdAndItemId(cartId, itemId);
        } else {
            Optional<CartItem> cart_item = primaryCartItemRepo.findByCartIdAndItemId(cartId,itemId);
            if(cart_item.isEmpty())
                throw new RuntimeException("this item is not in your cart");
            primaryCartItemRepo.deleteByCartIdAndItemId(cartId, itemId);
        }

        // 3) remove it from the in-memory list and recalc total
        List<CartItem> updatedItems = cart.getCartItem()
                .stream()
                .filter(ci -> !ci.getItemId().equals(itemId))
                .collect(Collectors.toList());
        cart.setCartItem(updatedItems);

        BigDecimal newTotal = updatedItems.stream()
                .map(ci -> ci.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(newTotal);

        // 4) save the cart back
        if (accId % 2 == 0) {
            secondaryCartRepo.save(cart);
        } else {
            primaryCartRepo.save(cart);
        }
    }



}

