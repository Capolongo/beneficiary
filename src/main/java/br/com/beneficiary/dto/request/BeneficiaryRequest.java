package br.com.recipient.dto.request;


import br.com.recipient.dto.BeneficiaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiaryRequest {
    private BeneficiaryDTO recipient;
}
