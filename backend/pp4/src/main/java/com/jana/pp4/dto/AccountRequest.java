package com.jana.pp4.dto;

public class AccountRequest {
    private int account_id;

    public AccountRequest(){}
    public AccountRequest(int account_id){
        this.account_id = account_id;
    }
    public int getAccount_id() {
        return this.account_id;
    }

    public void setAccount(int account_id) {
        this.account_id = account_id;
    }

}
