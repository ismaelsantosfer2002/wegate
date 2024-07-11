package com.suit.checkout.controller;

import com.google.zxing.WriterException;
import com.suit.checkout.exception.InvalidKeyException;
import com.suit.checkout.models.Pagamentos;
import com.suit.checkout.models.dtos.MPDTOS.NotificationMP;
import com.suit.checkout.models.dtos.PagamentoRequestDTO;
import com.suit.checkout.models.dtos.RequestApiPaymentDTO;
import com.suit.checkout.models.dtos.ResponseRifaValues;
import com.suit.checkout.models.dtos.ReturnSuitPay;
import com.suit.checkout.models.dtos.wl.DataCallbackDTO;
import com.suit.checkout.models.dtos.wl.WhiteLabelCallBackDTO;
import com.suit.checkout.models.enums.StatusPagamento;
import com.suit.checkout.service.AtivoPayService;
import com.suit.checkout.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final String keySenha = "dropgf3212024";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AtivoPayService ativoPayService;

    @PostMapping
    public Object createPaymentWithSuitPay(@RequestBody RequestApiPaymentDTO data) throws IOException, WriterException {
        System.out.println(data);
        return ativoPayService.createPaymentHorizon(data);
    }

    @PostMapping("/callback")
    public void callback(@RequestBody WhiteLabelCallBackDTO data) {
         System.out.println(data);
         ativoPayService.callback(data);
    }

    @GetMapping
    public List<Pagamentos> getAllPayments() {
        return paymentService.findAllPayments();
    }

    @GetMapping("/{id}")
    public Pagamentos getPaymentById(@PathVariable String id) {
        return paymentService.findPaymentById(UUID.fromString(id));
    }

    @GetMapping("/status/{status}")
    public List<Pagamentos> getPaymentsByStatus(@PathVariable StatusPagamento status) {
        return paymentService.findPaymentsByStatusPagamento(status);
    }

    @GetMapping("/transaction/{id}")
    public Pagamentos getPaymentByTransactionId(@PathVariable String id) {
        return paymentService.getPagamentoByIdTransactionSuitPay(id);
    }

    @GetMapping("/rifa/{nomeRifa}/{key}")
    public ResponseRifaValues getPaymentsByRifa(@PathVariable String nomeRifa, @PathVariable String key) {
        if (!key.equals(keySenha)) {
            throw new InvalidKeyException("SENHA INVÁLIDA");
        }
        return paymentService.findPaymentsByNomeRifa(nomeRifa);
    }

    @PostMapping("/loginteste")
    public Object loginWeGate(){
        return ativoPayService.loginInWeGate();
    }



}
