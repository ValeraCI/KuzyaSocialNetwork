package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends org.springframework.data.repository.Repository<Post, Long> {
    Post save(Post entity);

    Set<Post> saveAll(Iterable<Post> entities);

    @Query("SELECT p FROM Post p JOIN FETCH p.mediaFiles mf JOIN FETCH mf.fileType WHERE p.id = :id AND p.isActive")
    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.mediaFiles mf JOIN FETCH mf.fileType WHERE p.isActive")
    Set<Post> findAll();

    @Query("SELECT p FROM Post p JOIN FETCH p.mediaFiles mf JOIN FETCH mf.fileType WHERE p.id IN :ids AND p.isActive")
    Set<Post> findAllById(Iterable<Long> ids);

    @Modifying
    @Query("UPDATE Post SET isActive = FALSE WHERE id =:id")
    void deleteById(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.isActive = FALSE WHERE p = :entity")
    void delete(Post entity);

    @Modifying
    @Query("UPDATE Post p SET p.isActive = FALSE WHERE p.id IN :ids")
    void deleteAllById(Iterable<Long> ids);
}
