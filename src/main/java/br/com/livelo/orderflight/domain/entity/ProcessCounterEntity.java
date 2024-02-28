package br.com.livelo.orderflight.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PROCESS_COUNTER")
public class ProcessCounterEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESS_COUNTER_SEQ")
    @SequenceGenerator(name = "PROCESS_COUNTER_SEQ", sequenceName = "PROCESS_COUNTER_SEQ", allocationSize = 1)
    private int id;
    private int count;
    private String process;
    private String orderId;
    private ZonedDateTime createDate;
    private ZonedDateTime lastModifiedDate;
}
