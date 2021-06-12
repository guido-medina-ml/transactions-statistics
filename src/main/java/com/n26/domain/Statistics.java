package com.n26.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Statistics implements Serializable {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;


    public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, Long count) {
        super();
        this.sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.avg = avg.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.max = max.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.min = min.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
