package com.assignment.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.assignment.calculator.dao.ProductDao;
import com.assignment.calculator.discount.ConsulManagedDiscountPolicy;
import com.assignment.calculator.model.AppliedDiscount;
import com.assignment.calculator.model.DiscountType;
import com.assignment.calculator.model.Product;
import com.assignment.calculator.model.ProductCalculationResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductCalculatorService {

    private final ProductDao productDao;
    private final ConsulManagedDiscountPolicy discountPolicy;

    public Mono<ProductCalculationResponse> calculateProduct(UUID id, int amount) {
        return Mono.fromCallable(() -> {
            Product product = productDao.findProductByUUID(id);
            BigDecimal gross = calculatePrice(product, amount);
            return new ProductCalculationResponse(gross, gross, null, amount, product);
        });
    }

    public Mono<ProductCalculationResponse> calculateProductsAmountBasedDiscount(UUID id, int amount) {
        return Mono.fromCallable(() -> {
            Product product = productDao.findProductByUUID(id);
            BigDecimal gross = calculatePrice(product, amount);
            BigDecimal discount = discountPolicy.getAmountBasedDiscount(amount);
            BigDecimal net = gross.subtract(discount).setScale(2, RoundingMode.HALF_UP);
            return new ProductCalculationResponse(gross, net, new AppliedDiscount(DiscountType.AMOUNT, discount), amount, product);
        });
    }

    public Mono<ProductCalculationResponse> calculateProductsPercentageBasedDiscount(UUID id, int amount) {
        return Mono.fromCallable(() -> {
            Product product = productDao.findProductByUUID(id);
            BigDecimal gross = calculatePrice(product, amount);
            BigDecimal discount = discountPolicy.getPercentageBasedDiscount(amount);
            applyPercentageDiscountToProduct(product, discount);
            BigDecimal net = calculatePrice(product, amount);
            return new ProductCalculationResponse(gross, net, new AppliedDiscount(DiscountType.PERCENTAGE, discount), amount, product);
        });
    }

    private BigDecimal calculatePrice(Product product, int amount) {
        return product.getPrice().multiply(new BigDecimal(amount)).setScale(2, RoundingMode.HALF_UP);
    }

    private void applyPercentageDiscountToProduct(Product product, BigDecimal discount) {
        BigDecimal discountAmount = product.getPrice()
            .multiply(discount)
            .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
        product.setPrice(product.getPrice().subtract(discountAmount));
    }
}
