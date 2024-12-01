package com.assignment.calculator.resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.calculator.model.ProductCalculationRequest;
import com.assignment.calculator.model.ProductCalculationResponse;
import com.assignment.calculator.service.ProductCalculatorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calculator")
public class ProductCalculatorController {

    private final ProductCalculatorService service;

    @PostMapping
    public Mono<ProductCalculationResponse> calculateProducts(@RequestBody @Valid Mono<ProductCalculationRequest> request) {
        return request.flatMap(r -> service.calculateProduct(r.productId(), r.amount()));
    }

    @PostMapping("/discount/amount")
    public Mono<ProductCalculationResponse> calculateProductsAmountBasedDiscount(@RequestBody @Valid Mono<ProductCalculationRequest> request) {
        return request.flatMap(r -> service.calculateProductsAmountBasedDiscount(r.productId(), r.amount()));
    }

    @PostMapping("/discount/percentage")
    public Mono<ProductCalculationResponse> calculateProductsPercentageBasedDiscount(@RequestBody @Valid Mono<ProductCalculationRequest> request) {
        return request.flatMap(r -> service.calculateProductsPercentageBasedDiscount(r.productId(), r.amount()));
    }
}
