package com.suit.checkout.models.dtos;

public record ResponsePagamento(
        String idTransaction,
        String paymentCodeBase64,
        String paymentCode
) {
}
