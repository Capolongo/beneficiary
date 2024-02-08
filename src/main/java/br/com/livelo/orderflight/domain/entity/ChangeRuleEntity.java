package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHANGE_RULE")
@EqualsAndHashCode(callSuper = false)
public class ChangeRuleEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_RULE_SEQ")
    @SequenceGenerator(name = "CHANGE_RULE_SEQ", sequenceName = "CHANGE_RULE_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String description;

    private String type;
}
