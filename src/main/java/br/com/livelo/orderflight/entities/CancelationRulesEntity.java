package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

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
@Table(name = "CANCELATION_RULES")
public class CancelationRulesEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CANCELATION_RULES_SEQ")
    @SequenceGenerator(name = "CANCELATION_RULES_SEQ", sequenceName = "CANCELATION_RULES_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    private String type;

    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;
}
