package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

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
public class FlightLegEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLIGHT_LEG_SEQ")
    @SequenceGenerator(name = "FLIGHT_LEG_SEQ", sequenceName = "FLIGHT_LEG_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String flightNumber;

    private Integer flightDuration;

    private String airline;

    private String managedBy;

    private Integer timeToWait;
    
    private String originIata;

    private String originDescription;

    private String destinationIata;

    private String destinationDescription;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private String type;
}
