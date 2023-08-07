package com.valeraci.kuzyasocialnetwork.controllers;

import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.services.api.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserCredentialService userCredentialService;
    @PostMapping()
    public void register(@Validated @RequestBody RegistrationDto registrationDto){
        userCredentialService.register(registrationDto);
    }
}
