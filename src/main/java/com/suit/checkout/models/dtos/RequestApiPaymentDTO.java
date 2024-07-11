package com.suit.checkout.models.dtos;

public record RequestApiPaymentDTO(
        String nomePagador,
        String email,
        String telefone,
        Double valorAPagar,
        String cpf,
        String nomeRifa
) {
}
