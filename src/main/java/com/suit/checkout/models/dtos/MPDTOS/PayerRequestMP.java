package com.suit.checkout.models.dtos.MPDTOS;

public record PayerRequestMP(
        String entity_type,
        String type,
        String email,
        IndentificationMP identification
) {
}
