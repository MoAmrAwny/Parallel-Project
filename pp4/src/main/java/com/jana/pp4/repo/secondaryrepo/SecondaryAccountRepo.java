package com.jana.pp4.repo.secondaryrepo;

import com.jana.pp4.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecondaryAccountRepo extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.DepositLogs WHERE a.accId = :id")
    Optional<Account> findAccountWithDepositLogsById(@Param("id") Integer id);
}
