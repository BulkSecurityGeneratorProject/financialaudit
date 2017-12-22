package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.config.CacheConfig;
import com.bkavramlari.financialaudit.domain.financial.BAOzel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface BAOzelRepository extends JpaRepository<BAOzel, Long> {

    @Cacheable(cacheNames = CacheConfig.ICacheNames.DAILY, unless = "#result == null")
    BAOzel findByVkNoAndUploadid(String baViewVergiNo, Long bsOzelUploadId);
}