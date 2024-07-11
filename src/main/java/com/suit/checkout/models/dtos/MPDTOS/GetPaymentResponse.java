package com.suit.checkout.models.dtos.MPDTOS;

public record GetPaymentResponse(
        String status,
        String external_reference
) {
}
