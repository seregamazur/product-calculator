package com.assignment.calculator.model;

import java.util.UUID;

import jakarta.validation.constraints.Min;

public record ProductCalculationRequest(UUID productId, @Min(1) int amount) {
}
