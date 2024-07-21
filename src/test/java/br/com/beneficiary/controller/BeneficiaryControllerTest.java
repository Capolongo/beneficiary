package br.com.beneficiary.controller;

import br.com.beneficiary.dto.BeneficiaryDTO;
import br.com.beneficiary.dto.request.BeneficiaryRequest;
import br.com.beneficiary.dto.response.MessageResponse;
import br.com.beneficiary.dto.response.BeneficiaryListResponse;
import br.com.beneficiary.dto.response.BeneficiaryResponse;
import br.com.beneficiary.service.BeneficiaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Set;

import static br.com.beneficiary.mockBuilder.BeneficiaryMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class BeneficiaryControllerTest {

    @InjectMocks
    private BeneficiaryController beneficiaryController;

    @Mock
    private BeneficiaryService beneficiaryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBeneficiary() throws Exception {
        BeneficiaryRequest request = new BeneficiaryRequest();
        BeneficiaryResponse response = builderBeneficiaryResponse();

        when(beneficiaryService.createBeneficiary(any())).thenReturn(response);

        ResponseEntity<BeneficiaryResponse> result = beneficiaryController.createBeneficiary(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testUpdateBeneficiary() throws Exception {
        BeneficiaryRequest request = new BeneficiaryRequest();

        var response = builderBeneficiary();

        when(beneficiaryService.updateBeneficiary(any(BeneficiaryRequest.class))).thenReturn(response);

        ResponseEntity<BeneficiaryDTO> result = beneficiaryController.updateBeneficiary(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testDeleteBeneficiaryById() throws Exception {
        Long id = 1L;
        MessageResponse response = new MessageResponse(HttpStatus.OK.value(), "Delete to sucess");

        when(beneficiaryService.deleteBeneficiaryById(eq(id))).thenReturn(response);

        ResponseEntity<MessageResponse> result = beneficiaryController.deleteBeneficiaryById(id);


        assertEquals(response.code(), result.getBody().code());
        assertEquals(response.description(), result.getBody().description());
    }

    @Test
    public void testGetBeneficiaryAll() throws Exception {
        BeneficiaryListResponse response = BeneficiaryListResponse.builder()
                .beneficiaries(Set.of(builderBeneficiary()))
                .build();

        when(beneficiaryService.getBeneficiaryAll()).thenReturn(response);

        ResponseEntity<BeneficiaryListResponse> result = beneficiaryController.getBeneficiaryAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetBeneficiaryById() throws Exception {
        Long id = 1L;
        BeneficiaryDTO response =  builderBeneficiary();

        when(beneficiaryService.getBeneficiaryById(eq(id))).thenReturn(response);

        ResponseEntity<BeneficiaryDTO> result = beneficiaryController.getBeneficiaryById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}
