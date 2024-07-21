package br.com.recipient.repository;

import br.com.recipient.entities.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Long> {
    Optional<DocumentoEntity> deleteByBeneficiarioId(Long beneficiarioId);
    Optional<DocumentoEntity> findByBeneficiarioId(Long beneficiarioId);
}
