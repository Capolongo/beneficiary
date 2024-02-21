package br.com.livelo.orderflight.domain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PROCESS_COUNTER")
public class ProcessCounterEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESS_COUNTER_SEQ")
    @SequenceGenerator(name = "PROCESS_COUNTER_SEQ", sequenceName = "PROCESS_COUNTER_SEQ", allocationSize = 1)
    private String id;
    private int count;
    private String process;
}
