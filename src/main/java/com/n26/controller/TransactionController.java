package com.n26.controller;

import com.n26.domain.Statistics;
import com.n26.domain.Transaction;
import com.n26.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity addTransactions(@RequestBody Transaction transaction) {
        if (!transactionService.isValidTime(transaction.getTimestamp())) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        if (transactionService.isNotParseableOrFuture(transaction)) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        transactionService.addTransaction(transaction);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.DELETE)
    public ResponseEntity deleteTransactions() {
        transactionService.deleteTransactions();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public Statistics getStatistics() {
        return transactionService.getCurrentStatistics();
    }
}
