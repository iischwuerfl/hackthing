package ch.meineinitiative.service;

import ch.meineinitiative.service.dto.CommentarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Commentar.
 */
public interface CommentarService {

    /**
     * Save a commentar.
     *
     * @param commentarDTO the entity to save
     * @return the persisted entity
     */
    CommentarDTO save(CommentarDTO commentarDTO);

    /**
     *  Get all the commentars.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CommentarDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" commentar.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CommentarDTO findOne(Long id);

    /**
     *  Delete the "id" commentar.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
