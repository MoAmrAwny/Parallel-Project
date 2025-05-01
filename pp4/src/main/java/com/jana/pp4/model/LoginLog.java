package com.jana.pp4.model;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @ManyToOne
    @JoinColumn(name = "acc_id", referencedColumnName = "accId")
    private Account LoginOwner;
    private String Email;
    private Boolean Success;
    private Date LogTime;
    public LoginLog(){}

    public LoginLog(int logId, Account loginOwner, String email, Boolean success, Date logTime) {
        this.logId = logId;
        LoginOwner = loginOwner;
        Email = email;
        Success = success;
        LogTime = logTime;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "logId=" + logId +
                ", LoginOwner=" + LoginOwner +
                ", Email='" + Email + '\'' +
                ", Success=" + Success +
                ", LogTime=" + LogTime +
                '}';
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
