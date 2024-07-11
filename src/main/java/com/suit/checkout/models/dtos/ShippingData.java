package com.suit.checkout.models.dtos;

public record ShippingData(
        Integer fee,
        AddressRequestDTO address
) {
}
