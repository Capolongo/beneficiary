package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UpdateTimestamp;

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

    private String document;

    private String documentType;

}
