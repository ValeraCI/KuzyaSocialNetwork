package org.kuzya.repositories;


import org.kuzya.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @Query("update User u set u.isActive = false where u = ?1")
    @Modifying
    void delete(User user);
    @Override
    @Query("update User u set u.isActive = false where u.id = ?1")
    @Modifying
    void deleteById(Long id);
    @Override
    @Query("update User u set u.isActive = false where u.id in ?1")
    @Modifying
    void deleteAllByIdInBatch(Iterable<Long> iterable);
    @Query("select u from User u where u.isActive = true")
    List<User> findAllNotDeleted();
    @Query("select u from User u where u.id = ?1 and u.isActive = true")
    Optional<User> findByIdNotDeleted(Long id);
    @Query("select u from User u where u.lastName = ?1 and u.isActive = true")
    List<User> findByLastNameNotDeleted(String lastName);
    @Query("select u from User u where u.firstName = ?1 and u.isActive = true")
    List<User> findByFirstNameNotDeleted(String firstName);
    @Override
    @Query("select count(*) from User u where u.isActive = true")
    long count();

    @EntityGraph(attributePaths = {"roles"})
    @Query("select u from User u where u.id = ?1 and u.isActive = true")
    Optional<User> findUserWithRolesById(Long id);
}
