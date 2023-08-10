package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.User;
import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdEntity;
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
import java.util.List;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByIdTest() {
        User user = ObjectCreator.createUser();
        assertNull(user.getId());

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        User readUser = userRepository.findById(user.getId()).orElse(null);
        entityManager.detach(readUser);

        assertNotNull(readUser);
        assertEquals(user.getLastName(), readUser.getLastName());
        assertTrue(readUser.isActive());
        assertEquals(FamilyStatusTitle.SINGLE, readUser.getFamilyStatus().getTitle());
    }

    @Test
    public void findByIdFailTest() {
        User user = ObjectCreator.createUser();
        user.setActive(false);
        assertNull(user.getId());

        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);

        User readUser = userRepository.findById(user.getId()).orElse(null);

        assertNull(readUser);
    }

    @Test
    public void findAllTest() {
        Set<User> userSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createUser(
                                "ln" + num, "fn" + num,
                                FamilyStatusTitle.SINGLE))
                .collect(Collectors.toSet());

        userSet.forEach(entityManager::persist);
        entityManager.flush();
        userSet.forEach(entityManager::detach);

        Set<User> readUserSet = userRepository.findAll();

        readUserSet.forEach(entityManager::detach);

        assertEquals(userSet.size(), readUserSet.size());
        readUserSet.forEach(user -> assertTrue(user.isActive()));
        readUserSet
                .forEach(user -> assertEquals(FamilyStatusTitle.SINGLE, user.getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllFailTest() {
        User user = ObjectCreator.createUser();
        user.setActive(false);
        assertNull(user.getId());

        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);

        Set<User> readUserSet = userRepository.findAll();

        assertEquals(0, readUserSet.size());
    }

    @Test
    public void findAllByIdTest() {
        Set<User> userSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createUser(
                                "ln" + num, "fn" + num,
                                FamilyStatusTitle.SINGLE))
                .collect(Collectors.toSet());

        userSet.forEach(entityManager::persist);
        entityManager.flush();
        userSet.forEach(entityManager::detach);


        Set<Long> usersId = userSet
                .stream()
                .map(IdEntity::getId)
                .collect(Collectors.toSet());

        Set<User> readUserSet = userRepository.findAllById(usersId);

        readUserSet.forEach(entityManager::detach);

        assertEquals(userSet.size(), readUserSet.size());
        readUserSet.forEach(user -> assertTrue(user.isActive()));
        readUserSet
                .forEach(user -> assertEquals(FamilyStatusTitle.SINGLE, user.getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllByIdFailTest() {
        User user = ObjectCreator.createUser();
        user.setActive(false);
        assertNull(user.getId());

        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);

        Set<User> readUserSet = userRepository.findAllById(Collections.singletonList(1L));

        assertEquals(0, readUserSet.size());
    }

    @Test
    public void deleteByIdTest() {
        User user = ObjectCreator.createUser();
        assertNull(user.getId());

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        userRepository.deleteById(user.getId());

        User readUser = entityManager.find(User.class, user.getId());

        assertFalse(readUser.isActive());
    }

    @Test
    public void deleteTest() {
        User user = ObjectCreator.createUser();
        assertNull(user.getId());

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        userRepository.delete(user);

        User readUser = entityManager.find(User.class, user.getId());

        assertFalse(readUser.isActive());
    }

    @Test
    public void deleteAllByIdTest() {
        Set<User> userSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createUser(
                                "ln" + num, "fn" + num,
                                FamilyStatusTitle.SINGLE))
                .collect(Collectors.toSet());

        userSet.forEach(entityManager::persist);
        entityManager.flush();
        userSet.forEach(entityManager::detach);

        Set<Long> usersId = userSet
                .stream()
                .map(IdEntity::getId)
                .collect(Collectors.toSet());

        userRepository.deleteAllById(usersId);

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        root.fetch("familyStatus");

        query
                .select(root);

        List<User> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        readlist.forEach(user -> assertFalse(user.isActive()));
    }
}
