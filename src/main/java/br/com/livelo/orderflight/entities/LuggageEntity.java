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
@Table(name = "LUGGAGE")
public class LuggageEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LUGGAGE_SEQ")
    @SequenceGenerator(name = "LUGGAGE_SEQ", sequenceName = "LUGGAGE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SEGMENT_ID")
    private Integer segmentId;

    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;
}
