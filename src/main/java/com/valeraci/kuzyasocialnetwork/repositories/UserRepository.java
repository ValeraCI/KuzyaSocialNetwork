package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends org.springframework.data.repository.Repository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.familyStatus WHERE u.id = :id AND u.isActive")
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u JOIN FETCH u.familyStatus WHERE u.isActive")
    Set<User> findAll();

    @Query("SELECT u FROM User u JOIN FETCH u.familyStatus WHERE u.id IN :ids AND u.isActive")
    Set<User> findAllById(Iterable<Long> ids);

    @Modifying
    @Query("UPDATE User SET isActive = FALSE WHERE id =:id")
    void deleteById(Long id);

    @Modifying
    @Query("UPDATE User u SET u.isActive = FALSE WHERE u = :entity")
    void delete(User entity);

    @Modifying
    @Query("UPDATE User u SET u.isActive = FALSE WHERE u.id IN :ids")
    void deleteAllById(Iterable<Long> ids);
}
