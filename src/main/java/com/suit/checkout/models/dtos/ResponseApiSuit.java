package com.suit.checkout.models.dtos;

public record ResponseApiSuit(
        String idTransaction,
        String paymentCode,
        String response,
        String paymentCodeBase64
) {
}
