package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.DepositLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimaryDepLogRepo extends JpaRepository<DepositLog, Integer> {
}
