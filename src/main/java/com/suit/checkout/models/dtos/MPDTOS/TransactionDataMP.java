package com.suit.checkout.models.dtos.MPDTOS;

public record TransactionDataMP(
        String qr_code,
        String qr_code_base64,
        String ticket_url
) {
}
