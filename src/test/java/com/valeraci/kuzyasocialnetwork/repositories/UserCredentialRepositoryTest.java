package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("repositoryTest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserCredentialRepositoryTest {
    @Autowired
    private UserCredentialRepository userCredentialRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void saveTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        assertNull(userCredential.getId());

        userCredential = userCredentialRepository.save(userCredential);

        UserCredential readUserCredential = entityManager.find(UserCredential.class, userCredential.getId());

        entityManager.detach(readUserCredential);

        assertNotNull(readUserCredential);
        assertEquals(userCredential.getEmail(), readUserCredential.getEmail());
        assertEquals(userCredential.getUser().getLastName(), readUserCredential.getUser().getLastName());
    }

    @Test
    public void saveAllTest() {
        Set<UserCredential> userCredentialSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createUserCredential("em" + num, "ps" + num,
                                "ln" + num, "fn" + num,
                                FamilyStatusTitle.SINGLE))
                .collect(Collectors.toSet());

        userCredentialSet = userCredentialRepository.saveAll(userCredentialSet);

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserCredential> query = builder.createQuery(UserCredential.class);
        Root<UserCredential> root = query.from(UserCredential.class);
        query.select(root);

        List<UserCredential> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        assertEquals(userCredentialSet.size(), readlist.size());
    }

    @Test
    public void saveAllJoinTest() {
        Set<UserCredential> userCredentialSet = new HashSet<>();

        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredentialSet.add(userCredential);

        userCredentialRepository.saveAll(userCredentialSet);

        userCredential =
                entityManager.find(UserCredential.class, userCredential.getId());

        assertEquals("Tester", userCredential.getUser().getLastName());
    }

    @Test
    public void findByIdTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        assertNull(userCredential.getId());

        entityManager.persistAndFlush(userCredential);
        entityManager.detach(userCredential);

        UserCredential readUserCredential =
                userCredentialRepository.findById(userCredential.getId()).orElse(null);
        entityManager.detach(readUserCredential);

        assertNotNull(readUserCredential);
        assertEquals(userCredential.getEmail(), readUserCredential.getEmail());
        assertEquals(userCredential.getUser().getFamilyStatus(),
                readUserCredential.getUser().getFamilyStatus());
    }

    @Test
    public void findByIdFailTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredential.getUser().setActive(false);
        assertNull(userCredential.getId());

        entityManager.persistAndFlush(userCredential);
        entityManager.detach(userCredential);

        UserCredential readUserCredential =
                userCredentialRepository.findById(userCredential.getId()).orElse(null);

        assertNull(readUserCredential);
    }

    @Test
    public void findAllTest() {
        Set<UserCredential> userCredentialSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createUserCredential("em" + num, "ps" + num,
                                "ln" + num, "fn" + num,
                                FamilyStatusTitle.SINGLE))
                .collect(Collectors.toSet());

        userCredentialSet.forEach(entityManager::persist);
        entityManager.flush();
        userCredentialSet.forEach(entityManager::detach);

        Set<UserCredential> readUserCredentialSet = userCredentialRepository.findAll();

        readUserCredentialSet.forEach(entityManager::detach);

        assertEquals(userCredentialSet.size(), readUserCredentialSet.size());
        readUserCredentialSet
                .forEach(userCredential -> assertTrue(userCredential.getUser().isActive()));
        readUserCredentialSet
                .forEach(userCredential -> assertEquals(FamilyStatusTitle.SINGLE,
                        userCredential.getUser().getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllFailTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredential.getUser().setActive(false);
        assertNull(userCredential.getId());

        entityManager.persistAndFlush(userCredential);
        entityManager.detach(userCredential);

        Set<UserCredential> readUserCredentialSet = userCredentialRepository.findAll();

        assertEquals(0, readUserCredentialSet.size());
    }

    @Test
    public void findAllByIdTest() {
        Set<UserCredential> userCredentialSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createUserCredential("em" + num, "ps" + num,
                                "ln" + num, "fn" + num,
                                FamilyStatusTitle.SINGLE))
                .collect(Collectors.toSet());

        userCredentialSet.forEach(entityManager::persist);
        entityManager.flush();
        userCredentialSet.forEach(entityManager::detach);

        Set<Long> usersId = userCredentialSet
                .stream()
                .map(UserCredential::getId)
                .collect(Collectors.toSet());

        Set<UserCredential> readUserSet = userCredentialRepository.findAllById(usersId);

        readUserSet.forEach(entityManager::detach);

        assertEquals(userCredentialSet.size(), readUserSet.size());
        readUserSet.forEach(userCredential -> assertTrue(userCredential.getUser().isActive()));
        readUserSet
                .forEach(userCredential -> assertEquals(FamilyStatusTitle.SINGLE,
                        userCredential.getUser().getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllByIdFailTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredential.getUser().setActive(false);
        assertNull(userCredential.getId());

        entityManager.persistAndFlush(userCredential);
        entityManager.detach(userCredential);

        Set<UserCredential> readUserCredentialSet = userCredentialRepository.findAllById(Collections.singletonList(1L));

        assertEquals(0, readUserCredentialSet.size());
    }

    @Test
    public void existsByEmailTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        assertNull(userCredential.getId());

        assertFalse(userCredentialRepository.existsByEmail(userCredential.getEmail()));

        entityManager.persistAndFlush(userCredential);
        entityManager.detach(userCredential);

        assertTrue(userCredentialRepository.existsByEmail(userCredential.getEmail()));
    }

    @Test
    public void findWithLocksAndRolesByEmailTest() {
        UserCredential userCredential=
                ObjectCreator.createUserCredential();

        assertNull(userCredential.getId());

        entityManager.persistAndFlush(userCredential);
        entityManager.detach(userCredential);

        UserCredential credential = userCredentialRepository
                .findWithLocksAndRolesByEmail(userCredential.getEmail()).orElse(null);
        assertNotNull(credential);
        entityManager.detach(credential);

        assertEquals(userCredential.getEmail(), credential.getEmail());
        assertEquals(userCredential.getRoles().size(), credential.getRoles().size());
        assertNotNull(credential.getLocks());
        assertEquals(userCredential.getLocks().size(), credential.getLocks().size());
    }

    @Test
    public void findWithLocksAndRolesByEmailFailTest() {
        Optional<UserCredential> userCredential =
                userCredentialRepository.findWithLocksAndRolesByEmail("soneEmail@gmail.com");
        assertNull(userCredential.orElse(null));
    }

    @Test
    public void findWithLocksAndRolesByIdTest() {
        UserCredential userCredential=
                ObjectCreator.createUserCredential();

        assertNull(userCredential.getId());

        entityManager.persistAndFlush(userCredential);
        entityManager.detach(userCredential);

        UserCredential credential = userCredentialRepository
                .findWithLocksAndRolesById(userCredential.getId()).orElse(null);
        assertNotNull(credential);
        entityManager.detach(credential);

        assertEquals(userCredential.getEmail(), credential.getEmail());
        assertEquals(userCredential.getRoles().size(), credential.getRoles().size());
        assertNotNull(credential.getLocks());
        assertEquals(userCredential.getLocks().size(), credential.getLocks().size());
    }

    @Test
    public void findWithLocksAndRolesByIdFailTest() {
        Optional<UserCredential> userCredential =
                userCredentialRepository.findWithLocksAndRolesById(-5L);
        assertNull(userCredential.orElse(null));
    }
}
