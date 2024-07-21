package br.com.beneficiary.dto.request;


import br.com.beneficiary.dto.BeneficiaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiaryRequest {
    private BeneficiaryDTO beneficiary;
}
