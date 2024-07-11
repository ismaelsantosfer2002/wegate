package com.suit.checkout.models.dtos;

import com.suit.checkout.models.Pagamentos;

import java.util.List;

public record ResponseRifaValues(
        Double valorPedidosTotal,
        Double valorAprovadoTotal,
        Double valorPendenteTotal,
        Double valorExpiradoTotal,
        Double valorPedidosHoje,
        Double valorAprovadoHoje,
        Double valorPendenteHoje,
        Double valorExpiradoHoje,
        List<Pagamentos> pagamentos

) {
}
