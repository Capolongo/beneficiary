package br.com.recipient.dto.response;

import br.com.recipient.dto.BeneficiaryDTO;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BeneficiaryResponse {
    private BeneficiaryDTO recipient;
}
