package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("{id}/shipment-options/{shipmentOptionId}/payment-options")
    public ResponseEntity<PaymentOptionResponse> getPaymentOptions(@PathVariable String id, @PathVariable String shipmentOptionId) {
        log.debug("PaymentController.getPaymentOptions - start id: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        PaymentOptionResponse paymentOptions = paymentService.getPaymentOptions(id, shipmentOptionId);
        log.debug("PaymentController.getPaymentOptions - end id: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        return ResponseEntity.ok().body(paymentOptions);
    }

}
