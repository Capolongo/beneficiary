package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerResponseStatus {
    private String code;
    private String description;
    private String partnerCode;
    private String partnerDescription;
}
