package com.assignment.calculator.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assignment.calculator.dao.ProductDao;
import com.assignment.calculator.discount.ConsulManagedDiscountPolicy;
import com.assignment.calculator.model.Product;

import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyList;
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
    void calculateProducts_shouldReturnCorrectResponse() {
        List<Product> mockProducts = List.of(
            new Product(UUID.randomUUID(), BigDecimal.valueOf(100)),
            new Product(UUID.randomUUID(), BigDecimal.valueOf(50))
        );

        when(productDao.findProductsByUUIDs(anyList())).thenReturn(mockProducts);

        List<UUID> productIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        StepVerifier.create(service.calculateProducts(productIds))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.grossTotal());
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.netTotal());
                assertEquals(2, response.amountOfProducts());
                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductsAmountBasedDiscount_shouldApplyDiscount() {
        List<Product> mockProducts = List.of(
            new Product(UUID.randomUUID(), BigDecimal.valueOf(100).setScale(2)),
            new Product(UUID.randomUUID(), BigDecimal.valueOf(50).setScale(2))
        );

        when(productDao.findProductsByUUIDs(anyList())).thenReturn(mockProducts);
        when(discountPolicy.getAmountBasedDiscount(2)).thenReturn(BigDecimal.valueOf(20).setScale(2));

        List<UUID> productIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        StepVerifier.create(service.calculateProductsAmountBasedDiscount(productIds))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.grossTotal());
                assertEquals(BigDecimal.valueOf(20).setScale(2), response.discount());
                assertEquals(BigDecimal.valueOf(130).setScale(2), response.netTotal());
                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductsPercentageBasedDiscount_shouldApplyDiscountToEachProduct() {
        List<Product> mockProducts = List.of(
            new Product(UUID.randomUUID(), BigDecimal.valueOf(100)),
            new Product(UUID.randomUUID(), BigDecimal.valueOf(50))
        );

        when(productDao.findProductsByUUIDs(anyList())).thenReturn(mockProducts);
        when(discountPolicy.getPercentageBasedDiscount(2)).thenReturn(BigDecimal.valueOf(10).setScale(2));

        List<UUID> productIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        StepVerifier.create(service.calculateProductsPercentageBasedDiscount(productIds))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.grossTotal());
                assertEquals(BigDecimal.valueOf(10).setScale(2), response.discount());
                assertEquals(BigDecimal.valueOf(135).setScale(2), response.netTotal());
                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductsAmountBasedDiscount_shouldApply0Discount() {
        List<Product> mockProducts = List.of(
            new Product(UUID.randomUUID(), BigDecimal.valueOf(100)),
            new Product(UUID.randomUUID(), BigDecimal.valueOf(50))
        );

        when(productDao.findProductsByUUIDs(anyList())).thenReturn(mockProducts);
        when(discountPolicy.getPercentageBasedDiscount(2)).thenReturn(BigDecimal.valueOf(0).setScale(2));

        List<UUID> productIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        StepVerifier.create(service.calculateProductsPercentageBasedDiscount(productIds))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.grossTotal());
                assertEquals(BigDecimal.valueOf(0).setScale(2), response.discount());
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.netTotal());
                return true;
            })
            .verifyComplete();
    }

    @Test
    void calculateProductsPercentageBasedDiscount_shouldApply0DiscountToEachProduct() {
        List<Product> mockProducts = List.of(
            new Product(UUID.randomUUID(), BigDecimal.valueOf(100)),
            new Product(UUID.randomUUID(), BigDecimal.valueOf(50))
        );

        when(productDao.findProductsByUUIDs(anyList())).thenReturn(mockProducts);
        when(discountPolicy.getPercentageBasedDiscount(2)).thenReturn(BigDecimal.valueOf(0).setScale(2));

        List<UUID> productIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        StepVerifier.create(service.calculateProductsPercentageBasedDiscount(productIds))
            .expectNextMatches(response -> {
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.grossTotal());
                assertEquals(BigDecimal.valueOf(0).setScale(2), response.discount());
                assertEquals(BigDecimal.valueOf(150).setScale(2), response.netTotal());
                return true;
            })
            .verifyComplete();
    }
}
