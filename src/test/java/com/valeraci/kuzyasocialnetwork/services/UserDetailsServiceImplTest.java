package com.valeraci.kuzyasocialnetwork.services;

import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.repositories.UserCredentialRepository;
import com.valeraci.kuzyasocialnetwork.services.impl.UserDetailsServiceImpl;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @Mock
    private UserCredentialRepository repository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void loadUserByUsernameTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Optional<UserCredential> optionalUserCredential = Optional.of(userCredential);
        when(repository.findByEmail(anyString())).thenReturn(optionalUserCredential);

        UserDetails accountDetails = userDetailsServiceImpl.loadUserByUsername("");

        assertEquals(userCredential.getEmail(), accountDetails.getUsername());
        assertEquals(userCredential.getPassword(), accountDetails.getPassword());
    }

    @Test
    public void loadUserByUsernameFailTest() {
        Optional<UserCredential> optionalUserCredential = Optional.empty();
        when(repository.findByEmail(anyString())).thenReturn(optionalUserCredential);

        assertThrows(
                UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername("")
        );
    }
}
