//package br.com.beneficiary.controller;
//
//import br.com.beneficiary.dto.BeneficiaryDTO;
//import br.com.beneficiary.dto.request.BeneficiaryRequest;
//import br.com.beneficiary.dto.response.BeneficiaryResponse;
//import br.com.beneficiary.dto.response.MessageResponse;
//import br.com.beneficiary.dto.response.BeneficiaryListResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/v1/beneficiary")
//public class RecipientControllerTest {
//
//    private final RecipientService recipientService;
//
//    @PostMapping
//    public ResponseEntity<BeneficiaryResponse> createRecipient(@RequestBody BeneficiaryRequest request) throws Exception {
//        log.info("BeneficiaryService.createRecipient() - Start - request: [{}]", request);
//        var response = recipientService.createRecipient(request);
//        log.info("BeneficiaryService.createRecipient() - End - response: [{}]", response);
//        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
//    }
//
//    @PutMapping
//    public ResponseEntity<BeneficiaryDTO> updateRecipient(@RequestBody BeneficiaryRequest request) throws Exception {
//        log.info("BeneficiaryService.updateRecipient() - Start - request: [{}]", request);
//        var response = recipientService.updateRecipient(request);
//        log.info("BeneficiaryService.updateRecipient() - End - response: [{}]", response);
//        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
//    }
//
//    @DeleteMapping("{id}")
//    public ResponseEntity<MessageResponse> deleteBeneficiaryById(@PathVariable("id") Long id) throws Exception {
//        log.info("BeneficiaryService.deleteBeneficiaryById() - Start - id: [{}]", id);
//        var response = recipientService.deleteBeneficiaryById(id);
//        log.info("BeneficiaryService.deleteBeneficiaryById() - End - response: [{}]", response);
//        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<BeneficiaryListResponse> getRecipientAll() throws Exception {
//        log.info("BeneficiaryService.getRecipientAll() - Start");
//        var response = recipientService.getRecipientAll();
//        log.info("BeneficiaryService.getRecipientAll() - End - response: [{}]", response);
//        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<BeneficiaryDTO> getRecipientById(@PathVariable("id") Long id) throws Exception {
//        log.info("BeneficiaryService.getRecipientById() - Start - id: [{}]", id);
//        var response = recipientService.getRecipientById(id);
//        log.info("BeneficiaryService.getRecipientById() - End - response: [{}]", response);
//        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
//    }
//}
