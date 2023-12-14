package br.com.livelo.orderflight.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "ORDER_ITEM")
public class OrderItemEntity {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_ITEM_SEQ")
	@SequenceGenerator(name = "ORDER_ITEM_SEQ", sequenceName = "ORDER_ITEM_SEQ", allocationSize = 1)
	@Column(name = "ID")
	@Id
	private Integer id;
	
	@Column(name = "QUANTITY")
	private Integer quantity;
	
	@Column(name = "EXTERNAL_COUPON")
	private String externalCoupon;
	
	@CreationTimestamp 
	@Column(name= "CREATE_DATE")
	private LocalDateTime createDate;
	
	@UpdateTimestamp
	@Column(name = "LAST_MODIFIED_DATE")
	private LocalDateTime lastModifiedDate;
	
	
	
	
	
	

}
