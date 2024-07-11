package com.suit.checkout.models.dtos;

import java.util.List;

public record HorizonRequestPaymentDTO(
        Integer amount,
        String externalRef,
        String postbackUrl,
        ClientRequestHorizon customer,
        Boolean traceable,
        ShippingData shipping,
        List<ItemsHorizonDTO> items,
        String paymentMethod

) {
}
