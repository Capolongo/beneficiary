package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AIRLINE_OPERATED_BY")
@EqualsAndHashCode(callSuper = false)
public class AirlineOperatedByEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AIRLINE_OPERATED_BY_SEQ")
    @SequenceGenerator(name = "AIRLINE_OPERATED_BY_SEQ", sequenceName = "AIRLINE_OPERATED_BY_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String iata;
    private String description;
}
