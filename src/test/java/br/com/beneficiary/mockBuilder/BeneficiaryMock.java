package br.com.beneficiary.mockBuilder;

import br.com.beneficiary.dto.BeneficiaryDTO;
import br.com.beneficiary.dto.DocumentDTO;
import br.com.beneficiary.dto.request.BeneficiaryRequest;
import br.com.beneficiary.dto.response.BeneficiaryResponse;
import br.com.beneficiary.entities.BeneficiarioEntity;
import br.com.beneficiary.entities.DocumentoEntity;
import br.com.beneficiary.enuns.TypeDocument;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class BeneficiaryMock {

    public static BeneficiarioEntity builderBeneficiarioEntity() {
        return BeneficiarioEntity.builder()
                .id(1L)
                .beneficiarioId(1L)
                .nome("Beneficiaro 1")
                .telefone("999999999")
                .dataNascimento("15/02/1980")
                .documentos(builderDocumentoEntitys())
                .dataInclusao(ZonedDateTime.now())
                .dataAtualizacao(ZonedDateTime.now())
                .build();
    }

    public static BeneficiaryResponse builderBeneficiaryResponse() {

        return BeneficiaryResponse.builder()
                .beneficiary(builderBeneficiary())
                .build();
    }

    public static BeneficiaryRequest builderBeneficiaryRequest() {
        return BeneficiaryRequest.builder()
                .beneficiary(builderBeneficiary())
                .build();
    }

    public static BeneficiaryDTO builderBeneficiary() {
        return BeneficiaryDTO.builder()
                .id(1L)
                .name("Beneficiaro 1")
                .telephone("999999999")
                .birthDate("15/02/1980")
                .documents(builderDocuments())
                .build();
    }

    public static Set<DocumentoEntity> builderDocumentoEntitys() {
        Set<DocumentoEntity> documentDTOS = new HashSet<>();

        documentDTOS.add(buildDocumentoEntity(DocumentoEntity.builder()
                .descricao("48.053.88804")
                .id(1L)
                .tipoDocumento(TypeDocument.RG)
                .build()));

        documentDTOS.add(buildDocumentoEntity(DocumentoEntity.builder()
                .descricao("425.4444.999-08")
                .id(2L)
                .tipoDocumento(TypeDocument.CPF)
                .build()));

        return documentDTOS;
    }


    public static Set<DocumentDTO> builderDocuments() {
        Set<DocumentDTO> documentDTOS = new HashSet<>();

        documentDTOS.add(buildDocument(DocumentDTO.builder()
                .description("48.053.88804")
                .id(1L)
                .typeDocument(TypeDocument.RG)
                .build()));

        documentDTOS.add(buildDocument(DocumentDTO.builder()
                .description("425.4444.999-08")
                .id(2L)
                .typeDocument(TypeDocument.CPF)
                .build()));

        return documentDTOS;
    }

    private static DocumentDTO buildDocument(DocumentDTO document) {

        return DocumentDTO.builder()
                .id(document.getId())
                .typeDocument(document.getTypeDocument())
                .description(document.getDescription())
                .dateInclusion(ZonedDateTime.now())
                .dateUpdate(ZonedDateTime.now())
                .build();
    }

    private static DocumentoEntity buildDocumentoEntity(DocumentoEntity document) {

        return DocumentoEntity.builder()
                .id(document.getId())
                .tipoDocumento(document.getTipoDocumento())
                .descricao(document.getDescricao())
                .beneficiarioId(document.getBeneficiarioId())
                .build();
    }


}
