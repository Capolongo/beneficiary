package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

	private String id;
	private String firstName;
	private String lastName;
	private String gender;
	private String notes;
	private String email;
	private List<Phone> phones;
	private String birthDate;
	private String type;
	@Builder.Default
	private List<DocumentDTO> documents = new ArrayList<>();
}
