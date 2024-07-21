package br.com.beneficiary.service;

import br.com.beneficiary.dto.BeneficiaryDTO;
import br.com.beneficiary.dto.request.BeneficiaryRequest;
import br.com.beneficiary.dto.response.MessageResponse;
import br.com.beneficiary.dto.response.BeneficiaryListResponse;
import br.com.beneficiary.dto.response.BeneficiaryResponse;

public interface BeneficiaryService {
    BeneficiaryResponse createBeneficiary(BeneficiaryRequest request);
    BeneficiaryListResponse getBeneficiaryAll() throws Exception;
    BeneficiaryDTO getBeneficiaryById(Long id) throws Exception;
    BeneficiaryDTO updateBeneficiary(BeneficiaryRequest request) throws Exception;
    MessageResponse deleteBeneficiaryById(Long id) throws Exception;
}
