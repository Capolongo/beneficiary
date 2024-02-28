package br.com.livelo.orderflight.domain.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommerceItem {
    private String id;
    private String commerceItemId;
    private String partnerOrderId;
    private String partnerOrderLinkId;
}
