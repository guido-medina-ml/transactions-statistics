package com.n26.utils;

import java.math.BigDecimal;

public class Utils {
    public static BigDecimal setScale(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
