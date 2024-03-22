package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LUGGAGE")
@EqualsAndHashCode(callSuper = false)
public class LuggageEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LUGGAGE_SEQ")
    @SequenceGenerator(name = "LUGGAGE_SEQ", sequenceName = "LUGGAGE_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String description;

    private String type;

    private Long quantity;
    private String measurement;
    private Double weight;
}
