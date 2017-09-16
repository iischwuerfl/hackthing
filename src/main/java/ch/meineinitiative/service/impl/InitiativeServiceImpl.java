package ch.meineinitiative.service.impl;

import ch.meineinitiative.service.InitiativeService;
import ch.meineinitiative.domain.Initiative;
import ch.meineinitiative.repository.InitiativeRepository;
import ch.meineinitiative.service.dto.InitiativeDTO;
import ch.meineinitiative.service.mapper.InitiativeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Initiative.
 */
@Service
@Transactional
public class InitiativeServiceImpl implements InitiativeService{

    private final Logger log = LoggerFactory.getLogger(InitiativeServiceImpl.class);

    private final InitiativeRepository initiativeRepository;

    private final InitiativeMapper initiativeMapper;

    public InitiativeServiceImpl(InitiativeRepository initiativeRepository, InitiativeMapper initiativeMapper) {
        this.initiativeRepository = initiativeRepository;
        this.initiativeMapper = initiativeMapper;
    }

    /**
     * Save a initiative.
     *
     * @param initiativeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InitiativeDTO save(InitiativeDTO initiativeDTO) {
        log.debug("Request to save Initiative : {}", initiativeDTO);
        Initiative initiative = initiativeMapper.toEntity(initiativeDTO);
        initiative = initiativeRepository.save(initiative);
        return initiativeMapper.toDto(initiative);
    }

    /**
     *  Get all the initiatives.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InitiativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Initiatives");
        return initiativeRepository.findAll(pageable)
            .map(initiativeMapper::toDto);
    }

    /**
     *  Get one initiative by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InitiativeDTO findOne(Long id) {
        log.debug("Request to get Initiative : {}", id);
        Initiative initiative = initiativeRepository.findOneWithEagerRelationships(id);
        return initiativeMapper.toDto(initiative);
    }

    /**
     *  Delete the  initiative by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Initiative : {}", id);
        initiativeRepository.delete(id);
    }
}
