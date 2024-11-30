package com.assignment.calculator.discount;

import java.math.BigDecimal;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsulManagedDiscountPolicyTest {

    private ConsulManagedDiscountPolicy discountPolicy;

    @BeforeEach
    void setup() {
        discountPolicy = new ConsulManagedDiscountPolicy();

        TreeMap<Integer, BigDecimal> amountBased = new TreeMap<>();
        amountBased.put(1, BigDecimal.valueOf(5));
        amountBased.put(5, BigDecimal.valueOf(10));
        amountBased.put(10, BigDecimal.valueOf(15));

        TreeMap<Integer, BigDecimal> percentageBased = new TreeMap<>();
        percentageBased.put(1, BigDecimal.valueOf(1));
        percentageBased.put(5, BigDecimal.valueOf(5));
        percentageBased.put(10, BigDecimal.valueOf(10));

        discountPolicy.setAmountBasedDiscountPolicy(amountBased);
        discountPolicy.setPercentageBasedDiscountPolicy(percentageBased);
    }

    @Test
    void getAmountBasedDiscount_shouldReturnExactMatch() {
        BigDecimal discount = discountPolicy.getAmountBasedDiscount(5);
        assertEquals(BigDecimal.valueOf(10).setScale(2), discount);
    }

    @Test
    void getAmountBasedDiscount_shouldReturnNearestLowerKey() {
        BigDecimal discount = discountPolicy.getAmountBasedDiscount(8);
        assertEquals(BigDecimal.valueOf(10).setScale(2), discount);
    }

    @Test
    void getPercentageBasedDiscount_shouldHandleExactMatch() {
        BigDecimal discount = discountPolicy.getPercentageBasedDiscount(10);
        assertEquals(BigDecimal.valueOf(10).setScale(2), discount);
    }

    @Test
    void getPercentageBasedDiscount_shouldReturnNearestLowerKey() {
        BigDecimal discount = discountPolicy.getPercentageBasedDiscount(7);
        assertEquals(BigDecimal.valueOf(5).setScale(2), discount);
    }

    @Test
    void getPercentageBasedDiscount_shouldHandleEmptyPolicy() {
        discountPolicy.setPercentageBasedDiscountPolicy(new TreeMap<>());
        BigDecimal discount = discountPolicy.getPercentageBasedDiscount(7);
        assertEquals(BigDecimal.valueOf(0).setScale(2), discount);
    }

}