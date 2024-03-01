package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AIRLINE")
@EqualsAndHashCode(callSuper = false)
public class AirlineEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AIRLINE_SEQ")
    @SequenceGenerator(name = "AIRLINE_SEQ", sequenceName = "AIRLINE_SEQ", allocationSize = 1)
    @Id
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AIRLINE_MANAGED_BY_ID")
    private AirlineManagedByEntity managedBy;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AIRLINE_OPERATED_BY_ID")
    private AirlineOperatedByEntity operatedBy;
}
