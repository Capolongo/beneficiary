package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRAVEL_INFO")
@EqualsAndHashCode(callSuper = false)
public class TravelInfoEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAVEL_INFO_SEQ")
    @SequenceGenerator(name = "TRAVEL_INFO_SEQ", sequenceName = "TRAVEL_INFO_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String type;

    private String reservationCode;

    private Integer adultQuantity;

    private Integer childQuantity;

    private Integer babyQuantity;

    private String voucher;

    private String typeClass;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "TRAVEL_INFO_ID")
    private Set<PaxEntity> paxs;
}
