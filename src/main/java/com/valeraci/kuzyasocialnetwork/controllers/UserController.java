package com.valeraci.kuzyasocialnetwork.controllers;

import com.valeraci.kuzyasocialnetwork.dto.locks.LockDto;
import com.valeraci.kuzyasocialnetwork.services.api.LockService;
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
public class UserController {
    private final LockService lockService;

    @PatchMapping("/lock/{id}")
    @Secured({"ROLE_OWNER", "ROLE_ADMINISTRATOR"})
    public void lock(@PathVariable("id") Long id, @Validated @RequestBody LockDto lockDto){
        lockService.lock(id, lockDto);
    }

    @DeleteMapping("/lock/{id}")
    @Secured({"ROLE_OWNER", "ROLE_ADMINISTRATOR"})
    public void unlock(@PathVariable("id") Long id){
        lockService.unlock(id);
    }
}
