package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.Post;
import com.valeraci.kuzyasocialnetwork.models.User;
import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdEntity;
import com.valeraci.kuzyasocialnetwork.models.enums.FileTypeTitle;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final User user = ObjectCreator.createUser();

    @BeforeEach
    public void userInsert() {
        entityManager.persist(user);
    }

    @Test
    public void saveTest() {
        Post post = ObjectCreator.createPostWithMediaFiles(user);
        Assertions.assertNull(post.getId());

        post = postRepository.save(post);
        entityManager.flush();
        entityManager.detach(post);

        Post readPost = entityManager.find(Post.class, post.getId());

        Assertions.assertNotNull(readPost);
        Assertions.assertEquals(post.getTitle(), readPost.getTitle());
        Assertions.assertEquals(post.getMediaFiles().iterator().next().getPath(),
                readPost.getMediaFiles().iterator().next().getPath());
    }

    @Test
    public void saveAllTest() {
        Set<Post> postSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createPostWithMediaFiles(user,
                                "tt" + num, "tx" + num, "pt" + num))
                .collect(Collectors.toSet());

        postSet = postRepository.saveAll(postSet);

        entityManager.flush();
        postSet.forEach(post -> entityManager.detach(post));

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);

        root.fetch("mediaFiles");

        query
                .select(root)
                .where(builder.equal(root.get("isActive"), true));

        List<Post> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        Assertions.assertEquals(postSet.size(), readlist.size());
    }

    @Test
    public void findByIdTest() {
        Post post = ObjectCreator.createPostWithMediaFiles(user);
        Assertions.assertNull(post.getId());

        entityManager.persistAndFlush(post);
        entityManager.detach(post);

        Post readPost = postRepository.findById(post.getId()).orElse(null);
        entityManager.detach(readPost);

        Assertions.assertNotNull(readPost);
        Assertions.assertEquals(post.getTitle(), readPost.getTitle());
        Assertions.assertTrue(readPost.isActive());
        Assertions.assertEquals(FileTypeTitle.MP4,
                readPost.getMediaFiles().iterator().next().getFileType().getTitle());
    }

    @Test
    public void findByIdFailTest() {
        Post post = ObjectCreator.createPost(user);
        post.setActive(false);
        Assertions.assertNull(post.getId());

        entityManager.persist(post);
        entityManager.flush();
        entityManager.detach(post);

        Post readPost = postRepository.findById(post.getId()).orElse(null);

        Assertions.assertNull(readPost);
    }

    @Test
    public void findAllTest() {
        Set<Post> postSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createPostWithMediaFiles(user,
                                "tt" + num, "tx" + num, "pt" + num))
                .collect(Collectors.toSet());

        postSet.forEach(entityManager::persist);
        entityManager.flush();
        postSet.forEach(entityManager::detach);

        Set<Post> readPostSet = postRepository.findAll();

        readPostSet.forEach(entityManager::detach);

        Assertions.assertEquals(postSet.size(), readPostSet.size());
        readPostSet.forEach(post -> Assertions.assertTrue(post.isActive()));
        readPostSet
                .forEach(post -> Assertions.assertEquals(FileTypeTitle.MP4,
                        post.getMediaFiles().iterator().next().getFileType().getTitle()));
    }

    @Test
    public void findAllFailTest() {
        Post post = ObjectCreator.createPost(user);
        post.setActive(false);
        Assertions.assertNull(post.getId());

        entityManager.persist(post);
        entityManager.flush();
        entityManager.detach(post);

        Set<Post> readPostSet = postRepository.findAll();

        Assertions.assertEquals(0, readPostSet.size());
    }

    @Test
    public void findAllByIdTest() {
        Set<Post> postSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createPostWithMediaFiles(user,
                                "tt" + num, "tx" + num, "pt" + num))
                .collect(Collectors.toSet());

        postSet.forEach(entityManager::persist);
        entityManager.flush();
        postSet.forEach(entityManager::detach);


        Set<Long> postsId = postSet
                .stream()
                .map(IdEntity::getId)
                .collect(Collectors.toSet());

        Set<Post> readPostSet = postRepository.findAllById(postsId);

        readPostSet.forEach(entityManager::detach);

        Assertions.assertEquals(postSet.size(), readPostSet.size());
        readPostSet.forEach(post -> Assertions.assertTrue(post.isActive()));
        readPostSet
                .forEach(post -> Assertions.assertEquals(FileTypeTitle.MP4,
                        post.getMediaFiles().iterator().next().getFileType().getTitle()));
    }

    @Test
    public void findAllByIdFailTest() {
        Post post = ObjectCreator.createPost(user);
        post.setActive(false);
        Assertions.assertNull(post.getId());

        entityManager.persist(post);
        entityManager.flush();
        entityManager.detach(post);

        Set<Post> readPostSet = postRepository.findAllById(Collections.singletonList(1L));

        Assertions.assertEquals(0, readPostSet.size());
    }

    @Test
    public void deleteByIdTest() {
        Post post = ObjectCreator.createPost(user);
        Assertions.assertNull(post.getId());

        entityManager.persistAndFlush(post);
        entityManager.detach(post);

        postRepository.deleteById(post.getId());

        Post readPost = entityManager.find(Post.class, post.getId());

        Assertions.assertFalse(readPost.isActive());
    }

    @Test
    public void deleteTest() {
        Post post = ObjectCreator.createPost(user);
        Assertions.assertNull(post.getId());

        entityManager.persistAndFlush(post);
        entityManager.detach(post);

        postRepository.delete(post);

        Post readPost = entityManager.find(Post.class, post.getId());

        Assertions.assertFalse(readPost.isActive());
    }

    @Test
    public void deleteAllByIdTest() {
        Set<Post> postSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createPostWithMediaFiles(user,
                                "tt" + num, "tx" + num, "pt" + num))
                .collect(Collectors.toSet());

        postSet.forEach(entityManager::persist);
        entityManager.flush();
        postSet.forEach(entityManager::detach);

        Set<Long> postsId = postSet
                .stream()
                .map(IdEntity::getId)
                .collect(Collectors.toSet());

        postRepository.deleteAllById(postsId);

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);

        root.fetch("mediaFiles");

        query
                .select(root);

        List<Post> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        readlist.forEach(post -> Assertions.assertFalse(post.isActive()));
    }
}
