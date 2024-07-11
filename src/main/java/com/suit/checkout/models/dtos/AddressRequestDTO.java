package com.suit.checkout.models.dtos;

public record AddressRequestDTO(
        String street,
        String streetNumber,
        String complement,
        String zipCode,
        String neighborhood,
        String city,
        String state,
        String country
) {
}
