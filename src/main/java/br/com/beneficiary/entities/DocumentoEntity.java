package br.com.recipient.entities;

import br.com.recipient.enuns.TypeDocument;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
@Table(name = "DOCUMENTO")
public class DocumentoEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_SEQ")
    @SequenceGenerator(name = "DOCUMENTO_SEQ", sequenceName = "DOCUMENTO_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private TypeDocument tipoDocumento;
    private String descricao;
    @Column(name = "beneficiario_id")
    private Long beneficiarioId;
}
