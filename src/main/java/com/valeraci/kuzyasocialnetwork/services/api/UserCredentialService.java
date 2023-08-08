package com.valeraci.kuzyasocialnetwork.services.api;

import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;

public interface UserCredentialService {
    void register(RegistrationDto registrationDto);
}
