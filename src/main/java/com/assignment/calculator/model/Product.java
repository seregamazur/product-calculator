package com.assignment.calculator.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Product {
    private UUID id;
    private BigDecimal price;
}
