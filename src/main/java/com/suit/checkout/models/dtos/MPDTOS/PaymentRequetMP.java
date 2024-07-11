package com.suit.checkout.models.dtos.MPDTOS;

public record PaymentRequetMP(
        String description,
        String notification_url,
        String external_reference,
        PayerRequestMP payer,
        String payment_method_id,
        Double transaction_amount
) {

}
