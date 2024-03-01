package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AIRLINE_MANAGED_BY")
@EqualsAndHashCode(callSuper = false)
public class AirlineManagedByEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AIRLINE_MANAGED_BY_SEQ")
    @SequenceGenerator(name = "AIRLINE_MANAGED_BY_SEQ", sequenceName = "AIRLINE_MANAGED_BY_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String iata;
    private String description;
}
