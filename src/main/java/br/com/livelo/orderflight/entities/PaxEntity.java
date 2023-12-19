package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
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

public class PaxEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAX_SEQ")
    @SequenceGenerator(name = "PAX_SEQ", sequenceName = "PAX_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "AREA_CODE")
    private String areaCode;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "GENDER")
    private String gender;

    @CreationTimestamp
    @Column(name = "BIRTH_DATE")
    private LocalDateTime birthDate;

    @Column(name = "DOCUMENT")
    private String document;

    @Column(name = "DOCUMENT_TYPE")
    private String documentType;

    @Column(name = "TRAVEL_INFO_ID")
    private String travelInfoId;
    
    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

}
