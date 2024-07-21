package br.com.recipient.controller;

import br.com.recipient.dto.BeneficiaryDTO;
import br.com.recipient.dto.request.BeneficiaryRequest;
import br.com.recipient.dto.response.MessageResponse;
import br.com.recipient.dto.response.RecipientListResponse;
import br.com.recipient.dto.response.BeneficiaryResponse;
import br.com.recipient.service.BeneficiaryService;
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
    public ResponseEntity<BeneficiaryResponse> createRecipient(@RequestBody BeneficiaryRequest request) throws Exception {
        log.info("BeneficiaryService.createRecipient() - Start - request: [{}]", request);
        var response = beneficiaryService.createRecipient(request);
        log.info("BeneficiaryService.createRecipient() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PutMapping
    public ResponseEntity<BeneficiaryDTO> updateRecipient(@RequestBody BeneficiaryRequest request) throws Exception {
        log.info("BeneficiaryService.updateRecipient() - Start - request: [{}]", request);
        var response = beneficiaryService.updateRecipient(request);
        log.info("BeneficiaryService.updateRecipient() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> deleteRecipientById(@PathVariable("id") Long id) throws Exception {
        log.info("BeneficiaryService.deleteRecipientById() - Start - id: [{}]", id);
        var response = beneficiaryService.deleteRecipientById(id);
        log.info("BeneficiaryService.deleteRecipientById() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping
    public ResponseEntity<RecipientListResponse> getRecipientAll() throws Exception {
        log.info("BeneficiaryService.getRecipientAll() - Start");
        var response = beneficiaryService.getRecipientAll();
        log.info("BeneficiaryService.getRecipientAll() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<BeneficiaryDTO> getRecipientById(@PathVariable("id") Long id) throws Exception {
        log.info("BeneficiaryService.getRecipientById() - Start - id: [{}]", id);
        var response = beneficiaryService.getRecipientById(id);
        log.info("BeneficiaryService.getRecipientById() - End - response: [{}]", response);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
