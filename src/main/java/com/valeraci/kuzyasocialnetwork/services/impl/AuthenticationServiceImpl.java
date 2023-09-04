package com.valeraci.kuzyasocialnetwork.services.impl;

import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.services.api.AuthenticationService;
import com.valeraci.kuzyasocialnetwork.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity<Object> login(LoginDto loginDto) {

        String username = loginDto.getEmail();
        String password = loginDto.getPassword();

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtUtil.create(authentication);

        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
