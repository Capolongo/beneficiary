package br.com.beneficiary.dto;

import br.com.beneficiary.enuns.TypeDocument;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Builder
@Data
public class DocumentDTO {
    private Long id;
    private TypeDocument typeDocument;
    private String description;
    private ZonedDateTime dateInclusion;
    private ZonedDateTime dateUpdate;
}
