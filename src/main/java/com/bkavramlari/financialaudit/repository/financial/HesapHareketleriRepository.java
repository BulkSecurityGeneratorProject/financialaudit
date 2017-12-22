package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.financial.Buffer;
import com.bkavramlari.financialaudit.domain.financial.HesapHareketleri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface HesapHareketleriRepository extends JpaRepository<HesapHareketleri, Long> {

    /**
     * @param uploadId
     * @param kebirKodlari
     * @return
     */
    @Query("SELECT new com.bkavramlari.financialaudit.domain.financial.Buffer(COALESCE(h.yevmiyeno, 0), h.muhasebehesapkodu, h.uploadid) FROM HesapHareketleri h WHERE h.kebirkodu IN(:kebirKodlari) AND h.uploadid = :uploadId AND COALESCE(h.yevmiyeno, 0) > 1")
    List<Buffer> getBufferValues(@Param("uploadId") Long uploadId, @Param("kebirKodlari") List<String> kebirKodlari);

    /**
     *
     * @param hesapKodu
     * @param hesapHareketleriUploadId
     * @return
     */
    @Query("SELECT SUM(COALESCE(hh.borc, 0.00)) AS borc FROM HesapHareketleri hh WHERE hh.muhasebehesapkodu= :hesapKodu AND hh.uploadid = :uploadId")
    Double muhasebehesapkoduVeUploadidIleBorclarToplaminiGetir(
            @Param("hesapKodu") String hesapKodu,
            @Param("uploadId") Long hesapHareketleriUploadId);

    /**
     * @param hesapKodu
     * @param hesapHareketleriUploadId
     * @return
     */
    @Query("SELECT SUM(COALESCE(hh.alacak, 0.00)) AS alacak FROM HesapHareketleri hh WHERE hh.muhasebehesapkodu= :hesapKodu AND hh.uploadid = :uploadId")
    Double muhasebehesapkoduVeUploadidIleAlacaklarToplaminiGetir(
            @Param("hesapKodu") String hesapKodu,
            @Param("uploadId") Long hesapHareketleriUploadId);

    /**
     * @param kebirKodlari
     * @param uploadId
     * @return
     */
    @Query("SELECT SUM(COALESCE(h.alacak, 0.00)) AS borc FROM HesapHareketleri h WHERE h.kebirkodu IN (:kebirKodlari) AND h.uploadid = :uploadId")
    Double findSumByKebirkoduAndUploadidAlacak(
            @Param("kebirKodlari") List<String> kebirKodlari,
            @Param("uploadId") Long uploadId
    );

    /**
     * @param kebirKodlari
     * @param uploadId
     * @return
     */
    @Query("SELECT SUM(COALESCE(h.borc, 0.00)) AS borc FROM HesapHareketleri h WHERE h.kebirkodu IN (:kebirKodlari) ANd COALESCE(h.yevmiyeno, 0) > 1 AND h.uploadid = :uploadId")
    Double findSumByKebirkoduAndUploadidAndYemiyenoBorc(
            @Param("kebirKodlari") List<String> kebirKodlari,
            @Param("uploadId") Long uploadId
    );

    /**
     * @param kebirKodlari
     * @param uploadId
     * @return
     */
    @Query("SELECT SUM(COALESCE(h.alacak, 0.00)) AS borc FROM HesapHareketleri h WHERE h.kebirkodu IN (:kebirKodlari) ANd COALESCE(h.yevmiyeno, 0) > 1 AND h.uploadid = :uploadId")
    Double findSumByKebirkoduAndUploadidAndYemiyenoAlacak(
            @Param("kebirKodlari") List<String> kebirKodlari,
            @Param("uploadId") Long uploadId
    );
}