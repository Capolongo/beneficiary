package br.com.beneficiary.mapper;

import br.com.beneficiary.dto.BeneficiaryDTO;
import br.com.beneficiary.dto.DocumentDTO;
import br.com.beneficiary.dto.request.BeneficiaryRequest;
import br.com.beneficiary.dto.response.BeneficiaryResponse;
import br.com.beneficiary.entities.BeneficiarioEntity;
import br.com.beneficiary.entities.DocumentoEntity;
import br.com.beneficiary.mappers.BeneficiaryMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.HashSet;
import java.util.Set;

import static br.com.beneficiary.mockBuilder.BeneficiaryMock.builderBeneficiarioEntity;
import static br.com.beneficiary.mockBuilder.BeneficiaryMock.builderBeneficiaryRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BeneficiaryMapperTest {

    BeneficiaryMapper beneficiaryMapper = Mappers.getMapper(BeneficiaryMapper.class);

    @Test
    public void testBeneficiaryRequestToBeneficiarioCreateSucess() {
        BeneficiarioEntity beneficiario = beneficiaryMapper.beneficiaryRequestToBeneficiarioCreate(builderBeneficiaryRequest());
        assertNotNull(beneficiario);
    }

    @Test
    public void testBeneficiaryRequestToBeneficiarioUpdateSucess() {
        BeneficiarioEntity response = beneficiaryMapper.beneficiaryRequestToBeneficiarioUpdate(builderBeneficiaryRequest(), builderBeneficiarioEntity());
        assertNotNull(response);
    }

    @Test
    public void testbeneficiarioEntityToBeneficiaryResponseSucess() {
        BeneficiaryResponse beneficiaryResponse = beneficiaryMapper.beneficiarioEntityToBeneficiaryResponse(builderBeneficiarioEntity());
        assertNotNull(beneficiaryResponse);
    }

    @Test
    public void testBeneficiarioToBeneficiarySucess() {
        BeneficiaryDTO beneficiaryDTO = beneficiaryMapper.beneficiarioToBeneficiary(builderBeneficiarioEntity());
        assertNotNull(beneficiaryDTO);
    }



}
