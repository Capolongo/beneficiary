package br.com.livelo.orderflight.domain.entity;

import br.com.livelo.orderflight.utils.StringPrefixedSequenceIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
@EqualsAndHashCode(callSuper = false)
@ToString
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
    private String transactionId;
    private LocalDateTime expirationDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_PRICE_ID")
    private OrderPriceEntity price;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID")
    private Set<OrderItemEntity> items;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID")
    private Set<OrderStatusEntity> statusHistory;
    @Transient
    private OrderStatusEntity status;

    public void setStatus(Set<OrderStatusEntity> statusHistory) {
        this.status = statusHistory.stream()
                .reduce((a, b) ->
                        a.getStatusDate().isAfter(b.getStatusDate()) ? a : b
                ).orElse(null);
    }
}
