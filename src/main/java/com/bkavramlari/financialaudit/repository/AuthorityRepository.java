package com.bkavramlari.financialaudit.repository;

import com.bkavramlari.financialaudit.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findOneByName(String name);
}