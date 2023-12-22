package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FLIGHT_LEG")
public class FlightLegEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLIGHT_LEG_SEQ")
    @SequenceGenerator(name = "FLIGHT_LEG_SEQ", sequenceName = "FLIGHT_LEG_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "FLIGHT_DURATION")
    private String flightDuration;

    @Column(name = "AIRLINE")
    private String airline;

    @Column(name = "MANAGED_BY")
    private String managedBy;

    @Column(name = "TIME_TO_WAIT")
    private Integer timeToWait;

    @CreationTimestamp
    @Column(name = "OPERATED_DATE")
    private LocalDateTime operatedDate;

    @Column(name = "ORIGIN_IATA")
    private String originIata;

    @Column(name = "ORIGIN_DESCRIPTION")
    private String originDescription;

    @Column(name = "DESTINATION_IATA")
    private String destinationIata;

    @Column(name = "DESTINATION_DESCRIPTION")
    private String destinationDescription;

    @CreationTimestamp
    @Column(name = "DEPARTURE_DATE")
    private LocalDateTime derpatureDate;

    @CreationTimestamp
    @Column(name = "ARRIVAL_DATE")
    private LocalDateTime arrivalDate;

    @Column(name = "TYPE")
    private String type;

    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;
}
