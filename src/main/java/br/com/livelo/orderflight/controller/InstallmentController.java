package br.com.livelo.orderflight.controller;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.service.installment.InstallmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class InstallmentController {

    private final InstallmentService installmentService;

    @GetMapping("{id}/payment-options/{paymentOptionId}/installment-options")
    public ResponseEntity<InstallmentOptionsResponse> getInstallmentOptions(@PathVariable("id") final String id, @PathVariable("paymentOptionId") final String paymentOptionId){
        log.debug("InstallmentController.getInstallmentptions - start id: [{}], paymentOptionId: [{}]", id, paymentOptionId);
        InstallmentOptionsResponse installmentOptionsResponse = installmentService.getInstallmentOptions(id, paymentOptionId);
        log.debug("InstallmentController.getInstallmentptions - end installmentOptionsResponse: [{}]", installmentOptionsResponse);

        return ResponseEntity.ok().body(installmentOptionsResponse);
    }

}
