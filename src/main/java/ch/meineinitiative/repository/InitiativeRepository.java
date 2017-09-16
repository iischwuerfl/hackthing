package ch.meineinitiative.repository;

import ch.meineinitiative.domain.Initiative;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Initiative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

    @Query("select initiative from Initiative initiative where initiative.initiator.login = ?#{principal.username}")
    List<Initiative> findByInitiatorIsCurrentUser();
    @Query("select distinct initiative from Initiative initiative left join fetch initiative.citizenSupporters left join fetch initiative.politicianSupporters")
    List<Initiative> findAllWithEagerRelationships();

    @Query("select initiative from Initiative initiative left join fetch initiative.citizenSupporters left join fetch initiative.politicianSupporters where initiative.id =:id")
    Initiative findOneWithEagerRelationships(@Param("id") Long id);

}
