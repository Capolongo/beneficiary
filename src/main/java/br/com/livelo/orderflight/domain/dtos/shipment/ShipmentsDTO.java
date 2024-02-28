package br.com.livelo.orderflight.domain.dtos.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ShipmentsDTO {

    private Long id;

    private String type;

    private String description;

    private String currency;

    private Double price;

    private List<String> items;

    private List<CommerceItemDTO> commerceItems;
}
