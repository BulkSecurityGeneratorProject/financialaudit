package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.financial.TevkifatTablosu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface TevkifatTablosuRepository extends JpaRepository<TevkifatTablosu, Long> {

    TevkifatTablosu findTop1ByUploadid(Long indirilecekKDVUploadId);

    TevkifatTablosu findTop1ByUploadidAndOranAndBagenelid(Long indirilecekKDVUploadId, Long oran, Long bagenelid);
}