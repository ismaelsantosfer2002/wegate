package com.suit.checkout.models.dtos;

import com.suit.checkout.models.Pagamentos;

public record ClientRequestDTO(
        String name,
        String document,
        String phoneNumber,
        String email
) {
    public ClientRequestDTO(Pagamentos pagamentos) {
        this(
                pagamentos.getNomeComprador(),
                pagamentos.getCpf(),
                pagamentos.getTelefonePagador(),
                pagamentos.getEmailPagador()
        );
    }
}
