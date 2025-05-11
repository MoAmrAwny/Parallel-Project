package com.jana.pp4.repo.secondaryrepo;

import com.jana.pp4.model.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondaryLogInRepo extends JpaRepository<LoginLog,Integer> {
}
