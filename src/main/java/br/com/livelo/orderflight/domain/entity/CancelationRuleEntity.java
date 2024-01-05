package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "CANCELATION_RULE")
public class CancelationRuleEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CANCELATION_RULE_SEQ")
    @SequenceGenerator(name = "CANCELATION_RULE_SEQ", sequenceName = "CANCELATION_RULE_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String description;

    private String type;
}
