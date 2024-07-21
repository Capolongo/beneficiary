package br.com.beneficiary.service;

import br.com.beneficiary.exception.BeneficiaryException;
import br.com.beneficiary.mappers.BeneficiaryMapper;
import br.com.beneficiary.repository.BeneficiarioRepository;
import br.com.beneficiary.repository.DocumentoRepository;
import br.com.beneficiary.service.impl.BeneficiaryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static br.com.beneficiary.mockBuilder.BeneficiaryMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryServiceTest {

    @Mock
    private BeneficiarioRepository beneficiarioRepository;
    @Mock
    private DocumentoRepository documentoRepository;
    @Mock
    private BeneficiaryMapper mapper;
    @InjectMocks
    private BeneficiaryServiceImpl service;

    @Test
    void shouldCreateSuccessfully() throws Exception {

        var beneficiarioEntity = builderBeneficiarioEntity();

        when(mapper.beneficiaryRequestToBeneficiarioCreate(any())).thenReturn(beneficiarioEntity);

        beneficiarioEntity.setId(1L);
        when(beneficiarioRepository.save(any())).thenReturn(beneficiarioEntity);

        when(mapper.beneficiarioEntityToBeneficiaryResponse(any())).thenReturn(builderBeneficiaryResponse());

        var response = service.createBeneficiary(builderBeneficiaryRequest());

        assertEquals(beneficiarioEntity.getTelefone(), response.getBeneficiary().getTelephone());
        assertEquals(beneficiarioEntity.getDataNascimento(), response.getBeneficiary().getBirthDate());
        assertEquals(beneficiarioEntity.getNome(), response.getBeneficiary().getName());
        assertEquals(beneficiarioEntity.getId(), response.getBeneficiary().getId());
        assertEquals(beneficiarioEntity.getDocumentos().size(), response.getBeneficiary().getDocuments().size());
    }

    @Test
    void shouldCreateErrorBeneficiaryException() {

        var beneficiarioEntity = builderBeneficiarioEntity();

        when(mapper.beneficiaryRequestToBeneficiarioCreate(any())).thenThrow(new RuntimeException("Error message"));
        assertThrows(BeneficiaryException.class, () -> {
            service.createBeneficiary(builderBeneficiaryRequest());
        });
    }

    @Test
    void shouldUpdateSuccessfully() throws Exception {

        var beneficiarioEntity = builderBeneficiarioEntity();

        var optionFindAll =Optional.of(beneficiarioEntity);

        when(beneficiarioRepository.findById(any())).thenReturn(optionFindAll);

        when(mapper.beneficiaryRequestToBeneficiarioUpdate(any(), any())).thenReturn(beneficiarioEntity);

        beneficiarioEntity.setId(1L);
        when(beneficiarioRepository.save(any())).thenReturn(beneficiarioEntity);

        when(mapper.beneficiarioToBeneficiary(any())).thenReturn(builderBeneficiary());

        var response = service.updateBeneficiary(builderBeneficiaryRequest());

        assertEquals(beneficiarioEntity.getTelefone(), response.getTelephone());
        assertEquals(beneficiarioEntity.getDataNascimento(), response.getBirthDate());
        assertEquals(beneficiarioEntity.getNome(), response.getName());
        assertEquals(beneficiarioEntity.getId(), response.getId());
        assertEquals(beneficiarioEntity.getDocumentos().size(), response.getDocuments().size());
    }

    @Test
    void shouldUpdateErrorException() {
        when(beneficiarioRepository.findById(any())).thenThrow(new RuntimeException("Error"));
        assertThrows(Exception.class, () -> {
            service.updateBeneficiary(builderBeneficiaryRequest());
        });
    }

    @Test
    void shouldUpdateErrorBeneficiaryException() {
        when(beneficiarioRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BeneficiaryException.class, () -> {
            service.updateBeneficiary(builderBeneficiaryRequest());
        });
    }

    @Test
    void shouldDeleteSuccessfully() throws Exception {

        var beneficiarioEntity = builderBeneficiarioEntity();

        var optionFindAll =Optional.of(beneficiarioEntity);

        when(beneficiarioRepository.findById(any())).thenReturn(optionFindAll);

        doNothing().when(documentoRepository).deleteByBeneficiarioId(1L);

        doNothing().when(beneficiarioRepository).deleteById(1L);

        var response = service.deleteBeneficiaryById(1L);

        assertEquals("Delete to sucess", response.description());
        assertEquals(HttpStatus.OK.value(), response.code());
    }

    @Test
    void shouldDeleteErrorException() {
        when(beneficiarioRepository.findById(any())).thenThrow(new RuntimeException("Error"));
        assertThrows(Exception.class, () -> {
            service.deleteBeneficiaryById(1L);
        });
    }

    @Test
    void shouldDeleteErrorBeneficiaryException() {
        when(beneficiarioRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BeneficiaryException.class, () -> {
            service.deleteBeneficiaryById(1L);
        });
    }

    @Test
    void shouldGetByIdSuccessfully() throws Exception {

        var beneficiarioEntity = builderBeneficiarioEntity();

        var optionFindAll =Optional.of(beneficiarioEntity);

        when(beneficiarioRepository.findById(any())).thenReturn(optionFindAll);

        when(mapper.beneficiarioToBeneficiary(any())).thenReturn(builderBeneficiary());

        var response = service.getBeneficiaryById(1L);

        assertEquals(beneficiarioEntity.getTelefone(), response.getTelephone());
        assertEquals(beneficiarioEntity.getDataNascimento(), response.getBirthDate());
        assertEquals(beneficiarioEntity.getNome(), response.getName());
        assertEquals(beneficiarioEntity.getId(), response.getId());
        assertEquals(beneficiarioEntity.getDocumentos().size(), response.getDocuments().size());
    }

    @Test
    void shouldGetByIdErrorException() {
        when(beneficiarioRepository.findById(any())).thenThrow(new RuntimeException("Error"));
        assertThrows(Exception.class, () -> {
            service.getBeneficiaryById(1L);
        });
    }

    @Test
    void shouldGetByIdErrorBeneficiaryException() {
        when(beneficiarioRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BeneficiaryException.class, () -> {
            service.getBeneficiaryById(1L);
        });
    }

    @Test
    void shouldGetAllSuccessfully() throws Exception {

        var beneficiarioEntity = builderBeneficiarioEntity();

        when(beneficiarioRepository.findAll()).thenReturn(Arrays.asList(beneficiarioEntity));

        when(mapper.beneficiarioToBeneficiary(any())).thenReturn(builderBeneficiary());

        var response = service.getBeneficiaryAll();

        assertEquals(1, response.getBeneficiaries().size());
    }

    @Test
    void shouldGetAllErrorException() {
        when(beneficiarioRepository.findAll()).thenThrow(new RuntimeException("Error"));
        assertThrows(Exception.class, () -> {
            service.getBeneficiaryAll();
        });
    }

    @Test
    void shouldGetAllErrorBeneficiaryException() {
        when(beneficiarioRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(BeneficiaryException.class, () -> {
            service.getBeneficiaryAll();
        });
    }
}
