package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "TRAVEL_INFO")
public class TravelInfoEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAVEL_INFO_SEQ")
    @SequenceGenerator(name = "TRAVEL_INFO_SEQ", sequenceName = "TRAVEL_INFO_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private Integer type;

    private String reservationCode;

    private Integer adultQuantity;

    private Integer childQuantity;

    private Integer babyQuantity;

    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "TRAVEL_INFO_ID")
    private Set<PaxEntity> pax;

}
