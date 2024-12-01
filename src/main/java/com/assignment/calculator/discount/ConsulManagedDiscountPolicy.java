package com.assignment.calculator.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
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

    public BigDecimal getAmountBasedDiscount(int amount) {
        return findApplicableDiscount(amount, amountBasedDiscountPolicy);
    }

    public BigDecimal getPercentageBasedDiscount(int amount) {
        return findApplicableDiscount(amount, percentageBasedDiscountPolicy);
    }

    private BigDecimal findApplicableDiscount(int amount, TreeMap<Integer, BigDecimal> discountPolicy) {
        if (discountPolicy.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        Map.Entry<Integer, BigDecimal> entry = discountPolicy.floorEntry(amount);
        return entry == null
            ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            : entry.getValue().setScale(2, RoundingMode.HALF_UP);
    }

}
