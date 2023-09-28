package com.valeraci.kuzyasocialnetwork.controllers;

import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.services.api.AuthenticationService;
import com.valeraci.kuzyasocialnetwork.services.api.UserCredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SecurityController {
    private final UserCredentialService userCredentialService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Adding a new user to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful addition"),
            @ApiResponse(responseCode = "400", description = "Email already exists or invalid input parameters",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}
            )})

    @PostMapping("/registration")
    public void register(@Validated @RequestBody RegistrationDto registrationDto) {
        userCredentialService.register(registrationDto);
    }

    @Operation(summary = "Getting a JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid username or password",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))})
    })
    @GetMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }
}
