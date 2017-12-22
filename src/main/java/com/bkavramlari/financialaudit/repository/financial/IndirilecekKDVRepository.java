package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.financial.IndirilecekKDV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface IndirilecekKDVRepository extends JpaRepository<IndirilecekKDV, Long> {

    IndirilecekKDV findTop1ByUploadidAndOranAndBagenelid(Long indirilecekKDVUploadId, Long oran, Long baGenelId);

    IndirilecekKDV findTop1ByUploadid(Long indirilecekKDVUploadId);
}