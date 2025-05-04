package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.TransactionItem;
import com.jana.pp4.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimaryTransactionItemRepo extends JpaRepository<TransactionItem, Integer> {
    List<TransactionItem> findBySellerId(int sellerId);

}
