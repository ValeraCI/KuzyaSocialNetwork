package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.Lock;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
@DataJpaTest
@ActiveProfiles("repositoryTest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LockRepositoryTest {

    @Autowired
    private LockRepository lockRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final UserCredential userCredential = ObjectCreator.createUserCredential();

    @BeforeEach
    public void userInsert() {
        entityManager.persist(userCredential);
    }

    @Test
    void saveTest() {
        Lock lock = ObjectCreator.createLock();
        lock.setUserCredential(userCredential);
        lockRepository.save(lock);

        entityManager.flush();
        entityManager.detach(lock);

        Lock readLock = entityManager.find(Lock.class, lock.getId());

        assertNotNull(readLock);
        assertEquals(lock.getReason(), readLock.getReason());
        assertEquals(lock.getDays(), readLock.getDays());
    }

    @Test
    void deleteTest() {
        Lock lock = ObjectCreator.createLock();
        lock.setUserCredential(userCredential);

        entityManager.persistAndFlush(lock);
        entityManager.detach(lock);

        lockRepository.delete(lock);

        lock = entityManager.find(Lock.class, lock.getId());

        assertNull(lock);
    }
}