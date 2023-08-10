package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserCredentialRepository extends org.springframework.data.repository.Repository<UserCredential, Long> {
    UserCredential save(UserCredential entity);

    Set<UserCredential> saveAll(Iterable<UserCredential> entities);

    @Query("SELECT uc FROM UserCredential uc JOIN uc.user u WHERE uc.id = :id AND u.isActive")
    Optional<UserCredential> findById(Long id);

    @Query("SELECT uc FROM UserCredential uc JOIN uc.user u WHERE u.isActive")
    Set<UserCredential> findAll();

    @Query("SELECT uc FROM UserCredential uc JOIN uc.user u WHERE uc.id IN :ids AND u.isActive")
    Set<UserCredential> findAllById(Iterable<Long> ids);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserCredential u WHERE u.email = :email")
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserCredential u LEFT JOIN FETCH u.locks LEFT JOIN FETCH u.roles LEFT JOIN u.user us WHERE u.email = :email AND us.isActive")
    Optional<UserCredential> findByEmail(String email);
}
