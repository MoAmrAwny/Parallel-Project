package com.jana.pp4.model;
import jakarta.persistence.*;

import java.util.Date;


@Entity
public class DepositLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @ManyToOne
    @JoinColumn(name = "acc_id", referencedColumnName = "accId")
    private Account DepositOwner;
    private float DepositedAmt;
    private Date LogTime;
    public DepositLog(){}

    public DepositLog(int logId, Account depositOwner, float depositedAmt, Date logTime) {
        this.logId = logId;
        DepositOwner = depositOwner;
        DepositedAmt = depositedAmt;
        LogTime = logTime;
    }


    @Override
    public String toString() {
        return "DepositLog{" +
                "logId=" + logId +
                ", DepositOwner=" + DepositOwner +
                ", DepositedAmt=" + DepositedAmt +
                ", LogTime=" + LogTime +
                '}';
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Account getDepositOwner() {
        return DepositOwner;
    }

    public void setDepositOwner(Account depositOwner) {
        DepositOwner = depositOwner;
    }

    public float getDepositedAmt() {
        return DepositedAmt;
    }

    public void setDepositedAmt(float depositedAmt) {
        DepositedAmt = depositedAmt;
    }

    public Date getLogTime() {
        return LogTime;
    }

    public void setLogTime(Date logTime) {
        LogTime = logTime;
    }
}

