package com.valeraci.kuzyasocialnetwork.models.security;

import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
public class AccountDetails implements UserDetails {

    private final UserCredential userCredential;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userCredential
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getTitle().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return userCredential.getPassword();
    }

    @Override
    public String getUsername() {
        return userCredential.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        LocalDateTime now = LocalDateTime.now();

        return userCredential.getLocks()
                .stream().noneMatch(lock -> lock.getBeginning().plusDays(lock.getDays()).isAfter(now));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
