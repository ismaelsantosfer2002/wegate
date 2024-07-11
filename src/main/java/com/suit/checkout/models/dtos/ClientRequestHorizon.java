package com.suit.checkout.models.dtos;

public record ClientRequestHorizon(
        String name,
        String email,
        String phone,
        HorizonDocumentRequestDTO document
) {
}
