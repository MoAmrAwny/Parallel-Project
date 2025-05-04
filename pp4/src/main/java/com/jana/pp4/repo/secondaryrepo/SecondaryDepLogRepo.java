package com.jana.pp4.repo.secondaryrepo;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.DepositLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondaryDepLogRepo extends JpaRepository<DepositLog, Integer> {

}
