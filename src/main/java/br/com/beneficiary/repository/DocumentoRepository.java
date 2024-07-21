package br.com.beneficiary.repository;

import br.com.beneficiary.entities.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Long> {
    void deleteByBeneficiarioId(Long beneficiarioId);
    Optional<DocumentoEntity> findByBeneficiarioId(Long beneficiarioId);
}
