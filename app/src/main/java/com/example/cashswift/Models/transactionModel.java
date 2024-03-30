package com.example.cashswift.Models;

public class transactionModel {
    String timestamp,transactionId,description,amount,fromUser,toUser,received;

    transactionModel(){

    }

    public transactionModel(String timestamp, String transactionId, String description, String amount, String fromUser, String toUser, String received) {
        this.timestamp = timestamp;
        this.transactionId = transactionId;
        this.description = description;
        this.amount = amount;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.received = received;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }
}
