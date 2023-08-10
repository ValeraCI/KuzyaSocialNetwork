package com.valeraci.kuzyasocialnetwork.controllers;

import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.services.api.AuthenticationService;
import com.valeraci.kuzyasocialnetwork.services.api.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityController {
    private final UserCredentialService userCredentialService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public void register(@Validated @RequestBody RegistrationDto registrationDto) {
        userCredentialService.register(registrationDto);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }
}
