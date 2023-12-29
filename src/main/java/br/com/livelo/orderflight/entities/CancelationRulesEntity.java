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
@Table(name = "CANCELATION_RULES")
public class CancelationRulesEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CANCELATION_RULES_SEQ")
    @SequenceGenerator(name = "CANCELATION_RULES_SEQ", sequenceName = "CANCELATION_RULES_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String description;

    private String type;
}
