package com.valeraci.kuzyasocialnetwork.services.impl;

import com.valeraci.kuzyasocialnetwork.dto.locks.LockDto;
import com.valeraci.kuzyasocialnetwork.exceptions.UserAlreadyLockedException;
import com.valeraci.kuzyasocialnetwork.exceptions.UserNotBlockedException;
import com.valeraci.kuzyasocialnetwork.exceptions.UserNotFoundException;
import com.valeraci.kuzyasocialnetwork.models.Lock;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;
import com.valeraci.kuzyasocialnetwork.repositories.LockRepository;
import com.valeraci.kuzyasocialnetwork.repositories.UserCredentialRepository;
import com.valeraci.kuzyasocialnetwork.services.api.LockService;
import com.valeraci.kuzyasocialnetwork.util.mappers.LockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockServiceImpl implements LockService {
    private final LockMapper lockMapper;
    private final UserCredentialRepository userCredentialRepository;
    private final LockRepository lockRepository;

    @Override
    public void lock(Long userId, LockDto lockDto) {
        UserCredential userCredential =
                userCredentialRepository.findWithLocksAndRolesById(userId)
                        .orElseThrow(() -> new UserNotFoundException("user with id " + userId + " not found"));

        if(userCredential.getRoles()
                .stream()
                .anyMatch(role -> !role.getTitle().equals(RoleTitle.ROLE_USER))){
            throw new AccessDeniedException("You can only block users without a role");
        }

        if(!userCredential.hasActiveLock()) {
            lockRepository.save(lockMapper.toEntity(userId, lockDto));
        }
        else{
            throw new UserAlreadyLockedException("The user is already blocked");
        }
    }

    @Override
    public void unlock(Long userId) {
        UserCredential userCredential =
                userCredentialRepository.findWithLocksAndRolesById(userId)
                        .orElseThrow(() -> new UserNotFoundException("user with id " + userId + " not found"));


        Lock lock = userCredential.getActiveLock()
                .orElseThrow(() -> new UserNotBlockedException("The user is not blocked"));

        lockRepository.delete(lock);
    }
}
