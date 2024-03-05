package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateOrderRequest extends ConfirmOnPartnersResponse {
    private String partnerCode;

}
