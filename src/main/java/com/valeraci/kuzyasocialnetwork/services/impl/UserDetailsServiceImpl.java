package com.valeraci.kuzyasocialnetwork.services.impl;

import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.security.AccountDetails;
import com.valeraci.kuzyasocialnetwork.repositories.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredential user = repository.findWithLocksAndRolesByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));

        return new AccountDetails(user);
    }

}
