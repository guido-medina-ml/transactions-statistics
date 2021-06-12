package com.n26.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private final double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private final Date timestamp;

    public Transaction(double amount, Date timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
