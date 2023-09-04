package com.valeraci.kuzyasocialnetwork.services.api;

import com.valeraci.kuzyasocialnetwork.dto.locks.LockDto;

public interface LockService {
    void lock(Long userId, LockDto lockDto);

    void unlock(Long userId);
}
