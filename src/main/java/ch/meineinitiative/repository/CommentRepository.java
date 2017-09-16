package ch.meineinitiative.repository;

import ch.meineinitiative.domain.Comment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select comment from Comment comment where comment.creator.login = ?#{principal.username}")
    List<Comment> findByCreatorIsCurrentUser();

}
