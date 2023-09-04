package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends org.springframework.data.repository.Repository<Lock, Long>{
    Lock save(Lock lock);
    void delete(Lock lock);
}
