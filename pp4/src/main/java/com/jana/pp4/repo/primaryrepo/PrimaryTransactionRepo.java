package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.DepositLog;
import com.jana.pp4.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimaryTransactionRepo extends JpaRepository<Transactions, Integer> {
    List<Transactions> findAllByBuyer_AccId(Integer accId);
}
