package br.com.livelo.orderflight.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "ORDER_PRICE")
public class OrderPriceEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARITY_AUDIT_SEQ")
    @SequenceGenerator(name = "PARITY_AUDIT_SEQ", sequenceName = "PARITY_AUDIT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ACCRUAL_POINTS")
    private Integer accuralPoints;
    @Column(name = "POINTS_AMOUNT")
    private Number pointsAmount;
    @Column(name = "PARTNER_AMOUNT")
    private Number partnerAmount;
    @Column(name = "PRICE_LIST_ID")
    private String priceListId;
    @Column(name = "PRICE_LIST_DESCRIPTION")
    private String priceListDescription;
    @CreationTimestamp
    @Column(name = "CREATE_DATE ")
    private Date createDate;
    @CreationTimestamp
    @Column(name = "LAST_MODIFIED_DATE ")
    private Date lastModifiedDate;

}
