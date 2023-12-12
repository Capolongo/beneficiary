package br.com.livelo.orderflight.entities;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_PRICE")
public class OrderPriceEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_PRICE_SEQ")
    @SequenceGenerator(name = "ORDER_PRICE_SEQ", sequenceName = "ORDER_PRICE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "ACCRUAL_POINTS")
    private Integer accuralPoints;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "POINTS_AMOUNT")
    private BigDecimal pointsAmount;
    @Column(name = "PARTNER_AMOUNT")
    private BigDecimal partnerAmount;
    @Column(name = "PRICE_LIST_ID")
    private String priceListId;
    @Column(name = "PRICE_LIST_DESCRIPTION")
    private String priceListDescription;
    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @CreationTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;

}
