package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusRequest;
import br.com.livelo.orderflight.service.status.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderStatusController {

    private final StatusService statusService;
    @PostMapping("{id}/status")
    public ResponseEntity<ConfirmOrderResponse> updateStatusOrder(@PathVariable("id") String id, @RequestBody UpdateStatusRequest request) {
        log.info("OrderStatusController.updateStatusOrder() - Start - id: [{}], body: [{}]", id, request);
        var confirmOrderResponse = statusService.updateStatus(id, request);

        log.info("OrderStatusController.updateStatusOrder() - End - response: [{}]", confirmOrderResponse);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(confirmOrderResponse);
    }
}
