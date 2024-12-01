package com.assignment.calculator.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assignment.calculator.dao.ProductDao;
import com.assignment.calculator.discount.ConsulManagedDiscountPolicy;
import com.assignment.calculator.model.AppliedDiscount;
import com.assignment.calculator.model.DiscountType;
import com.assignment.calculator.model.Product;

import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCalculatorServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private ConsulManagedDiscountPolicy discountPolicy;

    private ProductCalculatorService service;

    @BeforeEach
    void setUp() {
        service = new ProductCalculatorService(productDao, discountPolicy);
    }

    @Test
    void calculateProduct_shouldReturnCorrectResponse() {
        Product mockProduct = new Product(UUID.randomUUID(), BigDecimal.valueOf(100));
        when(productDao.findProductByUUID(any())).thenReturn(mockProduct);

        UUID productId = UUID.randomUUID();
        int productAmount = 10;

        StepVerifier.create(service.calculateProduct(productId, productAmount))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.originalPrice());
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.discountedPrice());
                assertNull(response.discount());
                assertEquals(productAmount, response.amount());
                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductAmountBasedDiscount_shouldApplyDiscount() {
        Product mockProduct = new Product(UUID.randomUUID(), BigDecimal.valueOf(100));
        when(productDao.findProductByUUID(any())).thenReturn(mockProduct);
        when(discountPolicy.getAmountBasedDiscount(10)).thenReturn(BigDecimal.valueOf(200).setScale(2));

        UUID productId = UUID.randomUUID();
        int productAmount = 10;

        StepVerifier.create(service.calculateProductsAmountBasedDiscount(productId, productAmount))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.originalPrice());
                assertEquals(BigDecimal.valueOf(800).setScale(2), response.discountedPrice());
                assertEquals(productAmount, response.amount());

                AppliedDiscount discount = response.discount();
                assertEquals(DiscountType.AMOUNT, discount.type());
                assertEquals(BigDecimal.valueOf(200).setScale(2), discount.value());

                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductPercentageBasedDiscount_shouldApplyDiscountToProduct() {
        Product mockProduct = new Product(UUID.randomUUID(), BigDecimal.valueOf(100));
        when(productDao.findProductByUUID(any())).thenReturn(mockProduct);
        when(discountPolicy.getPercentageBasedDiscount(10)).thenReturn(BigDecimal.valueOf(10).setScale(2));

        UUID productId = UUID.randomUUID();
        int productAmount = 10;

        StepVerifier.create(service.calculateProductsPercentageBasedDiscount(productId, productAmount))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.originalPrice());
                assertEquals(BigDecimal.valueOf(900).setScale(2), response.discountedPrice());
                assertEquals(productAmount, response.amount());

                AppliedDiscount discount = response.discount();
                assertEquals(DiscountType.PERCENTAGE, discount.type());
                assertEquals(BigDecimal.valueOf(10).setScale(2), discount.value());

                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductAmountBasedDiscount_shouldApply0Discount() {
        Product mockProduct = new Product(UUID.randomUUID(), BigDecimal.valueOf(100));
        when(productDao.findProductByUUID(any())).thenReturn(mockProduct);
        when(discountPolicy.getAmountBasedDiscount(10)).thenReturn(BigDecimal.ZERO.setScale(2));

        UUID productId = UUID.randomUUID();
        int productAmount = 10;

        StepVerifier.create(service.calculateProductsAmountBasedDiscount(productId, productAmount))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.originalPrice());
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.discountedPrice());
                assertEquals(productAmount, response.amount());

                AppliedDiscount discount = response.discount();
                assertEquals(DiscountType.AMOUNT, discount.type());
                assertEquals(BigDecimal.ZERO.setScale(2), discount.value());

                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductPercentageBasedDiscount_shouldApply0Discount() {
        Product mockProduct = new Product(UUID.randomUUID(), BigDecimal.valueOf(100));
        when(productDao.findProductByUUID(any())).thenReturn(mockProduct);
        when(discountPolicy.getPercentageBasedDiscount(10)).thenReturn(BigDecimal.ZERO.setScale(2));

        UUID productId = UUID.randomUUID();
        int productAmount = 10;

        StepVerifier.create(service.calculateProductsPercentageBasedDiscount(productId, productAmount))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.originalPrice());
                assertEquals(BigDecimal.valueOf(1000).setScale(2), response.discountedPrice());
                assertEquals(productAmount, response.amount());

                AppliedDiscount discount = response.discount();
                assertEquals(DiscountType.PERCENTAGE, discount.type());
                assertEquals(BigDecimal.ZERO.setScale(2), discount.value());

                return true;
            })
            .verifyComplete();
    }

}
