package com.n26.service;

import com.n26.domain.Statistics;
import com.n26.domain.Transaction;

import java.util.Date;

public interface TransactionService {
    boolean isValidTime(Date timestamp);

    void addTransaction(Transaction transaction);

    boolean isNotParseableOrFuture(Transaction transaction);

    void deleteTransactions();

    Statistics getCurrentStatistics();
}
