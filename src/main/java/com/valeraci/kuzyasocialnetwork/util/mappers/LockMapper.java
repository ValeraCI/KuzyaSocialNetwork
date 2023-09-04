package com.valeraci.kuzyasocialnetwork.util.mappers;

import com.valeraci.kuzyasocialnetwork.dto.locks.LockDto;
import com.valeraci.kuzyasocialnetwork.models.Lock;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LockMapper {
    private final ModelMapper mapper;

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(LockDto.class, Lock.class)
                .setPostConverter(
                        lockDtoToLock()
                );
    }

    private Converter<LockDto, Lock> lockDtoToLock() {
        return context -> {
            Lock destination = context.getDestination();
            mapLoginRequestSpecificFields(destination);
            return context.getDestination();
        };
    }

    public void mapLoginRequestSpecificFields(Lock destination) {
        destination.setBeginning(LocalDateTime.now());
    }

    public Lock toEntity(long userId, LockDto lockDto) {
        UserCredential userCredential = new UserCredential();
        userCredential.setId(userId);

        Lock lock = mapper.map(lockDto, Lock.class);
        lock.setUserCredential(userCredential);

        return lock;
    }
}
