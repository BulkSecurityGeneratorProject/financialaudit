package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.financial.HesapPlani;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface HesapPlaniRepository extends JpaRepository<HesapPlani, Long> {

    /**
     * @param like1
     * @param like2
     * @param like3
     * @param like4
     * @param hesapPlaniUploadId
     * @return
     */
    @Query(" SELECT hp.hesapkodu FROM HesapPlani hp WHERE  (hp.hesapkodu LIKE :like1% OR hp.hesapkodu LIKE :like2%) AND hp.hesapadi LIKE %:like3% AND hp.hesapadi NOT LIKE %:like4% AND hp.uploadid= :uploadId")
    List<String> kdvIcinHesapPlaniGetirUploadIdIle(
            @Param("like1") String like1,
            @Param("like2") String like2,
            @Param("like3") String like3,
            @Param("like4") String like4,
            @Param("uploadId") Long hesapPlaniUploadId);

    @Query(" SELECT hp.hesapkodu FROM HesapPlani hp WHERE hp.hesapkodu LIKE :like1% AND (hp.hesapadi LIKE %:like2% OR hp.hesapadi LIKE %:like3%) AND hp.uploadid= :uploadId")
    List<String> kdvIcinHesapPlaniGetirUploadIdIle2(
            @Param("like1") String like1,
            @Param("like2") String like2,
            @Param("like3") String like3,
            @Param("uploadId") Long hesapPlaniUploadId);
}