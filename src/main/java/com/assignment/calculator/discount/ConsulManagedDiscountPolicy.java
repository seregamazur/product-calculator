package com.assignment.calculator.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "config.product-calculator")
@RefreshScope
@Getter
@Setter
public class ConsulManagedDiscountPolicy {

    @Value("#{${policies.discount.amount-based}}")
    private TreeMap<Integer, BigDecimal> amountBasedDiscountPolicy = new TreeMap<>();

    @Value("#{${policies.discount.percentage-based}}")
    private TreeMap<Integer, BigDecimal> percentageBasedDiscountPolicy = new TreeMap<>();

    public BigDecimal getAmountBasedDiscount(int size) {
        return amountBasedDiscountPolicy.isEmpty()
            ? new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
            : amountBasedDiscountPolicy.floorEntry(size).getValue().setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentageBasedDiscount(int size) {
        return percentageBasedDiscountPolicy.isEmpty()
            ? new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
            : percentageBasedDiscountPolicy.floorEntry(size).getValue().setScale(2, RoundingMode.HALF_UP);
    }

}
