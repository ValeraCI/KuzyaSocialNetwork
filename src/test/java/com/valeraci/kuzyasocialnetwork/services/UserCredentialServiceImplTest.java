package com.valeraci.kuzyasocialnetwork.services;

import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.exceptions.EmailAlreadyExistsException;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.repositories.UserCredentialRepository;
import com.valeraci.kuzyasocialnetwork.services.impl.UserCredentialServiceImpl;
import com.valeraci.kuzyasocialnetwork.util.mappers.UserCredentialMapper;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCredentialServiceImplTest {
    @Mock
    private UserCredentialRepository userCredentialRepository;
    @Mock
    private UserCredentialMapper userCredentialMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserCredentialServiceImpl userCredentialService;

    @Test
    public void registerSuccessTest() {
        RegistrationDto registrationDto = ObjectCreator.createRegistrationDto();
        UserCredential userCredential = ObjectCreator.createUserCredential();
        when(userCredentialRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("new encoded password");
        when(userCredentialMapper.toEntity(registrationDto)).thenReturn(userCredential);
        when(userCredentialRepository.save(userCredential)).thenReturn(userCredential);

        userCredentialService.register(registrationDto);

        verify(userCredentialRepository).existsByEmail(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userCredentialMapper).toEntity(registrationDto);
        verify(userCredentialRepository).save(userCredential);
    }

    @Test
    public void registerFailureTest() {
        RegistrationDto registrationDto = ObjectCreator.createRegistrationDto();
        when(userCredentialRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        EmailAlreadyExistsException thrown = assertThrows(
                EmailAlreadyExistsException.class, () -> userCredentialService.register(registrationDto)
        );
        assertEquals(thrown.getMessage(), registrationDto.getEmail() + " already exists");
    }
}
