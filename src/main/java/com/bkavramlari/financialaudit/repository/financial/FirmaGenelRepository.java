package com.bkavramlari.financialaudit.repository.financial;

import com.bkavramlari.financialaudit.domain.financial.FirmaGenel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface FirmaGenelRepository extends JpaRepository<FirmaGenel, Long> {

    /**
     * @return
     */
    @Query("SELECT DISTINCT f from FirmaGenel f")
    List<FirmaGenel> findAllDistinctByVergino();

    /**
     * @param mukellef
     * @return
     */
    FirmaGenel findTop1ByVergino(String mukellef);
}