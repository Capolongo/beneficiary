package br.com.beneficiary.dto.response;

import br.com.beneficiary.dto.BeneficiaryDTO;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BeneficiaryResponse {
    private BeneficiaryDTO beneficiary;
}
