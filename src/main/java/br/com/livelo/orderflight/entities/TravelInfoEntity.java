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
@Table(name = "TRAVEL_INFO")
public class TravelInfoEntity {

	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAVEL_INFO_SEQ")
	    @SequenceGenerator(name = "TRAVEL_INFO_SEQ", sequenceName = "TRAVEL_INFO_SEQ", allocationSize = 1)
	    @Column(name = "ID")
	    @Id
	    private Integer id;

	    @Column(name = "TYPE")
	    private Integer type;
	    
	    @Column(name = "RESERVATION_CODE")
	    private String reservationCode;
	    
	    @Column(name = "ADULT_QUANTITY")
	    private Integer adultQuantity;
	    
	    @Column(name = "CHILD_QUANTITY")
	    private Integer childQuantity;
	    
	    @Column(name = "BABY_QUANTITY")
	    private Integer babyQuantity;
	    
	    @CreationTimestamp
	    @Column(name = "CREATE_DATE")
	    private LocalDateTime createDate;
	    
	    @UpdateTimestamp
	    @Column(name = "LAST_MODIFIED_DATE")
	    private LocalDateTime lastModifiedDate;
	    
	    
}
