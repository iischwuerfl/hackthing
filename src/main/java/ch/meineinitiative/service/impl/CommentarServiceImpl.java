package ch.meineinitiative.service.impl;

import ch.meineinitiative.service.CommentarService;
import ch.meineinitiative.domain.Commentar;
import ch.meineinitiative.repository.CommentarRepository;
import ch.meineinitiative.service.dto.CommentarDTO;
import ch.meineinitiative.service.mapper.CommentarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Commentar.
 */
@Service
@Transactional
public class CommentarServiceImpl implements CommentarService{

    private final Logger log = LoggerFactory.getLogger(CommentarServiceImpl.class);

    private final CommentarRepository commentarRepository;

    private final CommentarMapper commentarMapper;

    public CommentarServiceImpl(CommentarRepository commentarRepository, CommentarMapper commentarMapper) {
        this.commentarRepository = commentarRepository;
        this.commentarMapper = commentarMapper;
    }

    /**
     * Save a commentar.
     *
     * @param commentarDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CommentarDTO save(CommentarDTO commentarDTO) {
        log.debug("Request to save Commentar : {}", commentarDTO);
        Commentar commentar = commentarMapper.toEntity(commentarDTO);
        commentar = commentarRepository.save(commentar);
        return commentarMapper.toDto(commentar);
    }

    /**
     *  Get all the commentars.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommentarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commentars");
        return commentarRepository.findAll(pageable)
            .map(commentarMapper::toDto);
    }

    /**
     *  Get one commentar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CommentarDTO findOne(Long id) {
        log.debug("Request to get Commentar : {}", id);
        Commentar commentar = commentarRepository.findOne(id);
        return commentarMapper.toDto(commentar);
    }

    /**
     *  Delete the  commentar by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commentar : {}", id);
        commentarRepository.delete(id);
    }
}
