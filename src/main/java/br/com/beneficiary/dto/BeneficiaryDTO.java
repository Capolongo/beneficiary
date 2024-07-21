package br.com.recipient.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Set;

@Builder
@Data
public class BeneficiaryDTO {
    private Long id;
    private String name;
    private String telephone;
    private String birthDate;
    private ZonedDateTime dateInclusion;
    private ZonedDateTime dateUpdate;
    private Set<DocumentDTO> documents;

}
