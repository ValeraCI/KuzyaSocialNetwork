package com.valeraci.kuzyasocialnetwork.services.api;

import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<Object> login(LoginDto loginDto);
}

