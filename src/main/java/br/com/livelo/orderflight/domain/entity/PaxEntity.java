package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PAX")

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
