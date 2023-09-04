package com.valeraci.kuzyasocialnetwork.security;

import com.valeraci.kuzyasocialnetwork.models.Lock;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.security.AccountDetails;
import com.valeraci.kuzyasocialnetwork.repositories.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
public class RepositoryAuthenticationProvider implements AuthenticationProvider {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)  {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<UserCredential> userCredential = userCredentialRepository.findWithLocksAndRolesByEmail(username);

        if (userCredential.isPresent() &&
                passwordEncoder.matches(password, userCredential.get().getPassword())) {
            AccountDetails accountDetails = new AccountDetails(userCredential.get());

            Optional<Lock> optionalLock = accountDetails.getUserCredential().getActiveLock();

            if (optionalLock.isPresent()) {
                Lock lock = optionalLock.get();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                throw new LockedException("Your account is locked. Ending : " + lock.getEnding().format(formatter) +
                        ". Reason: " + lock.getReason());
            }
            return new UsernamePasswordAuthenticationToken(accountDetails,
                    accountDetails.getPassword(), accountDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}