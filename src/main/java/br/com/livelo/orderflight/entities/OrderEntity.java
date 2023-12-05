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
@Table(name = "ORDER")
public class OrderEntity {
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARITY_AUDIT_SEQ")
    @SequenceGenerator(name = "PARITY_AUDIT_SEQ", sequenceName = "PARITY_AUDIT_SEQ", allocationSize = 1)
    @Column(name = "ID")
	private Integer id;
	
	@Column(name = "ORDER_PRICE_ID")
	private Integer orderPriceId;
	
	@Column(name = "COMMERCE_ORDER_ID")
	private String commerceOrderId;
	
	//TODO -- verificar com relação ao token da cvc - Defini como text, pois pode haver uma quantidade não previsível para outros parceiros
	@Column(name = "PARTNER_ORDER_ID")
	private String partnerOrderId;
	
	@Column(name = "PARTNER_CODE")
	private String partnerCode;
	
	@CreationTimestamp
	@Column(name = "SUBMITTED_DATE")
	private Date submittedDate;
	
	@Column(name = "CHANNEL")
	private String channel;
	
	@Column(name = "TIER_CODE")
	private String tierCode;
	
	@Column(name = "ORIGIN_ORDER")
	private String originOrder;
	
	@Column(name = "CUSTOMER_IDENTIFIER")
	private String customerIdentifier;
	
	@CreationTimestamp
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@CreationTimestamp
	@Column(name = "LAST_MODIFIED_DATE")
	private Date lastModifiedDate;
	
	@CreationTimestamp
	@Column(name = "STATUS")
	private String status;
}
