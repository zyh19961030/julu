package com.qu.util;

import java.math.BigDecimal;

public class PriceUtil {

    public static String toYuanStr(Integer price) {
        return new BigDecimal(price).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    public static BigDecimal toYuanDecimal(Integer price) {
        return new BigDecimal(price).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static Integer toFenInt(BigDecimal price) {
        return price.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static Integer toFenInt(Integer price) {
        return new BigDecimal(price).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static String toFenStr(BigDecimal price) {
        return price.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

}
