package br.com.livelo.orderflight.controller;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.service.option.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OptionController {

    private final OptionService optionService;

    @GetMapping("{id}/shipment-options/{shipmentOptionId}/payment-options")
    public ResponseEntity<PaymentOptionResponse> getPaymentOptions(@PathVariable String id, @PathVariable String shipmentOptionId) {
        log.info("OptionController.getPaymentOptions - start id: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        PaymentOptionResponse paymentOptions = optionService.getPaymentOptions(id, shipmentOptionId);
        log.info("OptionController.getPaymentOptions - end id: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        return ResponseEntity.ok().body(paymentOptions);
    }

    @GetMapping("{id}/shipment-options/{postalCode}")
    public ResponseEntity<?> getShipmentOptions(@PathVariable("id") final String id, @PathVariable("postalCode") final String postalCode){
        log.info("OptionController.getShipmentOptions - start id: [{}], postalCode: [{}]", id, postalCode);
        ShipmentOptionsResponse shipmentOptionsResponseDTO = optionService.getShipmentOptions(id, postalCode);
        log.info("OptionController.getShipmentOptions - end shipmentOptionsResponseDTO: [{}]", shipmentOptionsResponseDTO);
        return ResponseEntity.ok().body(shipmentOptionsResponseDTO);
    }

    @GetMapping("{id}/payment-options/{paymentOptionId}/installment-options")
    public ResponseEntity<InstallmentOptionsResponse> getInstallmentptions(@PathVariable("id") final String id, @PathVariable("paymentOptionId") final String paymentOptionId){
        log.info("OptionController.getInstallmentptions - start id: [{}], paymentOptionId: [{}]", id, paymentOptionId);
        InstallmentOptionsResponse installmentOptionsResponse = optionService.getInstallmentOptions(id, paymentOptionId);
        log.info("OptionController.getInstallmentptions - end installmentOptionsResponse: [{}]", installmentOptionsResponse);

        return ResponseEntity.ok().body(installmentOptionsResponse);
    }

}
