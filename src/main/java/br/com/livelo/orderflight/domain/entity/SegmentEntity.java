package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SEGMENT")
@EqualsAndHashCode(callSuper = false)
public class SegmentEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEGMENT_SEQ")
    @SequenceGenerator(name = "SEGMENT_SEQ", sequenceName = "SEGMENT_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String partnerId;

    private String step;

    private Integer stops;

    private Integer flightDuration;

    private String originIata;

    private String originDescription;

    private String destinationIata;

    private String destinationDescription;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<LuggageEntity> luggages;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<CancelationRuleEntity> cancelationRules;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<ChangeRuleEntity> changeRules;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<FlightLegEntity> flightsLegs;

}
