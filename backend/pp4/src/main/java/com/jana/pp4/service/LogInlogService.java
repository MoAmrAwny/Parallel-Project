package com.jana.pp4.service;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.LoginLog;
import com.jana.pp4.repo.primaryrepo.PrimaryAccountRepo;
import com.jana.pp4.repo.primaryrepo.PrimaryLogInRepo;
import com.jana.pp4.repo.secondaryrepo.SecondaryAccountRepo;
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
import java.util.stream.Collectors;

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
    @Autowired
    private PrimaryAccountRepo primaryAccountRepo;
    @Autowired
    private SecondaryAccountRepo secondaryAccountRepo;


    public LogInLogDTO saveLoginLog(String email, String password ) {
        int id;
        try{

        id =accountService.AccountExists(email);
}catch(IllegalArgumentException ex){
            return null;
        }

        AccountService.AccountDTO dbAccount =accountService.getAccountDtoById(id);

        LoginLog log;

        String DbPassword = dbAccount.getPassword();

        boolean status = encoder.matches(password, DbPassword);
        Account savedacc;
        if(id%2==0){
            savedacc = secondaryAccountRepo.findById(id).get();
        }
        else{
            savedacc = primaryAccountRepo.findById(id).get();
        }

        //log = new LoginLog((int)(primaryLogInRepo.count()+secondaryLogInRepo.count()+1),savedacc, email, status, new Date());
        log=new LoginLog();
        log.setLogId((int)(primaryLogInRepo.count()+secondaryLogInRepo.count()+1));
        log.setLoginOwner(savedacc);
        log.setLogTime(new Date());
        log.setEmail(email);
        log.setSuccess(status);

        if(id%2==0){
            return new LogInLogDTO(secondaryLogInRepo.save(log));
        }
        else{
            return new LogInLogDTO(primaryLogInRepo.save(log));
        }




    }



    public List<LogInLogDTO> getLogInLogs (Integer id){
            Account account = (id % 2 == 0)
                    ? secondaryAccountRepo.findAccountWithLoginLogsById(id)
                    .orElseThrow(() -> new RuntimeException("Account not found"))
                    : primaryAccountRepo.findAccountWithLoginLogsById(id)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getLoginLogs().stream()
                .map(LogInLogDTO::new)
                .sorted(Comparator.comparing(LogInLogDTO::getLogTime))
                .collect(Collectors.toList());
    }

    public static class LogInLogDTO {
        private int logId;
        private int accId;
        private String email;
        private Boolean success;
        private Date logTime;
        private String name;

        public LogInLogDTO(LoginLog save) {
            this.logId = save.getLogId();
            this.email = save.getEmail();
            this.logTime = save.getLogTime();
            this.success = save.getSuccess();
            this.accId = save.getLoginOwner().getAccId(); // assuming getAccountId() exists
            this.name = save.getLoginOwner().getName();

        }

        public int getLogId() {
            return logId;
        }

        public void setLogId(int logId) {
            this.logId = logId;
        }

        public int getAccId() {
            return accId;
        }

        public void setAccId(int accId) {
            this.accId = accId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Date getLogTime() {
            return logTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLogTime(Date logTime) {
            this.logTime = logTime;
        }
    }

//    public static class LogInLogDTO {
//        private int logId;
//        private Account LoginOwner;
//        private String Email;
//        private Boolean Success;
//        private Date LogTime;
//
//
//
//        public LogInLogDTO(LoginLog save) {
//            this.logId= save.getLogId();;
//            this.Email= save.getEmail();;
//            this.LogTime=save.getLogTime();
//            this.Success=save.getSuccess();
//        }
//
//
//        public int getLogId() {
//            return logId;
//        }
//
//        public void setLogId(int logId) {
//            this.logId = logId;
//        }
//
//        public Account getLoginOwner() {
//            return LoginOwner;
//        }
//
//        public void setLoginOwner(Account loginOwner) {
//            LoginOwner = loginOwner;
//        }
//
//        public String getEmail() {
//            return Email;
//        }
//
//        public void setEmail(String email) {
//            Email = email;
//        }
//
//        public Boolean getSuccess() {
//            return Success;
//        }
//
//        public void setSuccess(Boolean success) {
//            Success = success;
//        }
//
//        public Date getLogTime() {
//            return LogTime;
//        }
//
//        public void setLogTime(Date logTime) {
//            LogTime = logTime;
//        }
//    }


}
