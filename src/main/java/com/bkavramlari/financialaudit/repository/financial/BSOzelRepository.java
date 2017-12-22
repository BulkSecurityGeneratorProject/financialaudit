package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.financial.BSOzel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface BSOzelRepository extends JpaRepository<BSOzel, Long> {

    BSOzel findByVkNoAndUploadid(String bsViewVergiNo, Long bsOzelUploadId);
}