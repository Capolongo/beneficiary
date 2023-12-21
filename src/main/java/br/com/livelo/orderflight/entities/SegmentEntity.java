package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;
import java.util.Set;

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
@Table(name = "SEGMENT")

public class SegmentEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEGMENT_SEQ")
    @SequenceGenerator(name = "SEGMENT_SEQ", sequenceName = "SEGMENT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "PARTNER_ID")
    private String partnerId;

    @Column(name = "STEP")
    private Integer step;

    @Column(name = "STOPS")
    private Integer stops;

    @Column(name = "FLIGHT_DURATION")
    private Integer flightDuration;

    @Column(name = "ORIGIN_IATA")
    private String originIata;

    @Column(name = "ORIGIN_DESCRIPTION")
    private String originDescription;

    @Column(name = "DESTINATION_IATA")
    private String destionationIata;

    @Column(name = "DESTINATION_DESCRIPTION")
    private String destinationDescription;

    @CreationTimestamp
    @Column(name = "DEPARTURE_DATE")
    private LocalDateTime derpatureDate;

    @CreationTimestamp
    @Column(name = "ARRIVAL_DATE")
    private LocalDateTime arrivalDate;

    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<LuggageEntity> luggage;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<CancelationRulesEntity> cancelationRules;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<ChangeRulesEntity> changeRules;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_ID")
    private Set<FlightLegEntity> flightLeg;

}
