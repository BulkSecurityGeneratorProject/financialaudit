package com.bkavramlari.financialaudit.repository;

import com.bkavramlari.financialaudit.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByActivationKey(String key);

    Optional<User> findOneByResetKey(String key);

    Optional<User> findOneByEmail(String mail);

    Optional<User> findOneByLogin(String currentLogin);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant minus);

}