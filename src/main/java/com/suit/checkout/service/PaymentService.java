package com.suit.checkout.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;
import com.suit.checkout.models.Pagamentos;
import com.suit.checkout.models.dtos.*;
import com.suit.checkout.models.dtos.MPDTOS.*;
import com.suit.checkout.models.dtos.wl.ResponseData;
import com.suit.checkout.models.enums.StatusPagamento;
import com.suit.checkout.repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
public class PaymentService {

    private static final String  urlPayment ="https://ws.suitpay.app/api/v1/gateway/request-qrcode";
    private static final String callbackUrl = "https://rodapremios.com/api/payment/callback";

    private static final String ci = "leochagas10_1712778711095";
    private static final String cs = "dd1e86b4b1ec274fa4f8f6ba7f388e0a87d8cd31ef5fa375ea085076156260f1ed5f5febbea14b13a1577c37ad110853";
    private static final String tokenMercadoPago = "APP_USR-8420355569686117-060817-af73685d8e0d6c4d000ee38c97360345-1847731087";
    private static final String  urlGetPaymentMP = "https://api.mercadopago.com/v1/payments/";
    private static final String postUrl = "https://api.mercadopago.com/v1/payments";

    private static final String postUrlHorizon = "https://api.conta.ativopay.com/v1/transactions";
    private static final String skHorizon = "sk_live_jZQNXA8vIYByfjqnjLKEkS0klPJzNjhYQX5OL7seUb";
    private static final String pkHorizon = "pk_live_N47eCf5hPKI860YhfVdMSUrdwgSP5X";

    @Autowired
    private GenerateQRCode generateQRCode;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Object createPaymentMercadoPago(RequestApiPaymentDTO data){

        Pagamentos pagamentoModel = createPayment(data);
        pagamentoRepository.save(pagamentoModel);

        PagamentoRequestDTO pagamentoRequestDTO = new PagamentoRequestDTO(pagamentoModel, callbackUrl);

        IndentificationMP indentificationMP = new IndentificationMP("CPF", data.cpf());
        PayerRequestMP payerRequestMP = new PayerRequestMP("individual", "customer", data.email(), indentificationMP);
        PaymentRequetMP paymentRequetMP = new PaymentRequetMP("Payment for product", callbackUrl, pagamentoModel.getId().toString(), payerRequestMP, "pix", pagamentoModel.getValorAPagar());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenMercadoPago);
        headers.set("Content-Type", "application/json");
        headers.set("X-Idempotency-Key", UUID.randomUUID().toString());

