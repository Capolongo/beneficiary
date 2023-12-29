package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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
@Table(name = "LUGGAGE")
public class LuggageEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LUGGAGE_SEQ")
    @SequenceGenerator(name = "LUGGAGE_SEQ", sequenceName = "LUGGAGE_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String description;

    private String type;

    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
