package br.com.livelo.orderflight.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CartRequest {
    private String commerceOrderId;
    private String partnerOrderId;
    private String partnerCode;
    private List<String> segmentsPartnerIds;
//    private List<Item> items;
//    private List<Pax> paxs;
}
