package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.service.shipment.ShipmentService;
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
public class ShipmentController {

    private final ShipmentService shipmentService;
    @GetMapping("{id}/shipment-options/{postalCode}")
    public ResponseEntity<?> getShipmentOptions(@PathVariable("id") final String id, @PathVariable("postalCode") final String postalCode){
        log.debug("ShipmentController.getShipmentOptions - start id: [{}], postalCode: [{}]", id, postalCode);
        ShipmentOptionsResponse shipmentOptionsResponseDTO = shipmentService.getShipmentOptions(id, postalCode);
        log.debug("ShipmentController.getShipmentOptions - end shipmentOptionsResponseDTO: [{}]", shipmentOptionsResponseDTO);
        return ResponseEntity.ok().body(shipmentOptionsResponseDTO);
    }

}
