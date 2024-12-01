package com.assignment.calculator.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.assignment.calculator.model.Product;

@Component
public class ProductDao {

    private final Random random = new Random();

    public Product findProductByUUID(UUID id) {
        return new Product(id, new BigDecimal(random.nextInt(10) + 1).setScale(2, RoundingMode.HALF_UP));
    }
}
