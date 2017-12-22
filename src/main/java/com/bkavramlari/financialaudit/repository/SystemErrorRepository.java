package com.bkavramlari.financialaudit.repository;

import com.bkavramlari.financialaudit.domain.error.SystemError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SystemErrorRepository extends JpaRepository<SystemError, Long> {
}
