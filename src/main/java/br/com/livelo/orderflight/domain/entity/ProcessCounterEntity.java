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
    @Column(name = "ORDER_ID")
    private String orderId;
    @Column(name = "CREATE_DATE")
    private ZonedDateTime createDate;
    @Column(name = "LAST_MODIFIED_DATE")
    private ZonedDateTime lastModifiedDate;
}