        String json;
        try {
            json = new ObjectMapper().writeValueAsString(paymentRequetMP);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter para JSON.");
        }
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<ResponseMPRequest> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, entity, ResponseMPRequest.class);
        ResponseMPRequest responseMPRequest = responseEntity.getBody();
        ResponsePagamento responsePagamento = new ResponsePagamento(pagamentoModel.getId().toString(), responseMPRequest.point_of_interaction().transaction_data().qr_code_base64(), responseMPRequest.point_of_interaction().transaction_data().qr_code());
        return responsePagamento;
    }

    public Object createPaymentApi(RequestApiPaymentDTO data) {

        Pagamentos pagamentoModel = createPayment(data);
        pagamentoRepository.save(pagamentoModel);
        System.out.println(pagamentoModel.getId());
        PagamentoRequestDTO pagamentoRequestDTO = new PagamentoRequestDTO(pagamentoModel, callbackUrl);



        HttpHeaders headers = new HttpHeaders();
        headers.set("ci", ci);
        headers.set("cs", cs);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json;
        try {
            json = new ObjectMapper().writeValueAsString(pagamentoRequestDTO);
            System.out.println(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<ResponseApiSuit> responseEntity = restTemplate.exchange(urlPayment, HttpMethod.POST, entity, ResponseApiSuit.class);

        ResponseApiSuit responseMPRequest = responseEntity.getBody();
        pagamentoModel.setIdTransactionSuitPay(responseMPRequest.idTransaction());
        pagamentoRepository.save(pagamentoModel);

        return responseMPRequest;
    }

    public Pagamentos createPayment(RequestApiPaymentDTO data) {
        Pagamentos pagamentos = new Pagamentos(data);
        return pagamentoRepository.save(pagamentos);
    }

    public void setStatusPaymentApproved(String paymentId){
        Pagamentos payment = pagamentoRepository.findById(UUID.fromString(paymentId)).orElseThrow(() -> new RuntimeException("Não encontrado pagamento com este ID!"));
        payment.setStatusPagamento(StatusPagamento.PAGO);
        payment.setDataPagamento(LocalDateTime.now());
        pagamentoRepository.save(payment);

    }

    public void setStatusPaymentCancel(String paymentId){
        Pagamentos payment = pagamentoRepository.findById(UUID.fromString(paymentId)).orElseThrow(() -> new RuntimeException("Não encontrado pagamento com este ID!"));
        payment.setStatusPagamento(StatusPagamento.CANCELADO);
        payment.setDataExpiracaoPagamento(LocalDateTime.now());
        pagamentoRepository.save(payment);
    }

    public void verifyStatusPaymentInMp(String idInMP){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + tokenMercadoPago);
        String urlGetPaymentMP = "https://api.mercadopago.com/v1/payments/" + idInMP;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        GetPaymentResponse paymentResponse = restTemplate.exchange(urlGetPaymentMP, HttpMethod.GET, entity, GetPaymentResponse.class).getBody();
        if (Objects.equals(paymentResponse.status(), "approved")){
            setStatusPaymentApproved(paymentResponse.external_reference());
        }
        if (Objects.equals(paymentResponse.status(), "cancelled") ||
                Objects.equals(paymentResponse.status(), "canceled") ||
                Objects.equals(paymentResponse.status(), "Cancelled") ||
                Objects.equals(paymentResponse.status(), "CANCELLED") ||
                Objects.equals(paymentResponse.status(), "Cancelado") ||
                Objects.equals(paymentResponse.status(), "CANCELADO")) {
            setStatusPaymentCancel(paymentResponse.external_reference());
        }
    }


    public Pagamentos callback(ReturnSuitPay data) {
        Pagamentos pagamento = pagamentoRepository.findPagamentosByIdTransactionSuitPay((data.idTransaction()));
        if (Objects.equals(data.statusTransaction(), "PAID_OUT")){
            pagamento.setStatusPagamento(StatusPagamento.PAGO);
            pagamento.setDataPagamento(LocalDateTime.now());
            pagamentoRepository.save(pagamento);
            return pagamento;
        } else if (Objects.equals(data.statusTransaction(), "CHARGEBACK")){
            pagamento.setStatusPagamento(StatusPagamento.CANCELADO);
            pagamento.setDataExpiracaoPagamento(LocalDateTime.now());
            pagamentoRepository.save(pagamento);
            return pagamento;
        }
        return null;
    }

    public List<Pagamentos> findAllPayments() {
        return pagamentoRepository.findAll();
    }

    public Pagamentos findPaymentById(UUID id) {
        return pagamentoRepository.findById(id).orElseThrow();
    }

    public List<Pagamentos> findPaymentsByStatusPagamento(StatusPagamento statusPagamento){
        return pagamentoRepository.findAllByStatusPagamento(statusPagamento);
    }

    public Pagamentos getPagamentoByIdTransactionSuitPay(String idTransactionSuitPay){
        return pagamentoRepository.findPagamentosByIdTransactionSuitPay(idTransactionSuitPay);
    }

    public ResponseRifaValues findPaymentsByNomeRifa(String nomeRifa){
        List<Pagamentos> payments = pagamentoRepository.findAllByNomeRifa(nomeRifa);
        Double valorPedidosTotal = 0.0;
        Double valorAprovadoTotal = 0.0;
        Double valorPendenteTotal = 0.0;
        Double valorExpiradoTotal = 0.0;

        Double valorPedidosHoje = 0.0;
        Double valorAprovadoHoje = 0.0;
        Double valorPendenteHoje = 0.0;
        Double valorExpiradoHoje = 0.0;

        for (Pagamentos p : payments) {
            valorPedidosTotal += p.getValorAPagar();
            if (p.getStatusPagamento() == StatusPagamento.PAGO) {
                valorAprovadoTotal += p.getValorAPagar();
            }
            if (p.getStatusPagamento() == StatusPagamento.PENDENTE) {
                valorPendenteTotal += p.getValorAPagar();
            }
            if (p.getStatusPagamento() == StatusPagamento.CANCELADO) {
                valorExpiradoTotal += p.getValorAPagar();
            }
            if (p.getDataCriacaPagamento().toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
                valorPedidosHoje += p.getValorAPagar();
                if (p.getStatusPagamento() == StatusPagamento.PAGO) {
                    valorAprovadoHoje += p.getValorAPagar();
                }
                if (p.getStatusPagamento() == StatusPagamento.PENDENTE) {
                    valorPendenteHoje += p.getValorAPagar();
                }
                if (p.getStatusPagamento() == StatusPagamento.CANCELADO) {
                    valorExpiradoHoje += p.getValorAPagar();
                }
            }

        }

        return new ResponseRifaValues(valorPedidosTotal, valorAprovadoTotal, valorPendenteTotal, valorExpiradoTotal, valorPedidosHoje, valorAprovadoHoje, valorPendenteHoje, valorExpiradoHoje, payments);
    }


}
