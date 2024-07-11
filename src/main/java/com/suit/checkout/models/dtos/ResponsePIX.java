package com.suit.checkout.models.dtos;

public record ResponsePIX(
        String paymentCodeBase64,
        String paymentCode,
        String description
) {
}
