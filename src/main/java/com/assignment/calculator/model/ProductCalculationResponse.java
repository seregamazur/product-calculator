package com.assignment.calculator.model;

import java.math.BigDecimal;

public record ProductCalculationResponse(BigDecimal originalPrice, BigDecimal discountedPrice,
                                         AppliedDiscount discount, int amount,
                                         Product product) {
}
