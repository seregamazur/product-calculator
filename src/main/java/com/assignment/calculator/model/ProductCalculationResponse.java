package com.assignment.calculator.model;

import java.math.BigDecimal;
import java.util.List;

public record ProductCalculationResponse(BigDecimal grossTotal, BigDecimal netTotal,
                                         BigDecimal discount, int amountOfProducts,
                                         List<Product> products) {
}
