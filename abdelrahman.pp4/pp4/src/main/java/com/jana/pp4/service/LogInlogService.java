package com.jana.pp4.service;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.LoginLog;
import com.jana.pp4.repo.primaryrepo.PrimaryLogInRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryLogInRepo;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.util.Collections.sort;

@Service
public class LogInlogService {


    @Autowired
    private PrimaryLogInRepo primaryLogInRepo;
    @Autowired
    private SecondaryLogInRepo secondaryLogInRepo;
    @Autowired
    private  AccountService accountService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public LogInLogDTO saveLoginLog(String email, String password ) {

        int id=accountService.AccountExists(email);

        AccountService.AccountDTO dbAccount =accountService.getAccountDtoById(id);

        LoginLog log;

        String DbPassword = dbAccount.getPassword();

        boolean status = encoder.matches(password, DbPassword);

        Account savedacc = new Account(id, dbAccount.getEmail(), dbAccount.getName(), 0,new BigDecimal(0));

        log = new LoginLog((int)(primaryLogInRepo.count()+secondaryLogInRepo.count()),savedacc, email, status, new Date());


        if(id%2==0){
            return new LogInLogDTO(secondaryLogInRepo.save(log));
        }
        else{
            return new LogInLogDTO(primaryLogInRepo.save(log));
        }

    }



    public List<LogInLogDTO> getLogInLogs (){
        List<LogInLogDTO> loglist = new ArrayList<LogInLogDTO>();
        primaryLogInRepo.findAll()
                .forEach(log -> loglist.add(new LogInLogDTO(log)));
        secondaryLogInRepo.findAll()
                .forEach(log -> loglist.add(new LogInLogDTO(log)));

        loglist.sort(Comparator.comparing(LogInLogDTO::getLogTime));
        return loglist;
    }



    public static class LogInLogDTO {
        private int logId;
        private Account LoginOwner;
        private String Email;
        private Boolean Success;
        private Date LogTime;



        public LogInLogDTO(LoginLog save) {
            this.logId= save.getLogId();;
            this.Email= save.getEmail();;
            this.LogTime=save.getLogTime();
            this.Success=save.getSuccess();
        }


        public int getLogId() {
            return logId;
        }

        public void setLogId(int logId) {
            this.logId = logId;
        }

        public Account getLoginOwner() {
            return LoginOwner;
        }

        public void setLoginOwner(Account loginOwner) {
            LoginOwner = loginOwner;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public Boolean getSuccess() {
            return Success;
        }

        public void setSuccess(Boolean success) {
            Success = success;
        }

        public Date getLogTime() {
            return LogTime;
        }

        public void setLogTime(Date logTime) {
            LogTime = logTime;
        }
    }


    }
