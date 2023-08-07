package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        Assertions.assertNull(userCredential.getId());

        userCredential = userCredentialRepository.save(userCredential);
        entityManager.flush();
        entityManager.detach(userCredential);

        UserCredential readUserCredential = entityManager.find(UserCredential.class, userCredential.getId());

        entityManager.detach(readUserCredential);

        Assertions.assertNotNull(readUserCredential);
        Assertions.assertEquals(userCredential.getEmail(), readUserCredential.getEmail());
        Assertions.assertEquals(userCredential.getUser().getLastName(), readUserCredential.getUser().getLastName());
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

        entityManager.flush();
        userCredentialSet.forEach(entityManager::detach);

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserCredential> query = builder.createQuery(UserCredential.class);
        Root<UserCredential> root = query.from(UserCredential.class);
        query.select(root);

        List<UserCredential> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        Assertions.assertEquals(userCredentialSet.size(), readlist.size());
    }

    @Test
    public void saveAllJoinTest() {
        Set<UserCredential> userCredentialSet = new HashSet<>();

        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredentialSet.add(userCredential);

        userCredentialRepository.saveAll(userCredentialSet);

        entityManager.flush();
        entityManager.detach(userCredential);

        userCredential =
                entityManager.find(UserCredential.class, userCredential.getId());

        Assertions.assertEquals("Tester", userCredential.getUser().getLastName());
    }

    @Test
    public void findByIdTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Assertions.assertNull(userCredential.getId());

        entityManager.persist(userCredential);
        entityManager.flush();
        entityManager.detach(userCredential);

        UserCredential readUserCredential =
                userCredentialRepository.findById(userCredential.getId()).orElse(null);
        entityManager.detach(readUserCredential);

        Assertions.assertNotNull(readUserCredential);
        Assertions.assertEquals(userCredential.getEmail(), readUserCredential.getEmail());
        Assertions.assertEquals(userCredential.getUser().getFamilyStatus(),
                readUserCredential.getUser().getFamilyStatus());
    }

    @Test
    public void findByIdFailTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredential.getUser().setActive(false);
        Assertions.assertNull(userCredential.getId());

        entityManager.persist(userCredential);
        entityManager.flush();
        entityManager.detach(userCredential);

        UserCredential readUserCredential =
                userCredentialRepository.findById(userCredential.getId()).orElse(null);

        Assertions.assertNull(readUserCredential);
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

        Assertions.assertEquals(userCredentialSet.size(), readUserCredentialSet.size());
        readUserCredentialSet
                .forEach(userCredential -> Assertions.assertTrue(userCredential.getUser().isActive()));
        readUserCredentialSet
                .forEach(userCredential -> Assertions.assertEquals(FamilyStatusTitle.SINGLE,
                        userCredential.getUser().getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllFailTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredential.getUser().setActive(false);
        Assertions.assertNull(userCredential.getId());

        entityManager.persist(userCredential);
        entityManager.flush();
        entityManager.detach(userCredential);

        Set<UserCredential> readUserCredentialSet = userCredentialRepository.findAll();

        Assertions.assertEquals(0, readUserCredentialSet.size());
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

        Assertions.assertEquals(userCredentialSet.size(), readUserSet.size());
        readUserSet.forEach(userCredential -> Assertions.assertTrue(userCredential.getUser().isActive()));
        readUserSet
                .forEach(userCredential -> Assertions.assertEquals(FamilyStatusTitle.SINGLE,
                        userCredential.getUser().getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllByIdFailTest() {
        UserCredential userCredential = ObjectCreator.createUserCredential();
        userCredential.getUser().setActive(false);
        Assertions.assertNull(userCredential.getId());

        entityManager.persist(userCredential);
        entityManager.flush();
        entityManager.detach(userCredential);

        Set<UserCredential> readUserCredentialSet = userCredentialRepository.findAllById(Collections.singletonList(1L));

        Assertions.assertEquals(0, readUserCredentialSet.size());
    }

    @Test
    public void existsByEmailTest(){
        UserCredential userCredential = ObjectCreator.createUserCredential();
        Assertions.assertNull(userCredential.getId());

        Assertions.assertFalse(userCredentialRepository.existsByEmail(userCredential.getEmail()));

        entityManager.persist(userCredential);
        entityManager.flush();
        entityManager.detach(userCredential);

        Assertions.assertTrue(userCredentialRepository.existsByEmail(userCredential.getEmail()));
    }
}
