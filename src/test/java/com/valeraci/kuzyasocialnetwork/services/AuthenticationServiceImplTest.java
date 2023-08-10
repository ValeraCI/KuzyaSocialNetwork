package com.valeraci.kuzyasocialnetwork.services;

import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.services.impl.AuthenticationServiceImpl;
import com.valeraci.kuzyasocialnetwork.util.JwtUtil;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void loginTest() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, null);
        LoginDto loginDto = ObjectCreator.createLoginDto();
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.create(authentication)).thenReturn("token");

        ResponseEntity<Object> response = authenticationService.login(loginDto);

        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).create(authentication);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() instanceof HashMap);
        assertNotNull(((HashMap<?, ?>) response.getBody()).get("username"));
        assertNotNull(((HashMap<?, ?>) response.getBody()).get("token"));
    }
}
