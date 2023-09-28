package com.valeraci.kuzyasocialnetwork.controllers;

import com.valeraci.kuzyasocialnetwork.dto.locks.LockDto;
import com.valeraci.kuzyasocialnetwork.services.api.LockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource",
        content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))})
public class UserController {
    private final LockService lockService;

    @Operation(summary = "Blocking a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful blocking"),
            @ApiResponse(responseCode = "400", description = "Invalid user id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "409", description = "The user is already blocked",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))} )
    })
    @PatchMapping("/lock/{id}")
    @Secured({"ROLE_OWNER", "ROLE_ADMINISTRATOR"})
    public void lock(@Parameter(description = "id of the user who should be locked") @PathVariable("id") Long id,
                     @Validated @RequestBody LockDto lockDto){
        lockService.lock(id, lockDto);
    }

    @Operation(summary = "Unlocking a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful unlocking"),
            @ApiResponse(responseCode = "400", description = "Invalid user id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "409", description = "The user is not blocked",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))} )
    })
    @DeleteMapping("/lock/{id}")
    @Secured({"ROLE_OWNER", "ROLE_ADMINISTRATOR"})
    public void unlock(@Parameter(description = "id of the user who should be unlocked") @PathVariable("id") Long id){
        lockService.unlock(id);
    }
}
