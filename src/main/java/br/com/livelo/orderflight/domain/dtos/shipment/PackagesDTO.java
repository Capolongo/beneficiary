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
public class PackagesDTO {

    private Date deliveryDate;

    private List<String> items;

    private List<CommerceItem> commerceItems;

}
