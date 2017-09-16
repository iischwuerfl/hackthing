package ch.meineinitiative.web.rest;

import ch.meineinitiative.domain.User;
import ch.meineinitiative.domain.enumeration.Status;
import ch.meineinitiative.security.SecurityUtils;
import ch.meineinitiative.service.InitiativeService;
import ch.meineinitiative.service.UserService;
import ch.meineinitiative.service.dto.InitiativeDTO;
import ch.meineinitiative.service.dto.UserDTO;
import ch.meineinitiative.service.mapper.UserMapper;
import ch.meineinitiative.web.rest.util.HeaderUtil;
import ch.meineinitiative.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Initiative.
 */
@RestController
@RequestMapping("/api")
public class InitiativeResource {

    private final Logger log = LoggerFactory.getLogger(InitiativeResource.class);

    private static final String ENTITY_NAME = "initiative";

    private final InitiativeService initiativeService;
    private final UserService userService;

    private final UserMapper userMapper;

    public InitiativeResource(InitiativeService initiativeService, UserService userService, UserMapper userMapper) {
        this.initiativeService = initiativeService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * POST  /initiatives : Create a new initiative.
     *
     * @param initiativeDTO the initiativeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new initiativeDTO, or with status 400 (Bad Request) if the initiative has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/initiatives")
    @Timed
    public ResponseEntity<InitiativeDTO> createInitiative(@RequestBody InitiativeDTO initiativeDTO) throws Exception {
        log.debug("REST request to save Initiative : {}", initiativeDTO);
        if (initiativeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new initiative cannot already have an ID")).body(null);
        }
        LocalDateTime localDateAndTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("Europe/Berlin");

        initiativeDTO.setCreationDate(ZonedDateTime.of(localDateAndTime, zoneId));
        initiativeDTO.setStatus(Status.PROPOSAL);
        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin());
        if(userWithAuthoritiesByLogin.isPresent()){
            initiativeDTO.setInitiatorId(userWithAuthoritiesByLogin.get().getId());
        }
        else{
            throw new Exception("no user found");
        }
        InitiativeDTO result = initiativeService.save(initiativeDTO);
        return ResponseEntity.created(new URI("/api/initiatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /initiatives : Updates an existing initiative.
     *
     * @param initiativeDTO the initiativeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated initiativeDTO,
     * or with status 400 (Bad Request) if the initiativeDTO is not valid,
     * or with status 500 (Internal Server Error) if the initiativeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/initiatives")
    @Timed
    public ResponseEntity<InitiativeDTO> updateInitiative(@RequestBody InitiativeDTO initiativeDTO) throws Exception {
        log.debug("REST request to update Initiative : {}", initiativeDTO);
        if (initiativeDTO.getId() == null) {
            return createInitiative(initiativeDTO);
        }
        InitiativeDTO result = initiativeService.save(initiativeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, initiativeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /initiatives : get all the initiatives.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of initiatives in body
     */
    @GetMapping("/initiatives")
    @Timed
    public ResponseEntity<List<InitiativeDTO>> getAllInitiatives(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Initiatives");
        Page<InitiativeDTO> page = initiativeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/initiatives");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /initiatives/similar-title : get the most similar titled initiatives.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of initiatives in body
     */
    @GetMapping("/initiatives/similar-title/{title}")
    @Timed
    public ResponseEntity<List<InitiativeDTO>> getMostSimilarTitledInitiatives(@PathVariable String title, @RequestParam(required = false) Status status) {
        log.debug("REST request to get a page of similar Initiatives");
        List<InitiativeDTO> list = initiativeService.findAll(title, status);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * GET  /initiatives/:id : get the "id" initiative.
     *
     * @param id the id of the initiativeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the initiativeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/initiatives/{id}")
    @Timed
    public ResponseEntity<InitiativeDTO> getInitiative(@PathVariable Long id) {
        log.debug("REST request to get Initiative : {}", id);
        InitiativeDTO initiativeDTO = initiativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(initiativeDTO));
    }

    /**
     * DELETE  /initiatives/:id : delete the "id" initiative.
     *
     * @param id the id of the initiativeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/initiatives/{id}")
    @Timed
    public ResponseEntity<Void> deleteInitiative(@PathVariable Long id) {
        log.debug("REST request to delete Initiative : {}", id);
        initiativeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
