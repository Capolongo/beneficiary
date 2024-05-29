package br.com.livelo.orderflight.domain.entity;

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
@Table(name = "ORDERS_CURRENT_STATUS")
public class OrderCurrentStatusEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_CURRENT_STATUS_SEQ")
    @SequenceGenerator(name = "ORDERS_CURRENT_STATUS_SEQ", sequenceName = "ORDERS_CURRENT_STATUS_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String code;
    private String description;
    private String partnerCode;
    private String partnerDescription;
    private String partnerResponse;
}
