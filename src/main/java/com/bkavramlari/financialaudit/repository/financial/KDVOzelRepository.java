package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.financial.KDVOzel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface KDVOzelRepository extends JpaRepository<KDVOzel, Long> {

    KDVOzel findByUploadid(Long kdvOzelUploadId);
}