package ch.meineinitiative.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.meineinitiative.service.CommentarService;
import ch.meineinitiative.web.rest.util.HeaderUtil;
import ch.meineinitiative.web.rest.util.PaginationUtil;
import ch.meineinitiative.service.dto.CommentarDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Commentar.
 */
@RestController
@RequestMapping("/api")
public class CommentarResource {

    private final Logger log = LoggerFactory.getLogger(CommentarResource.class);

    private static final String ENTITY_NAME = "commentar";

    private final CommentarService commentarService;

    public CommentarResource(CommentarService commentarService) {
        this.commentarService = commentarService;
    }

    /**
     * POST  /commentars : Create a new commentar.
     *
     * @param commentarDTO the commentarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentarDTO, or with status 400 (Bad Request) if the commentar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commentars")
    @Timed
    public ResponseEntity<CommentarDTO> createCommentar(@RequestBody CommentarDTO commentarDTO) throws URISyntaxException {
        log.debug("REST request to save Commentar : {}", commentarDTO);
        if (commentarDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new commentar cannot already have an ID")).body(null);
        }
        CommentarDTO result = commentarService.save(commentarDTO);
        return ResponseEntity.created(new URI("/api/commentars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commentars : Updates an existing commentar.
     *
     * @param commentarDTO the commentarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentarDTO,
     * or with status 400 (Bad Request) if the commentarDTO is not valid,
     * or with status 500 (Internal Server Error) if the commentarDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commentars")
    @Timed
    public ResponseEntity<CommentarDTO> updateCommentar(@RequestBody CommentarDTO commentarDTO) throws URISyntaxException {
        log.debug("REST request to update Commentar : {}", commentarDTO);
        if (commentarDTO.getId() == null) {
            return createCommentar(commentarDTO);
        }
        CommentarDTO result = commentarService.save(commentarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commentarDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commentars : get all the commentars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commentars in body
     */
    @GetMapping("/commentars")
    @Timed
    public ResponseEntity<List<CommentarDTO>> getAllCommentars(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Commentars");
        Page<CommentarDTO> page = commentarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commentars/:id : get the "id" commentar.
     *
     * @param id the id of the commentarDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentarDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commentars/{id}")
    @Timed
    public ResponseEntity<CommentarDTO> getCommentar(@PathVariable Long id) {
        log.debug("REST request to get Commentar : {}", id);
        CommentarDTO commentarDTO = commentarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commentarDTO));
    }

    /**
     * DELETE  /commentars/:id : delete the "id" commentar.
     *
     * @param id the id of the commentarDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commentars/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommentar(@PathVariable Long id) {
        log.debug("REST request to delete Commentar : {}", id);
        commentarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
