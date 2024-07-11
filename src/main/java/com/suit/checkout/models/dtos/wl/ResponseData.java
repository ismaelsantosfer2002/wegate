package com.suit.checkout.models.dtos.wl;

import com.suit.checkout.models.dtos.ClientRequestHorizon;

public record ResponseData(
        String id,
        String status,
        Integer amount,
        Integer installments,
        Integer installmentValue,
        String externalRef,
        ClientRequestHorizon customer,
        PixObject pix,
        Boolean success
) {
}
