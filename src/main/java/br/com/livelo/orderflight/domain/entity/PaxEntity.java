package br.com.livelo.orderflight.domain.entity;

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
@Table(name = "PAX")
@EqualsAndHashCode(callSuper = false)
public class PaxEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAX_SEQ")
    @SequenceGenerator(name = "PAX_SEQ", sequenceName = "PAX_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String type;
    private String firstName;
    private String lastName;
    private String email;
    private String areaCode;
    private String phoneNumber;
    private String gender;
    private String birthDate;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "PAX_ID")
    private Set<DocumentEntity> document;

}
