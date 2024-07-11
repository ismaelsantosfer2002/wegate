package com.suit.checkout.models;

import com.suit.checkout.models.dtos.RequestApiPaymentDTO;
import com.suit.checkout.models.enums.StatusPagamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamentos")
public class Pagamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nomeComprador;
    private String emailPagador;
    private String telefonePagador;
    private String cpf;
    private Double valorAPagar;
    private LocalDateTime dataCriacaPagamento;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataExpiracaoPagamento;
    private StatusPagamento statusPagamento;
    private String idTransactionSuitPay;
    private Integer idTransactionAtivoPay;
    private String idTransactionWegate;
    private String nomeRifa;

    public Pagamentos() {
    }

    public Pagamentos(RequestApiPaymentDTO data) {
        this.nomeComprador = data.nomePagador();
        this.emailPagador = data.email();
        this.telefonePagador = data.telefone();
        this.valorAPagar = data.valorAPagar();
        this.dataCriacaPagamento = LocalDateTime.now();
        this.dataExpiracaoPagamento = null;
        this.dataPagamento = null;
        this.statusPagamento = StatusPagamento.PENDENTE;
        this.cpf = data.cpf();
        this.idTransactionSuitPay = null;
        this.nomeRifa = data.nomeRifa();
        this.idTransactionAtivoPay = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }

    public String getEmailPagador() {
        return emailPagador;
    }

    public void setEmailPagador(String emailPagador) {
        this.emailPagador = emailPagador;
    }

    public String getTelefonePagador() {
        return telefonePagador;
    }

    public void setTelefonePagador(String telefonePagador) {
        this.telefonePagador = telefonePagador;
    }

    public Double getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(Double valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public LocalDateTime getDataCriacaPagamento() {
        return dataCriacaPagamento;
    }

    public void setDataCriacaPagamento(LocalDateTime dataCriacaPagamento) {
        this.dataCriacaPagamento = dataCriacaPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDateTime getDataExpiracaoPagamento() {
        return dataExpiracaoPagamento;
    }

    public void setDataExpiracaoPagamento(LocalDateTime dataExpiracaoPagamento) {
        this.dataExpiracaoPagamento = dataExpiracaoPagamento;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getIdTransactionSuitPay() {
        return idTransactionSuitPay;
    }

    public void setIdTransactionSuitPay(String idTransactionSuitPay) {
        this.idTransactionSuitPay = idTransactionSuitPay;
    }

    public String getNomeRifa() {
        return nomeRifa;
    }

    public void setNomeRifa(String nomeRifa) {
        this.nomeRifa = nomeRifa;
    }

    public Integer getIdTransactionAtivoPay() {
        return idTransactionAtivoPay;
    }

    public void setIdTransactionAtivoPay(Integer idTransactionAtivoPay) {
        this.idTransactionAtivoPay = idTransactionAtivoPay;
    }


    public String getIdTransactionWegate() {
        return idTransactionWegate;
    }

    public void setIdTransactionWegate(String idTransactionWegate) {
        this.idTransactionWegate = idTransactionWegate;
    }
}

