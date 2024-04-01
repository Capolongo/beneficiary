package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OrderBy;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;



@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS_ITEM")
@EqualsAndHashCode(callSuper = false)
@ToString
public class OrderItemEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_ITEM_SEQ")
    @SequenceGenerator(name = "ORDERS_ITEM_SEQ", sequenceName = "ORDERS_ITEM_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String commerceItemId;
    private String skuId;
    private String productId;
    private Integer quantity;
    private String externalCoupon;
    private String  partnerOrderLinkId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ITEM_PRICE_ID")
    private OrderItemPriceEntity price;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRAVEL_INFO_ID")
    private TravelInfoEntity travelInfo;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ITEM_ID")
    @OrderBy("departureDate ASC")
    private Set<SegmentEntity> segments;
}
