package br.com.recipient.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
        @Table(name = "BENEFICIARIO")
@EqualsAndHashCode(callSuper = false)
@ToString
public class BeneficiarioEntity extends BaseEntity {
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BENEFICIARIO_SEQ")
        @SequenceGenerator(name = "BENEFICIARIO_SEQ", sequenceName = "BENEFICIARIO_SEQ", allocationSize = 1)
        @Id
        private Long id;
        private String nome;
        private String telefone;
        private String dataNascimento;
        private Long beneficiarioId;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "beneficiario_id")
        private Set<DocumentoEntity> documentos;
}
