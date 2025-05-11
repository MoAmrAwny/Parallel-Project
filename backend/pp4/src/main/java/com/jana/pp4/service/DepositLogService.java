package com.jana.pp4.service;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.DepositLog;
import com.jana.pp4.repo.primaryrepo.PrimaryDepLogRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryDepLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DepositLogService {

    @Autowired
    private PrimaryDepLogRepo primaryRepo;
    @Autowired
    private SecondaryDepLogRepo secondaryRepo;

    public DepositLog addDepositLog(BigDecimal num, Account acc) {
        if(acc.getAccId()%2==0){ //even
            DepositLog dl=new DepositLog(acc,num, new Date());
            secondaryRepo.save(dl);
            return dl;
        }
        else{ //odd
            DepositLog dl=new DepositLog(acc,num, new Date());
            primaryRepo.save(dl);
            return dl;
        }
    }
}
