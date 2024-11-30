package com.assignment.calculator.resource;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.calculator.model.ProductCalculationResponse;
import com.assignment.calculator.service.ProductCalculatorService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calculator")
public class ProductCalculatorController {

    private final ProductCalculatorService service;

    @PostMapping
    public Mono<ProductCalculationResponse> calculateProducts(@RequestBody Mono<List<UUID>> productIds) {
        return productIds
            .flatMap(ids -> {
                if (ids.isEmpty()) {
                    return Mono.error(new IllegalArgumentException("Product IDs cannot be empty"));
                }
                return service.calculateProducts(ids);
            });
    }

    @PostMapping("/amount")
    public Mono<ProductCalculationResponse> calculateProductsAmountBasedDiscount(@RequestBody Mono<List<UUID>> productIds) {
        return productIds
            .flatMap(ids -> {
                if (ids.isEmpty()) {
                    return Mono.error(new IllegalArgumentException("Product IDs cannot be empty"));
                }
                return service.calculateProductsAmountBasedDiscount(ids);
            });
    }

    @PostMapping("/percentage")
    public Mono<ProductCalculationResponse> calculateProductsPercentageBasedDiscount(@RequestBody Mono<List<UUID>> productIds) {
        return productIds
            .flatMap(ids -> {
                if (ids.isEmpty()) {
                    return Mono.error(new IllegalArgumentException("Product IDs cannot be empty"));
                }
                return service.calculateProductsPercentageBasedDiscount(ids);
            });
    }
}
