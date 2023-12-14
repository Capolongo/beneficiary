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
@Table(name = "ORDER_ITEM")
public class OrderItemEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_ITEM_SEQ")
    @SequenceGenerator(name = "ORDER_ITEM_SEQ", sequenceName = "ORDER_ITEM_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;
    @Column(name = "ORDER_ID")
    private String orderId;
    @Column(name = "TRAVEL_INFO_ID")
    private Integer travelInfoId;
    @Column(name = "COMMERCE_ITEM_ID")
    private String commerceItemId;
    @Column(name = "SKU_ID")
    private String skuId;
    @Column(name = "PRODUCT_ID")
    private String productId;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "EXTERNAL_COUPON")
    private String externalCoupon;
    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "ORDER_ITEM_PRICE_ID", referencedColumnName = "ID"),
    })
    private OrderItemPriceEntity orderItemPriceEntity;
}
