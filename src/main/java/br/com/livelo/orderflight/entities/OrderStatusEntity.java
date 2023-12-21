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
@Table(name = "ORDER_STATUS")
public class OrderStatusEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_STATUS_SEQ")
    @SequenceGenerator(name = "ORDER_STATUS_SEQ", sequenceName = "ORDER_STATUS_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARTNER_CODE")
    private String partnerCode;

    @Column(name = "PARTNER_DESCRIPTION")
    private String partnerDescription;

    @Lob
    @Column(name = "PARTNER_RESPONSE")
    private String partnerResponse;

    @CreationTimestamp
    @Column(name = "STATUS_DATE")
    private LocalDateTime statusDate;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

}
