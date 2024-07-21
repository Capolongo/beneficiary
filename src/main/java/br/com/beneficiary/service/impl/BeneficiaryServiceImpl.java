package br.com.recipient.service.impl;

import br.com.recipient.dto.BeneficiaryDTO;
import br.com.recipient.dto.request.BeneficiaryRequest;
import br.com.recipient.dto.response.BeneficiaryResponse;
import br.com.recipient.dto.response.MessageResponse;
import br.com.recipient.dto.response.RecipientListResponse;
import br.com.recipient.entities.BeneficiarioEntity;
import br.com.recipient.exception.BeneficiaryException;
import br.com.recipient.mappers.BeneficiaryMapper;
import br.com.recipient.repository.BeneficiarioRepository;
import br.com.recipient.repository.DocumentoRepository;
import br.com.recipient.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiarioRepository beneficiarioRepository;

    private final DocumentoRepository documentoRepository;

    private final BeneficiaryMapper beneficiaryMapper;

    @Override
    public BeneficiaryResponse createRecipient(BeneficiaryRequest request) throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.createRecipient() - Start - request: [{}]", request);
            var beneficiarioEntity= beneficiaryMapper.recipientRequestToBeneficiarioCreate(request);
            var response = beneficiarioRepository.save(beneficiarioEntity);
            var responseMapper = beneficiaryMapper.beneficiarioEntityToRecipientResponse(response);
            log.info("BeneficiaryServiceImpl.createRecipient() - End - response: [{}]", responseMapper);
            return responseMapper;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.createRecipient() - Error", ex);
            throw new Exception("Error create recipient");
        }
    }

    @Override
    public RecipientListResponse getRecipientAll() throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.getRecipientAll() - Start");
            var responseFindAll = beneficiarioRepository.findAll();

            if(responseFindAll.isEmpty()) {
                throw new BeneficiaryException("No results when searching for recipients");
            }

            var recipients = responseFindAll.stream().map(beneficiaryMapper::beneficiarioToRecipient).collect(Collectors.toSet());

            var response = RecipientListResponse.builder()
                    .recipients(recipients)
                    .build();

            log.info("BeneficiaryServiceImpl.getRecipientAll() - End - response: [{}]", response);
            return response;
        } catch (BeneficiaryException ex) {
            log.error("BeneficiaryServiceImpl.getRecipientAll() - Error RecipientException", ex);
            throw ex;
        }  catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.getRecipientAll() - Error", ex);
            throw new Exception("Error find all recipient", ex);
        }
    }

    @Override
    public BeneficiaryDTO getRecipientById(Long id) throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.getRecipientById() - Start - id: [{}]", id);
            var response = beneficiarioRepository.findById(id);

            if(response.isEmpty()) {
                throw new BeneficiaryException("Not Found By Id recipient");
            }

            var responseMapper = beneficiaryMapper.beneficiarioToRecipient(response.get());
            log.info("BeneficiaryServiceImpl.getRecipientById() - End - response: [{}]", responseMapper);
            return responseMapper;
        } catch (BeneficiaryException ex) {
            log.error("BeneficiaryServiceImpl.getRecipientById() - Error RecipientException", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.getRecipientById() - Error", ex);
            throw new Exception("Error get By recipient");
        }
    }

    @Override
    public BeneficiaryDTO updateRecipient(BeneficiaryRequest request) throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.updateRecipient() - Start - request: [{}]", request);
            Optional<BeneficiarioEntity> beneficiarioEntity = beneficiarioRepository.findById(request.getRecipient().getId());

            if(beneficiarioEntity.isEmpty()) {
                throw new BeneficiaryException("Not Found Id for update recipient");
            }

            var  beneficiario= beneficiaryMapper.recipientRequestToBeneficiarioUpdate(request, beneficiarioEntity.get());
            var response = beneficiarioRepository.save(beneficiario);
            var responseMapper = beneficiaryMapper.beneficiarioToRecipient(response);
            log.info("BeneficiaryServiceImpl.updateRecipient() - End - response: [{}]", responseMapper);
            return responseMapper;
        } catch (BeneficiaryException ex) {
            log.error("BeneficiaryServiceImpl.updateRecipient() - Error RecipientException", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.updateRecipient() - Error", ex);
            throw new Exception("Error updateRecipient");
        }
    }

    @Override
    @Transactional
    public MessageResponse deleteRecipientById(Long id) throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.deleteRecipientById() - Start - id: [{}]", id);
            Optional<BeneficiarioEntity> beneficiarioEntity = beneficiarioRepository.findById(id);

            if(beneficiarioEntity.isEmpty()) {
                throw new BeneficiaryException("Not Found Id for delete recipient");
            }

            documentoRepository.deleteByBeneficiarioId(id);
            beneficiarioRepository.deleteById(id);
            log.info("BeneficiaryServiceImpl.deleteRecipientById() - End");
            return new MessageResponse(HttpStatus.OK.value(), "Delete to sucess");
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.deleteRecipientById() - Error", ex);
            throw new Exception("Error delete recipient", ex);
        }
    }
}
