package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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
@Table(name = "ORDER_STATUS")
public class OrderStatusEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_STATUS_SEQ")
    @SequenceGenerator(name = "ORDER_STATUS_SEQ", sequenceName = "ORDER_STATUS_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String code;

    private String description;

    private String partnerCode;

    private String partnerDescription;

    @Lob
    private String partnerResponse;

    private LocalDateTime statusDate;

    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

}
