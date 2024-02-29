package br.com.livelo.orderflight.domain.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommerceItemDTO {

    private Date deliveryDate;

    private List<CommerceItem> commerceItems;


}
