package com.assignment.calculator.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.assignment.calculator.model.Product;

@Component
public class ProductDao {

    /**
     * Like *SELECT product_id, price from PRODUCTS WHERE product_id IN (...)*
     */
    public List<Product> findProductsByUUIDs(List<UUID> ids) {
        Random random = new Random();
        return ids.stream()
            .map(i -> new Product(i, new BigDecimal(
                random.nextInt(10) + 1).setScale(2, RoundingMode.HALF_UP)))
            .toList();
    }
}
