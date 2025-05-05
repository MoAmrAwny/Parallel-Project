package com.jana.pp4.repo.secondaryrepo;

import com.jana.pp4.model.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondaryTransactionItemRepo extends JpaRepository<TransactionItem, Integer> {
    List<TransactionItem> findBySellerId(int sellerId);

}
