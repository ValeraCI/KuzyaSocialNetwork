package com.valeraci.kuzyasocialnetwork.services.impl;

import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.exceptions.EmailAlreadyExistsException;
import com.valeraci.kuzyasocialnetwork.repositories.UserCredentialRepository;
import com.valeraci.kuzyasocialnetwork.services.api.UserCredentialService;
import com.valeraci.kuzyasocialnetwork.util.mappers.UserCredentialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegistrationDto registrationDto) {
        if (userCredentialRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException(registrationDto.getEmail() + " already exists");
        }

        registrationDto.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        userCredentialRepository.save(userCredentialMapper.toEntity(registrationDto));
    }
}
