package br.com.beneficiary.service.impl;

import br.com.beneficiary.dto.BeneficiaryDTO;
import br.com.beneficiary.dto.request.BeneficiaryRequest;
import br.com.beneficiary.dto.response.BeneficiaryResponse;
import br.com.beneficiary.dto.response.MessageResponse;
import br.com.beneficiary.dto.response.BeneficiaryListResponse;
import br.com.beneficiary.entities.BeneficiarioEntity;
import br.com.beneficiary.exception.BeneficiaryException;
import br.com.beneficiary.mappers.BeneficiaryMapper;
import br.com.beneficiary.repository.BeneficiarioRepository;
import br.com.beneficiary.repository.DocumentoRepository;
import br.com.beneficiary.service.BeneficiaryService;
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
    public BeneficiaryResponse createBeneficiary(BeneficiaryRequest request) {
        try {
            log.info("BeneficiaryServiceImpl.createBeneficiary() - Start - request: [{}]", request);
            var beneficiarioEntity= beneficiaryMapper.beneficiaryRequestToBeneficiarioCreate(request);
            var response = beneficiarioRepository.save(beneficiarioEntity);
            var responseMapper = beneficiaryMapper.beneficiarioEntityToBeneficiaryResponse(response);
            log.info("BeneficiaryServiceImpl.createBeneficiary() - End - response: [{}]", responseMapper);
            return responseMapper;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.createBeneficiary() - Error", ex);
            throw new BeneficiaryException("Error create beneficiary");
        }
    }

    @Override
    public BeneficiaryListResponse getBeneficiaryAll() throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.getBeneficiaryAll() - Start");
            var responseFindAll = beneficiarioRepository.findAll();

            if(responseFindAll.isEmpty()) {
                throw new BeneficiaryException("No results when searching for beneficiaries");
            }

            var recipients = responseFindAll.stream().map(beneficiaryMapper::beneficiarioToBeneficiary).collect(Collectors.toSet());

            var response = BeneficiaryListResponse.builder()
                    .beneficiaries(recipients)
                    .build();

            log.info("BeneficiaryServiceImpl.getBeneficiaryAll() - End - response: [{}]", response);
            return response;
        } catch (BeneficiaryException ex) {
            log.error("BeneficiaryServiceImpl.getBeneficiaryAll() - Error BeneficiaryException");
            throw ex;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.getBeneficiaryAll() - Error", ex);
            throw new Exception("Error find all beneficiary", ex);
        }
    }

    @Override
    public BeneficiaryDTO getBeneficiaryById(Long id) throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.getBeneficiaryById() - Start - id: [{}]", id);
            var response = beneficiarioRepository.findById(id);

            if(response.isEmpty()) {
                throw new BeneficiaryException("Not Found By Id beneficiary");
            }

            var responseMapper = beneficiaryMapper.beneficiarioToBeneficiary(response.get());
            log.info("BeneficiaryServiceImpl.getBeneficiaryById() - End - response: [{}]", responseMapper);
            return responseMapper;
        }  catch (BeneficiaryException ex) {
            log.error("BeneficiaryServiceImpl.getBeneficiaryById() - Error BeneficiaryException");
            throw ex;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.getBeneficiaryById() - Error", ex);
            throw new Exception("Error get By beneficiary");
        }
    }

    @Override
    public BeneficiaryDTO updateBeneficiary(BeneficiaryRequest request) throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.updateBeneficiary() - Start - request: [{}]", request);
            Optional<BeneficiarioEntity> beneficiarioEntity = beneficiarioRepository.findById(request.getBeneficiary().getId());

            if(beneficiarioEntity.isEmpty()) {
                throw new BeneficiaryException("Not Found Id for update beneficiary");
            }

            var  beneficiario= beneficiaryMapper.beneficiaryRequestToBeneficiarioUpdate(request, beneficiarioEntity.get());
            var response = beneficiarioRepository.save(beneficiario);
            var responseMapper = beneficiaryMapper.beneficiarioToBeneficiary(response);
            log.info("BeneficiaryServiceImpl.updateBeneficiary() - End - response: [{}]", responseMapper);
            return responseMapper;
        }  catch (BeneficiaryException ex) {
            log.error("BeneficiaryServiceImpl.updateBeneficiary() - Error BeneficiaryException");
            throw ex;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.updateRecipient() - Error", ex);
            throw new Exception("Error updateBeneficiary");
        }
    }

    @Override
    @Transactional
    public MessageResponse deleteBeneficiaryById(Long id) throws Exception {
        try {
            log.info("BeneficiaryServiceImpl.deleteBeneficiaryById() - Start - id: [{}]", id);
            Optional<BeneficiarioEntity> beneficiarioEntity = beneficiarioRepository.findById(id);

            if(beneficiarioEntity.isEmpty()) {
                throw new BeneficiaryException("Not Found Id for delete beneficiary");
            }

            documentoRepository.deleteByBeneficiarioId(id);
            beneficiarioRepository.deleteById(id);
            log.info("BeneficiaryServiceImpl.deleteBeneficiaryById() - End");
            return new MessageResponse(HttpStatus.OK.value(), "Delete to sucess");
        } catch (BeneficiaryException ex) {
            log.error("BeneficiaryServiceImpl.deleteBeneficiaryById() - Error BeneficiaryException");
            throw ex;
        } catch (Exception ex) {
            log.error("BeneficiaryServiceImpl.deleteBeneficiaryById() - Error", ex);
            throw new Exception("Error delete beneficiary", ex);
        }
    }
}
