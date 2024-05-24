package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS_STATUS_HISTORY")
@EqualsAndHashCode(callSuper = false)
@ToString
public class OrderStatusHistoryEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_STATUS_SEQ")
    @SequenceGenerator(name = "ORDERS_STATUS_SEQ", sequenceName = "ORDERS_STATUS_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String code;
    private String description;
    private String partnerCode;
    private String partnerDescription;
    private String partnerResponse;
}
