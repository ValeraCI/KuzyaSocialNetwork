package com.valeraci.kuzyasocialnetwork.services;

import com.valeraci.kuzyasocialnetwork.exceptions.UserAlreadyLockedException;
import com.valeraci.kuzyasocialnetwork.exceptions.UserNotBlockedException;
import com.valeraci.kuzyasocialnetwork.exceptions.UserNotFoundException;
import com.valeraci.kuzyasocialnetwork.models.Lock;
import com.valeraci.kuzyasocialnetwork.models.Role;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;
import com.valeraci.kuzyasocialnetwork.repositories.LockRepository;
import com.valeraci.kuzyasocialnetwork.repositories.UserCredentialRepository;
import com.valeraci.kuzyasocialnetwork.services.impl.LockServiceImpl;
import com.valeraci.kuzyasocialnetwork.util.mappers.LockMapper;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LockServiceImplTest {

    @Mock
    private LockMapper lockMapper;
    @Mock
    private UserCredentialRepository userCredentialRepository;
    @Mock
    private LockRepository lockRepository;
    @InjectMocks
    private LockServiceImpl lockService;

    @Test
    void lockUserNotFoundTest() {
        when(userCredentialRepository.findWithLocksAndRolesById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class, () -> lockService.lock(1L, null)
        );
    }

    @Test
    void lockAccessDeniedTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Set<Role> roleSet = userCredential.getRoles();
        roleSet.add(ObjectCreator.simpleRoleFactory.createRole(RoleTitle.ROLE_ADMINISTRATOR));
        userCredential.setRoles(roleSet);
        Optional<UserCredential> userCredentialOptional = Optional.of(userCredential);

        when(userCredentialRepository.findWithLocksAndRolesById(anyLong())).thenReturn(userCredentialOptional);

        assertThrows(
                AccessDeniedException.class, () -> lockService.lock(1L, null)
        );
    }

    @Test
    void lockUserAlreadyLockedTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Set<Lock> lockSet = new HashSet<>();
        lockSet.add(ObjectCreator.createLock());
        userCredential.setLocks(lockSet);

        Optional<UserCredential> userCredentialOptional = Optional.of(userCredential);

        when(userCredentialRepository.findWithLocksAndRolesById(anyLong())).thenReturn(userCredentialOptional);

        assertThrows(
                UserAlreadyLockedException.class, () -> lockService.lock(1L, null)
        );
    }

    @Test
    void lockTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Optional<UserCredential> userCredentialOptional = Optional.of(userCredential);
        Lock lock = ObjectCreator.createLock();

        when(userCredentialRepository.findWithLocksAndRolesById(anyLong())).thenReturn(userCredentialOptional);
        when(lockMapper.toEntity(anyLong(), any())).thenReturn(lock);

        lockService.lock(1L, null);

        verify(userCredentialRepository).findWithLocksAndRolesById(anyLong());
        verify(lockMapper).toEntity(anyLong(), any());
        verify(lockRepository).save(lock);
    }

    @Test
    void unlockUserNotFoundExceptionTest() {
        when(userCredentialRepository.findWithLocksAndRolesById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class, () -> lockService.unlock(1L)
        );
    }

    @Test
    void unlockUserNotBlockedExceptionTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Optional<UserCredential> userCredentialOptional = Optional.of(userCredential);

        when(userCredentialRepository.findWithLocksAndRolesById(anyLong())).thenReturn(userCredentialOptional);

        assertThrows(
                UserNotBlockedException.class, () -> lockService.unlock(1L)
        );
    }

    @Test
    void unlockTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Set<Lock> lockSet = new HashSet<>();
        Lock lock = ObjectCreator.createLock();
        lockSet.add(lock);
        userCredential.setLocks(lockSet);

        Optional<UserCredential> userCredentialOptional = Optional.of(userCredential);

        when(userCredentialRepository.findWithLocksAndRolesById(anyLong())).thenReturn(userCredentialOptional);

        lockService.unlock(1L);

        verify(lockRepository).delete(lock);
    }
}