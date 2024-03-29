package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.User;
import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdEntity;
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
import utils.ObjectCreator;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DataJpaTest
@ActiveProfiles("repositoryTest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void saveTest() {
        User user = ObjectCreator.createUser();
        Assertions.assertNull(user.getId());

        user = userRepository.save(user);
        entityManager.flush();
        entityManager.detach(user);

        User readUser = entityManager.find(User.class, user.getId());
        entityManager.detach(readUser);

        Assertions.assertNotNull(readUser);
        Assertions.assertEquals(user.getLastName(), readUser.getLastName());
        Assertions.assertEquals(user.getFamilyStatus().getTitle(), readUser.getFamilyStatus().getTitle());
    }

    @Test
    public void saveAllTest() {
        Set<User> userSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createUser(
                                "ln" + num, "fn" + num,
                                FamilyStatusTitle.SINGLE))
                .collect(Collectors.toSet());

        userSet = userRepository.saveAll(userSet);

        entityManager.flush();
        userSet.forEach(user -> entityManager.detach(user));

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        root.fetch("familyStatus");

        query
                .select(root)
                .where(builder.equal(root.get("isActive"), true));

        List<User> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        Assertions.assertEquals(userSet.size(), readlist.size());
    }

    @Test
    public void findByIdTest() {
        User user = ObjectCreator.createUser();
        Assertions.assertNull(user.getId());

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        User readUser = userRepository.findById(user.getId()).orElse(null);
        entityManager.detach(readUser);

        Assertions.assertNotNull(readUser);
        Assertions.assertEquals(user.getLastName(), readUser.getLastName());
        Assertions.assertTrue(readUser.isActive());
        Assertions.assertEquals(FamilyStatusTitle.SINGLE, readUser.getFamilyStatus().getTitle());
    }

    @Test
    public void findByIdFailTest() {
        User user = ObjectCreator.createUser();
        user.setActive(false);
        Assertions.assertNull(user.getId());

        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);

        User readUser = userRepository.findById(user.getId()).orElse(null);

        Assertions.assertNull(readUser);
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

        Assertions.assertEquals(userSet.size(), readUserSet.size());
        readUserSet.forEach(user -> Assertions.assertTrue(user.isActive()));
        readUserSet
                .forEach(user -> Assertions.assertEquals(FamilyStatusTitle.SINGLE, user.getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllFailTest() {
        User user = ObjectCreator.createUser();
        user.setActive(false);
        Assertions.assertNull(user.getId());

        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);

        Set<User> readUserSet = userRepository.findAll();

        Assertions.assertEquals(0, readUserSet.size());
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

        Assertions.assertEquals(userSet.size(), readUserSet.size());
        readUserSet.forEach(user -> Assertions.assertTrue(user.isActive()));
        readUserSet
                .forEach(user -> Assertions.assertEquals(FamilyStatusTitle.SINGLE, user.getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllByIdFailTest() {
        User user = ObjectCreator.createUser();
        user.setActive(false);
        Assertions.assertNull(user.getId());

        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);

        Set<User> readUserSet = userRepository.findAllById(Collections.singletonList(1L));

        Assertions.assertEquals(0, readUserSet.size());
    }

    @Test
    public void deleteByIdTest() {
        User user = ObjectCreator.createUser();
        Assertions.assertNull(user.getId());

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        userRepository.deleteById(user.getId());

        User readUser = entityManager.find(User.class, user.getId());

        Assertions.assertFalse(readUser.isActive());
    }

    @Test
    public void deleteTest() {
        User user = ObjectCreator.createUser();
        Assertions.assertNull(user.getId());

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        userRepository.delete(user);

        User readUser = entityManager.find(User.class, user.getId());

        Assertions.assertFalse(readUser.isActive());
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

        readlist.forEach(user -> Assertions.assertFalse(user.isActive()));
    }
}
