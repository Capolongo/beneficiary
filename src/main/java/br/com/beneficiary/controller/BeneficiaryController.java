package br.com.beneficiary.controller;

import br.com.beneficiary.dto.BeneficiaryDTO;
import br.com.beneficiary.dto.request.BeneficiaryRequest;
import br.com.beneficiary.dto.response.MessageResponse;
import br.com.beneficiary.dto.response.BeneficiaryListResponse;
import br.com.beneficiary.dto.response.BeneficiaryResponse;
import br.com.beneficiary.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/beneficiary")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> createBeneficiary(@RequestBody BeneficiaryRequest request) throws Exception {
        log.info("BeneficiaryService.createBeneficiary() - Start - request: [{}]", request);
        var response = beneficiaryService.createBeneficiary(request);
        log.info("BeneficiaryService.createBeneficiary() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PutMapping
    public ResponseEntity<BeneficiaryDTO> updateBeneficiary(@RequestBody BeneficiaryRequest request) throws Exception {
        log.info("BeneficiaryService.updateBeneficiary() - Start - request: [{}]", request);
        var response = beneficiaryService.updateBeneficiary(request);
        log.info("BeneficiaryService.updateBeneficiary() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> deleteBeneficiaryById(@PathVariable("id") Long id) throws Exception {
        log.info("BeneficiaryService.deleteBeneficiaryById() - Start - id: [{}]", id);
        var response = beneficiaryService.deleteBeneficiaryById(id);
        log.info("BeneficiaryService.deleteBeneficiaryById() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping
    public ResponseEntity<BeneficiaryListResponse> getBeneficiaryAll() throws Exception {
        log.info("BeneficiaryService.getBeneficiaryAll() - Start");
        var response = beneficiaryService.getBeneficiaryAll();
        log.info("BeneficiaryService.getBeneficiaryAll() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<BeneficiaryDTO> getBeneficiaryById(@PathVariable("id") Long id) throws Exception {
        log.info("BeneficiaryService.getBeneficiaryById() - Start - id: [{}]", id);
        var response = beneficiaryService.getBeneficiaryById(id);
        log.info("BeneficiaryService.getBeneficiaryById() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
