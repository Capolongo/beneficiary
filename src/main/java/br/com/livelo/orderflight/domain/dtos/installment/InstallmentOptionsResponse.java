package br.com.livelo.orderflight.domain.dtos.installment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstallmentOptionsResponse {
    private List<InstallmentDTO> installmentOptions;
}
