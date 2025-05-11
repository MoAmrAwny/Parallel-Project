package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrimaryLogInRepo extends JpaRepository<LoginLog, Integer> {
}
