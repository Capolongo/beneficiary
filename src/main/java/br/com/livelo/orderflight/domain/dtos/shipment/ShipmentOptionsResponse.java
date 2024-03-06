package br.com.livelo.orderflight.domain.dtos.shipment;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ShipmentOptionsResponse {

    private List<ShipmentsDTO> shipmentOptions;
}
