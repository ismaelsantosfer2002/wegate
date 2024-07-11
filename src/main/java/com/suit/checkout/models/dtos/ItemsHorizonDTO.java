package com.suit.checkout.models.dtos;

public record ItemsHorizonDTO(
        String title,
        Integer unitPrice,
        Integer quantity,
        Boolean tangible
) {
}
