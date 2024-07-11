package com.suit.checkout.models.dtos;

import com.suit.checkout.models.Pagamentos;

public record PagamentoRequestDTO(
        String requestNumber,
        String dueDate,
        Double amount,
        String callbackUrl,
        ClientRequestDTO client
) {
    public PagamentoRequestDTO(Pagamentos pagamentos, String callbackUrl) {
        this(
                pagamentos.getId().toString(),
                pagamentos.getDataCriacaPagamento().toString(),
                pagamentos.getValorAPagar(),
                callbackUrl,
                new ClientRequestDTO(pagamentos)
        );
    }
}
