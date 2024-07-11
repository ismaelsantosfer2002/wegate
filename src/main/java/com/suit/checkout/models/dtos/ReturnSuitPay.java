package com.suit.checkout.models.dtos;

public record ReturnSuitPay(
        String idTransaction,
        String typeTransaction,
        String statusTransaction,
        Double value,
        String payerName,
        String payerTaxId

) {
}
