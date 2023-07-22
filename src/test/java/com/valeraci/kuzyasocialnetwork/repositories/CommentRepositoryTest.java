package com.valeraci.kuzyasocialnetwork.repositories;

import com.valeraci.kuzyasocialnetwork.models.Comment;
import com.valeraci.kuzyasocialnetwork.models.Post;
import com.valeraci.kuzyasocialnetwork.models.User;
import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdEntity;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
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
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final User user = ObjectCreator.createUser();
    private final User commentator = ObjectCreator.createUser();
    private final Post post = ObjectCreator.createPost(user);

    @BeforeEach
    public void userInsert() {
        entityManager.persist(user);
        entityManager.persist(commentator);
        entityManager.persist(post);
    }

    @Test
    public void saveTest() {
        Comment comment = ObjectCreator.createComment(post, commentator);
        Assertions.assertNull(comment.getId());

        comment = commentRepository.save(comment);
        entityManager.flush();
        entityManager.detach(comment);

        Comment readComment = entityManager.find(Comment.class, comment.getId());

        Assertions.assertNotNull(readComment);
        Assertions.assertEquals(comment.getText(), readComment.getText());
        Assertions.assertEquals(comment.getCommentator().getFamilyStatus(),
                comment.getCommentator().getFamilyStatus());
    }

    @Test
    public void saveAllTest() {
        Set<Comment> commentSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createComment(post, commentator, "txt" + num))
                .collect(Collectors.toSet());

        commentSet = commentRepository.saveAll(commentSet);

        entityManager.flush();
        commentSet.forEach(comment -> entityManager.detach(comment));

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Comment> query = builder.createQuery(Comment.class);
        Root<Comment> root = query.from(Comment.class);

        root.fetch("commentator");

        query
                .select(root)
                .where(builder.equal(root.get("isActive"), true));

        List<Comment> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        Assertions.assertEquals(commentSet.size(), readlist.size());
    }

    @Test
    public void findByIdTest() {
        Comment comment = ObjectCreator.createComment(post, commentator);
        Assertions.assertNull(comment.getId());

        entityManager.persistAndFlush(comment);
        entityManager.detach(comment);

        Comment readComment = commentRepository.findById(comment.getId()).orElse(null);
        entityManager.detach(readComment);

        Assertions.assertNotNull(readComment);
        Assertions.assertEquals(comment.getText(), readComment.getText());
        Assertions.assertTrue(readComment.isActive());
        Assertions.assertEquals(FamilyStatusTitle.SINGLE,
                readComment.getCommentator().getFamilyStatus().getTitle());
    }

    @Test
    public void findByIdFailTest() {
        Comment comment = ObjectCreator.createComment(post, commentator);
        comment.setActive(false);
        Assertions.assertNull(comment.getId());

        entityManager.persist(comment);
        entityManager.flush();
        entityManager.detach(comment);

        Comment readComment = commentRepository.findById(comment.getId()).orElse(null);

        Assertions.assertNull(readComment);
    }

    @Test
    public void findAllTest() {
        Set<Comment> commentSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createComment(post, commentator, "txt" + num))
                .collect(Collectors.toSet());

        commentSet.forEach(entityManager::persist);
        entityManager.flush();
        commentSet.forEach(entityManager::detach);

        Set<Comment> readCommentSet = commentRepository.findAll();

        readCommentSet.forEach(entityManager::detach);

        Assertions.assertEquals(commentSet.size(), readCommentSet.size());
        readCommentSet.forEach(comment -> Assertions.assertTrue(comment.isActive()));
        readCommentSet
                .forEach(comment -> Assertions.assertEquals(FamilyStatusTitle.SINGLE,
                        comment.getCommentator().getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllFailTest() {
        Comment comment = ObjectCreator.createComment(post, commentator);
        comment.setActive(false);
        Assertions.assertNull(comment.getId());

        entityManager.persist(comment);
        entityManager.flush();
        entityManager.detach(comment);

        Set<Comment> readCommentSet = commentRepository.findAll();

        Assertions.assertEquals(0, readCommentSet.size());
    }

    @Test
    public void findAllByIdTest() {
        Set<Comment> commentSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createComment(post, commentator, "txt" + num))
                .collect(Collectors.toSet());

        commentSet.forEach(entityManager::persist);
        entityManager.flush();
        commentSet.forEach(entityManager::detach);


        Set<Long> commentsId = commentSet
                .stream()
                .map(IdEntity::getId)
                .collect(Collectors.toSet());

        Set<Comment> readCommentSet = commentRepository.findAllById(commentsId);

        readCommentSet.forEach(entityManager::detach);

        Assertions.assertEquals(commentSet.size(), readCommentSet.size());
        readCommentSet.forEach(comment -> Assertions.assertTrue(comment.isActive()));
        readCommentSet
                .forEach(comment -> Assertions.assertEquals(FamilyStatusTitle.SINGLE,
                        comment.getCommentator().getFamilyStatus().getTitle()));
    }

    @Test
    public void findAllByIdFailTest() {
        Comment comment = ObjectCreator.createComment(post, commentator);
        comment.setActive(false);
        Assertions.assertNull(comment.getId());

        entityManager.persist(comment);
        entityManager.flush();
        entityManager.detach(comment);

        Set<Comment> readCommentSet = commentRepository.findAllById(Collections.singletonList(1L));

        Assertions.assertEquals(0, readCommentSet.size());
    }

    @Test
    public void deleteByIdTest() {
        Comment comment = ObjectCreator.createComment(post, commentator);
        Assertions.assertNull(comment.getId());

        entityManager.persistAndFlush(comment);
        entityManager.detach(comment);

        commentRepository.deleteById(comment.getId());

        Comment readComment = entityManager.find(Comment.class, comment.getId());

        Assertions.assertFalse(readComment.isActive());
    }

    @Test
    public void deleteTest() {
        Comment comment = ObjectCreator.createComment(post, commentator);
        Assertions.assertNull(comment.getId());

        entityManager.persistAndFlush(comment);
        entityManager.detach(comment);

        commentRepository.delete(comment);

        Comment readComment = entityManager.find(Comment.class, comment.getId());

        Assertions.assertFalse(readComment.isActive());
    }

    @Test
    public void deleteAllByIdTest() {
        Set<Comment> commentSet = IntStream.range(0, 10)
                .mapToObj(num ->
                        ObjectCreator.createComment(post, commentator, "txt" + num))
                .collect(Collectors.toSet());

        commentSet.forEach(entityManager::persist);
        entityManager.flush();
        commentSet.forEach(entityManager::detach);

        Set<Long> commentsId = commentSet
                .stream()
                .map(IdEntity::getId)
                .collect(Collectors.toSet());

        commentRepository.deleteAllById(commentsId);

        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Comment> query = builder.createQuery(Comment.class);
        Root<Comment> root = query.from(Comment.class);

        root.fetch("commentator");

        query
                .select(root);

        List<Comment> readlist = entityManager.getEntityManager().createQuery(query).getResultList();

        readlist.forEach(comment -> Assertions.assertFalse(comment.isActive()));
    }
}
