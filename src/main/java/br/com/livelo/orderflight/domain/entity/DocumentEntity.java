package br.com.livelo.orderflight.entities;

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
@Table(name = "DOCUMENT")
public class DocumentEntity extends BaseEntity {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_SEQ")
	@SequenceGenerator(name = "DOCUMENT_SEQ", sequenceName = "DOCUMENT_SEQ", allocationSize = 1)
	@Id

	private Long id;

	private String documentNumber;

	private String type;

	private String issueDate;

	private String issuingCountry;

	private String expirationDate;

	private String residenceCountry;
}
