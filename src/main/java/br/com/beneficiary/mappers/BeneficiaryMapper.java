package br.com.beneficiary.mappers;


import br.com.beneficiary.dto.BeneficiaryDTO;
import br.com.beneficiary.dto.DocumentDTO;
import br.com.beneficiary.dto.request.BeneficiaryRequest;
import br.com.beneficiary.dto.response.BeneficiaryResponse;
import br.com.beneficiary.entities.BeneficiarioEntity;
import br.com.beneficiary.entities.DocumentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface BeneficiaryMapper {

    @Mapping(target = "nome", source = "request.beneficiary.name")
    @Mapping(target = "telefone", source = "request.beneficiary.telephone")
    @Mapping(target = "dataNascimento", source = "request.beneficiary.birthDate")
    @Mapping(target = "documentos", expression = "java(buildDocumentToDocumentos(request.getBeneficiary().getDocuments()))")
    BeneficiarioEntity beneficiaryRequestToBeneficiarioCreate(BeneficiaryRequest request);

    @Mapping(target = "beneficiario.id", source = "request.beneficiary.id")
    @Mapping(target = "beneficiario.nome", source = "request.beneficiary.name")
    @Mapping(target = "beneficiario.telefone", source = "request.beneficiary.telephone")
    @Mapping(target = "beneficiario.dataNascimento", source = "request.beneficiary.birthDate")
    @Mapping(target = "beneficiario.documentos", expression = "java(buildDocumentToDocumentos(request.getBeneficiary().getDocuments()))")
    BeneficiarioEntity beneficiaryRequestToBeneficiarioUpdate(BeneficiaryRequest request, @MappingTarget BeneficiarioEntity beneficiario);

    @Mapping(target = "beneficiary", expression = "java(buildbeneficiaryeToBeneficiarioResponse(beneficiario))")
    BeneficiaryResponse beneficiarioEntityToBeneficiaryResponse(BeneficiarioEntity beneficiario);

    @Mapping(target = "name", source = "beneficiario.nome")
    @Mapping(target = "telephone", source = "beneficiario.telefone")
    @Mapping(target = "birthDate", source = "beneficiario.dataNascimento")
    @Mapping(target = "dateInclusion", source = "beneficiario.dataInclusao")
    @Mapping(target = "dateUpdate", source = "beneficiario.dataAtualizacao")
    @Mapping(target = "documents", expression = "java(buildDocumentsToDocumentos(beneficiario))")
    BeneficiaryDTO beneficiarioToBeneficiary(BeneficiarioEntity beneficiario);

    default Set<DocumentoEntity> buildDocumentToDocumentos(Set<DocumentDTO> documents) {
        return documents.stream().map(document -> DocumentoEntity.builder()
                .id(document.getId())
                .tipoDocumento(document.getTypeDocument())
                .descricao(document.getDescription())
                .build()).collect(Collectors.toSet());
    }

    default BeneficiaryDTO buildbeneficiaryeToBeneficiarioEntity(BeneficiarioEntity beneficiarioEntity) {
        return BeneficiaryDTO.builder()
                .id(beneficiarioEntity.getId())
                .name(beneficiarioEntity.getNome())
                .telephone(beneficiarioEntity.getTelefone())
                .dateUpdate(beneficiarioEntity.getDataAtualizacao())
                .dateInclusion(beneficiarioEntity.getDataInclusao())
                .birthDate(beneficiarioEntity.getDataNascimento())
                .build();
    }

    default BeneficiaryDTO buildbeneficiaryeToBeneficiarioResponse(BeneficiarioEntity beneficiario) {
       return BeneficiaryDTO.
                builder()
                .documents(buildDocumentoToDocumentResponse(beneficiario.getDocumentos()))
                .birthDate(beneficiario.getDataNascimento())
                .id(beneficiario.getId())
                .telephone(beneficiario.getTelefone())
                .name(beneficiario.getNome())
                .dateUpdate(beneficiario.getDataAtualizacao())
                .dateInclusion(beneficiario.getDataInclusao())
                .build();

    }

    default Set<DocumentDTO> buildDocumentoToDocumentResponse(Set<DocumentoEntity> documentos) {
        return documentos.stream().map(documento -> DocumentDTO.builder()
                .id(documento.getId())
                .description(documento.getDescricao())
                .typeDocument(documento.getTipoDocumento())
                .dateInclusion(documento.getDataInclusao())
                .dateUpdate(documento.getDataAtualizacao())
                .build()).collect(Collectors.toSet());
    }

    default Set<BeneficiaryDTO> buildbeneficiaryesToBeneficiarios(List<BeneficiarioEntity> list) {
        return list.stream().map(beneficiario ->
                BeneficiaryDTO.builder()
                        .dateInclusion(beneficiario.getDataInclusao())
                        .dateUpdate(beneficiario.getDataAtualizacao())
                        .name(beneficiario.getNome())
                        .telephone(beneficiario.getTelefone())
                        .birthDate(beneficiario.getDataNascimento())
                        .id(beneficiario.getId())
                        .documents(buildDocumentsToDocumentos(beneficiario))
                        .build()
        ).collect(Collectors.toSet());
    }

    default Set<DocumentDTO> buildDocumentsToDocumentos(BeneficiarioEntity beneficiario) {
        return beneficiario.getDocumentos().stream().map(document -> DocumentDTO.builder()
                .id(document.getId())
                .description(document.getDescricao())
                .typeDocument(document.getTipoDocumento())
                .dateInclusion(document.getDataInclusao())
                .dateUpdate(document.getDataAtualizacao())
                .build()).collect(Collectors.toSet());
    }
}
