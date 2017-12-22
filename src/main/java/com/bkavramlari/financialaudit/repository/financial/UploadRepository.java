package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileProcStats;
import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface UploadRepository extends JpaRepository<Upload, Long> {

    List<Upload> findByStatusAndUserIdOrderByIdAsc(UploadFileProcStats aNew, Long id);

    @Query("SELECT MAX(u.id) FROM Upload u WHERE u.userId = :id AND u.fileType = :fileType")
    Long getLastUploadByCurrentUser(@Param("id") Long id, @Param("fileType") UploadFileTypes fileType);
}