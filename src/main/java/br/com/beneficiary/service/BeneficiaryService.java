package br.com.recipient.service;

import br.com.recipient.dto.BeneficiaryDTO;
import br.com.recipient.dto.request.BeneficiaryRequest;
import br.com.recipient.dto.response.MessageResponse;
import br.com.recipient.dto.response.RecipientListResponse;
import br.com.recipient.dto.response.BeneficiaryResponse;

public interface BeneficiaryService {
    BeneficiaryResponse createRecipient(BeneficiaryRequest request) throws Exception;
    RecipientListResponse getRecipientAll() throws Exception;
    BeneficiaryDTO getRecipientById(Long id) throws Exception;
    BeneficiaryDTO updateRecipient(BeneficiaryRequest request) throws Exception;
    MessageResponse deleteRecipientById(Long id) throws Exception;
}
