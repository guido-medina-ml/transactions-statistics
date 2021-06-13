package com.n26.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.n26.utils.Utils.setScale;

public class Statistics implements Serializable {
    private final BigDecimal sum;
    private final BigDecimal avg;
    private final BigDecimal max;
    private final BigDecimal min;
    private final long count;

    public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, Long count) {
        super();
        this.sum = setScale(sum);
        this.avg = setScale(avg);
        this.max = setScale(max);
        this.min = setScale(min);
        this.count = count;
    }

    public Statistics(Transaction transaction) {
        BigDecimal transactionAmount =  setScale(transaction.getAmount());
        this.sum = transactionAmount;
        this.avg = transactionAmount;
        this.max = transactionAmount;
        this.min = transactionAmount;
        this.count = 1L;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }
}
