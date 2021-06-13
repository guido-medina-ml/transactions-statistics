package com.n26.service;

import com.n26.domain.Statistics;
import com.n26.domain.Transaction;
import com.n26.utils.Utils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class TransactionServiceImplTest {
    private TransactionService transactionService;

    @Before
    public void init() {
        transactionService = new TransactionServiceImpl();
        transactionService.deleteTransactions();
    }

    @Test
    public void testIsValidTime() {
        assertTrue(transactionService.isValidTime(new Timestamp(System.currentTimeMillis())));
        assertTrue(transactionService.isValidTime(new Timestamp(System.currentTimeMillis() - 1)));
        assertTrue(transactionService.isValidTime(new Timestamp(System.currentTimeMillis() - 59000)));
    }

    @Test
    public void testIsValidTimeOldDate() {
        assertFalse(transactionService.isValidTime(new Timestamp(System.currentTimeMillis() - 61000)));
    }

    @Test
    public void testAddTransactionOneTransaction() {
        transactionService.addTransaction(new Transaction(BigDecimal.valueOf(1.478), new Timestamp(System.currentTimeMillis())));
        Statistics currentStatistics = transactionService.getCurrentStatistics();
        assertNotNull(currentStatistics);
        assertEquals("Avg", currentStatistics.getAvg(), BigDecimal.valueOf(1.48));
        assertEquals("Max", currentStatistics.getMax(), BigDecimal.valueOf(1.48));
        assertEquals("Min", currentStatistics.getMin(), BigDecimal.valueOf(1.48));
        assertEquals("Sum", currentStatistics.getSum(), BigDecimal.valueOf(1.48));
        assertEquals("Count", 1L, currentStatistics.getCount());
    }

    @Test
    public void testAddTransactionTwoTransactionsDiffTimestamp() {
        transactionService.addTransaction(new Transaction(BigDecimal.valueOf(3.0), new Timestamp(System.currentTimeMillis() - 3000)));
        transactionService.addTransaction(new Transaction(BigDecimal.valueOf(5.0), new Timestamp(System.currentTimeMillis() - 6000)));
        Statistics currentStatistics = transactionService.getCurrentStatistics();
        assertNotNull(currentStatistics);
        assertEquals("Avg", currentStatistics.getAvg(), Utils.setScale(BigDecimal.valueOf(4)));
        assertEquals("Max", currentStatistics.getMax(), Utils.setScale(BigDecimal.valueOf(5)));
        assertEquals("Min", currentStatistics.getMin(), Utils.setScale(BigDecimal.valueOf(3)));
        assertEquals("Sum", currentStatistics.getSum(), Utils.setScale(BigDecimal.valueOf(8)));
        assertEquals("Count", 2L, currentStatistics.getCount());
    }

    @Test
    public void testAddTransactionTwoTransactionsSameTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() - 2000);
        transactionService.addTransaction(new Transaction(BigDecimal.valueOf(3.0), timestamp));
        transactionService.addTransaction(new Transaction(BigDecimal.valueOf(5.0), timestamp));
        Statistics currentStatistics = transactionService.getCurrentStatistics();
        assertNotNull(currentStatistics);
        assertEquals("Avg", currentStatistics.getAvg(), Utils.setScale(BigDecimal.valueOf(4)));
        assertEquals("Max", currentStatistics.getMax(), Utils.setScale(BigDecimal.valueOf(5)));
        assertEquals("Min", currentStatistics.getMin(), Utils.setScale(BigDecimal.valueOf(3)));
        assertEquals("Sum", currentStatistics.getSum(), Utils.setScale(BigDecimal.valueOf(8)));
        assertEquals("Count", 2L, currentStatistics.getCount());
    }

    @Test
    public void testAddTransactionsConcurrent() {
        int transactionsCount = 12;
        new CountDownLatch(transactionsCount);
        CompletableFuture.allOf(IntStream.range(1, transactionsCount).boxed().map(value ->
                CompletableFuture.runAsync(() ->
                        transactionService.addTransaction(new Transaction(BigDecimal.valueOf(value), new Timestamp(System.currentTimeMillis())))))
                .collect(Collectors.toList())
                .toArray(new CompletableFuture[]{})).join();

        Statistics currentStatistics = transactionService.getCurrentStatistics();
        assertNotNull(currentStatistics);
        assertEquals("Avg", Utils.setScale(BigDecimal.valueOf(6)), currentStatistics.getAvg());
        assertEquals("Max", Utils.setScale(BigDecimal.valueOf(11)), currentStatistics.getMax());
        assertEquals("Min", Utils.setScale(BigDecimal.valueOf(1)), currentStatistics.getMin());
        assertEquals("Sum", Utils.setScale(BigDecimal.valueOf(66)), currentStatistics.getSum());
        assertEquals("Count", 11L, currentStatistics.getCount());
    }
}