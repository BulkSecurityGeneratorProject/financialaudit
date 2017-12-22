package com.bkavramlari.financialaudit.security;

import com.bkavramlari.financialaudit.domain.security.User;
import com.bkavramlari.financialaudit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    @Inject
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        Optional<User> userFromDatabase = userRepository.findOneByLogin(login);

        /*Social ??? */
        return userFromDatabase.map(user -> {
            /*if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }*/
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(login, user.getPassword(), grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database"));
    }
}
