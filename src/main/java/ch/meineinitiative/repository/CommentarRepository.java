package ch.meineinitiative.repository;

import ch.meineinitiative.domain.Commentar;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Commentar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentarRepository extends JpaRepository<Commentar, Long> {

    @Query("select commentar from Commentar commentar where commentar.creator.login = ?#{principal.username}")
    List<Commentar> findByCreatorIsCurrentUser();

}
