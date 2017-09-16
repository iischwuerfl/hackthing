package ch.meineinitiative.service;

import ch.meineinitiative.domain.enumeration.Status;
import ch.meineinitiative.service.dto.InitiativeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Interface for managing Initiative.
 */
public interface InitiativeService {

    /**
     * Save a initiative.
     *
     * @param initiativeDTO the entity to save
     * @return the persisted entity
     */
    InitiativeDTO save(InitiativeDTO initiativeDTO);

    /**
     * Get all the initiatives.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InitiativeDTO> findAll(Pageable pageable);

    Page<InitiativeDTO> findAllNew(Pageable pageable);

    Page<InitiativeDTO> findAllOld(Pageable pageable);

    /**
     * Get all the initiatives.
     *
     * @param title
     * @param status
     * @return the list of entities
     */
    List<InitiativeDTO> findAll(String title, Status status);


    /**
     * Get the "id" initiative.
     *
     * @param id the id of the entity
     * @return the entity
     */
    InitiativeDTO findOne(Long id);

    /**
     * Delete the "id" initiative.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
