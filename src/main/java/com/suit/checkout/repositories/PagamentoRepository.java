package com.suit.checkout.repositories;

import com.suit.checkout.models.Pagamentos;
import com.suit.checkout.models.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<Pagamentos, UUID> {

    List<Pagamentos> findAllByStatusPagamento(StatusPagamento statusPagamento);
    Pagamentos findPagamentosByIdTransactionSuitPay(String idTransactionSuitPay);

    List<Pagamentos> findAllByNomeRifa(String nomeRifa);

    Pagamentos findPagamentosByIdTransactionAtivoPay(Integer idTransactionAtivoPay);
    Pagamentos findPagamentosByIdTransactionWegate(String idTransactionAtivoPay);

}
