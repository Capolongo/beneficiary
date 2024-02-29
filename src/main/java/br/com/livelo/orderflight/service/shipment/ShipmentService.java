package br.com.livelo.orderflight.service.shipment;

import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
public interface ShipmentService {

    ShipmentOptionsResponse getShipmentOptions(String id, String postalCode);
}
