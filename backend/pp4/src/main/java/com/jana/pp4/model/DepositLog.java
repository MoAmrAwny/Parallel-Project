package com.jana.pp4.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


@Entity
public class DepositLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @ManyToOne
    @JoinColumn(name = "acc_id", referencedColumnName = "accId")
    private Account DepositOwner;
    private BigDecimal DepositedAmt;
    private Date LogTime;
    public DepositLog(){}

    public DepositLog(Account depositOwner, BigDecimal depositedAmt, Date logTime) {
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

    public BigDecimal getDepositedAmt() {
        return DepositedAmt;
    }

    public void setDepositedAmt(BigDecimal depositedAmt) {
        DepositedAmt = depositedAmt;
    }

    public Date getLogTime() {
        return LogTime;
    }

    public void setLogTime(Date logTime) {
        LogTime = logTime;
    }
}

