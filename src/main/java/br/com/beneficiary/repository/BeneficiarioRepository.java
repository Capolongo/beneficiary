package br.com.beneficiary.repository;

import br.com.beneficiary.entities.BeneficiarioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiarioRepository extends JpaRepository<BeneficiarioEntity, Long> {

    @EntityGraph(attributePaths = {"documentos"})
    List<BeneficiarioEntity> findAll();
}
