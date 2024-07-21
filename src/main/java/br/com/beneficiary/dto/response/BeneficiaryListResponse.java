package br.com.beneficiary.dto.response;

import br.com.beneficiary.dto.BeneficiaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class BeneficiaryListResponse {
    private Set<BeneficiaryDTO> beneficiaries;
}
