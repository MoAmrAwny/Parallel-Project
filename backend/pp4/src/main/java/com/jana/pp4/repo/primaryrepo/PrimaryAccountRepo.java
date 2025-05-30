package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrimaryAccountRepo extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.DepositLogs WHERE a.accId = :id")
    Optional<Account> findAccountWithDepositLogsById(@Param("id") Integer id);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.LoginLogs WHERE a.accId = :id")
    Optional<Account> findAccountWithLoginLogsById(@Param("id") Integer id);
    Optional<Account> findByEmail(String email);


}
