package com.example.cashswift.Models;

public class usersModel {
    String name,upi_pass,email,number,userId,upiId;
    Double Balance;

    usersModel(){

    }

    public usersModel(String name, String username, String email, String number, String userId, String upiId, Double balance) {
        this.name = name;
        this.upi_pass = username;
        this.email = email;
        this.number = number;
        this.userId = userId;
        this.upiId = upiId;
        Balance = balance;
    }

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        Balance = balance;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpi_pass() {
        return upi_pass;
    }

    public void setUpi_pass(String upi_pass) {
        this.upi_pass = upi_pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
