package com.n26.service;

import com.n26.domain.Statistics;
import com.n26.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionServiceImpl implements TransactionService {
    public static final long ONE_MINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private static final List<Transaction> TRANSACTIONS = new ArrayList<>();
    private Statistics statistics;
    private final Object lock = new Object();

    public TransactionServiceImpl() {
        // All transactions older than 1 minute from now will be removed from the list
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
            TRANSACTIONS.removeIf(entry -> System.currentTimeMillis() - entry.getTimestamp().getTime() > ONE_MINUTE);
            calculateStats();
        }, 1, 1, TimeUnit.SECONDS);

    }

    @Override
    public boolean isValidTime(Date timestamp) {
        return System.currentTimeMillis() - timestamp.getTime() < ONE_MINUTE;
    }

    @Override
    public boolean isNotParseableOrFuture(Transaction transaction) {
        return System.currentTimeMillis() - transaction.getTimestamp().getTime() < 0;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if ((System.currentTimeMillis() - transaction.getTimestamp().getTime()) > ONE_MINUTE) {
            return;
        }

        synchronized (lock) {
            TRANSACTIONS.add(transaction);
            calculateStats();
            log.info("Transaction added");
        }
    }

    @Override
    public void deleteTransactions() {
        TRANSACTIONS.clear();
        log.info("All transactions were deleted");
    }

    @Async
    public void calculateStats() {
        DoubleSummaryStatistics stat = TRANSACTIONS.stream().mapToDouble(Transaction::getAmount)
                .summaryStatistics();

        if (TRANSACTIONS.isEmpty()) {
            statistics = new Statistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0L);
        } else {
            statistics = new Statistics(BigDecimal.valueOf(stat.getSum()), BigDecimal.valueOf(stat.getAverage()), BigDecimal.valueOf(stat.getMax()), BigDecimal.valueOf(stat.getMin()), stat.getCount());
        }
    }

    @Override
    public Statistics getCurrentStatistics() {
       return statistics;
    }
}
