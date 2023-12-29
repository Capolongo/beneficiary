package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.livelo.orderflight.utils.StringPrefixedSequenceIdGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "ORDERS")
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_SEQ")
    @GenericGenerator(name = "ORDERS_SEQ", strategy = "br.com.livelo.orderflight.utils.StringPrefixedSequenceIdGenerator", parameters = {
            @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "lf"),
            @Parameter(name = "increment_size", value = "1")})
    private String id;

    private String commerceOrderId;

    private String partnerOrderId;

    private String partnerCode;

    private LocalDateTime submittedDate;

    private String channel;

    private String tierCode;

    private String originOrder;

    private String customerIdentifier;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_PRICE_ID")
    private OrderPriceEntity price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID")
    private Set<OrderItemEntity> items;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID")
    private Set<OrderStatusEntity> statusHistory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STATUS")
    private OrderStatusEntity currentStatus;

    private String transactionId;

    private LocalDateTime expirationDate;

}
