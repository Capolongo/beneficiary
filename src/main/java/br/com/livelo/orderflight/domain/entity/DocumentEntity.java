package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DOCUMENT")
@EqualsAndHashCode(callSuper = false)
public class DocumentEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_SEQ")
    @SequenceGenerator(name = "DOCUMENT_SEQ", sequenceName = "DOCUMENT_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String documentNumber;
    private String type;
    private String issueDate;
    private String issuingCountry;
    private String expirationDate;
    private String residenceCountry;
}
