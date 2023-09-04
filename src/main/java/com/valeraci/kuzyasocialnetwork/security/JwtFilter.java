package com.valeraci.kuzyasocialnetwork.security;

import com.valeraci.kuzyasocialnetwork.exceptions.InvalidTokenException;
import com.valeraci.kuzyasocialnetwork.models.security.AccountDetails;
import com.valeraci.kuzyasocialnetwork.util.JwtUtil;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final UserDetailsService accountDetailsService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String token = getToken((HttpServletRequest) req);
        if (Objects.nonNull(token)) {
            if(!jwtUtil.validate(token)){
                throw new InvalidTokenException(token + " is not valid");
            }
            String login = jwtUtil.getEmail(token);
            AccountDetails user = (AccountDetails) accountDetailsService.loadUserByUsername(login);
            if(user.getUserCredential().hasActiveLock()){
                throw new LockedException("Your account is locked");
            }

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(req, res);
    }

    private String getToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (Strings.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

