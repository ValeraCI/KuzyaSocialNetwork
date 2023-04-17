package org.kuzya.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kuzya.models.User;
import org.kuzya.models.enums.RoleTitle;
import org.kuzya.repositories.UserRepository;
import org.kuzya.util.ObjectCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = ObjectCreator.createUser("John", "Doe");
        User user2 = ObjectCreator.createUser("Jane", "Doe");
        User user3 = ObjectCreator.createUser("Bob", "Smith");

        userRepository.saveAll(Arrays.asList(user1, user2, user3));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testDelete() {
        List<User> users = userRepository.findAll();
        userRepository.delete(users.get(0));
        Assertions.assertEquals(2, userRepository.count());
    }

    @Test
    void testDeleteById() {
        List<User> users = userRepository.findAll();
        userRepository.deleteById(users.get(0).getId());
        Assertions.assertEquals(2, userRepository.count());
    }

    @Test
    void testDeleteAllByIdInBatch() {
        List<User> users = userRepository.findAll();
        userRepository.deleteAllByIdInBatch(users.stream().map(User::getId).collect(Collectors.toList()));
        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    void testFindAllNotDeleted() {
        List<User> users = userRepository.findAllNotDeleted();
        Assertions.assertEquals(3, users.size());
    }

    @Test
    void testFindByIdNotDeleted() {
        List<User> users = userRepository.findAll();
        Optional<User> user = userRepository.findByIdNotDeleted(users.get(0).getId());
        Assertions.assertNotNull(user);
    }

    @Test
    void testFindByLastNameNotDeleted() {
        List<User> users = userRepository.findByLastNameNotDeleted("Doe");
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void testFindByFirstNameNotDeleted() {
        List<User> users = userRepository.findByFirstNameNotDeleted("Bob");
        Assertions.assertEquals(1, users.size());
    }

    @Test
    void testCount() {
        Assertions.assertEquals(3, userRepository.count());
    }

    @Test
    void testFindUserWithRolesById(){
        List<User> users = userRepository.findAll();
        Optional<User> user = userRepository.findByIdNotDeleted(users.get(0).getId());
        Assertions.assertEquals(RoleTitle.ROLE_USER, user.get().getRoles().iterator().next().getTitle());
    }
}