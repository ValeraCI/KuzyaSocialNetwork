package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CommentRepository extends org.springframework.data.repository.Repository<Comment, Long> {
    Comment save(Comment entity);

    Set<Comment> saveAll(Iterable<Comment> entities);

    @Query("SELECT c FROM Comment c WHERE c.id = :id AND c.isActive")
    Optional<Comment> findById(Long id);

    @Query("SELECT с FROM Comment с WHERE с.isActive")
    Set<Comment> findAll();

    @Query("SELECT с FROM Comment с WHERE с.id IN :ids AND с.isActive")
    Set<Comment> findAllById(Iterable<Long> ids);

    @Modifying
    @Query("UPDATE Comment SET isActive = FALSE WHERE id =:id")
    void deleteById(Long id);

    @Modifying
    @Query("UPDATE Comment с SET с.isActive = FALSE WHERE с = :entity")
    void delete(Comment entity);

    @Modifying
    @Query("UPDATE Comment с SET с.isActive = FALSE WHERE с.id IN :ids")
    void deleteAllById(Iterable<Long> ids);
}
