package br.com.livelo.orderflight.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_PRICE_DESCRIPTION")
public class OrderPriceDescriptionEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_PRICE_DESCRIPTION_SEQ")
    @SequenceGenerator(name = "ORDER_PRICE_DESCRIPTION_SEQ", sequenceName = "ORDER_PRICE_DESCRIPTION_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "POINTS_AMOUNT")
    private BigDecimal pointsAmount;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_PRICE_ID")
    private Integer orderPriceId;

    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;
}
