package com.assignment.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.assignment.calculator.dao.ProductDao;
import com.assignment.calculator.discount.ConsulManagedDiscountPolicy;
import com.assignment.calculator.model.Product;
import com.assignment.calculator.model.ProductCalculationResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductCalculatorService {

    private final ProductDao productDao;
    private final ConsulManagedDiscountPolicy discountPolicy;

    public Mono<ProductCalculationResponse> calculateProducts(List<UUID> ids) {
        return Mono.fromCallable(() -> {
            List<Product> products = productDao.findProductsByUUIDs(ids);
            BigDecimal gross = calculateGross(products);
            return new ProductCalculationResponse(gross, gross, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), products.size(), products);
        });
    }

    public Mono<ProductCalculationResponse> calculateProductsAmountBasedDiscount(List<UUID> ids) {
        return Mono.fromCallable(() -> {
            List<Product> products = productDao.findProductsByUUIDs(ids);
            BigDecimal gross = calculateGross(products);
            BigDecimal discount = discountPolicy.getAmountBasedDiscount(ids.size());
            BigDecimal net = gross.subtract(discount).setScale(2, RoundingMode.HALF_UP);
            return new ProductCalculationResponse(gross, net, discount, products.size(), products);
        });
    }

    public Mono<ProductCalculationResponse> calculateProductsPercentageBasedDiscount(List<UUID> ids) {
        return Mono.fromCallable(() -> {
            List<Product> products = productDao.findProductsByUUIDs(ids);
            BigDecimal gross = calculateGross(products);
            BigDecimal discount = discountPolicy.getPercentageBasedDiscount(ids.size());
            applyPercentageDiscountToProducts(products, discount);
            BigDecimal net = calculateGross(products);
            return new ProductCalculationResponse(gross, net, discount, products.size(), products);
        });
    }

    private BigDecimal calculateGross(List<Product> products) {
        return products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), BigDecimal::add);
    }

    private void applyPercentageDiscountToProducts(List<Product> products, BigDecimal discount) {
        products.forEach(product -> {
            BigDecimal discountAmount = product.getPrice()
                .multiply(discount)
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
            product.setPrice(product.getPrice().subtract(discountAmount));
        });
    }
}
