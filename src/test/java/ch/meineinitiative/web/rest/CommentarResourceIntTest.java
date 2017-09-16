package ch.meineinitiative.web.rest;

import ch.meineinitiative.MeineInitiativeApp;

import ch.meineinitiative.domain.Commentar;
import ch.meineinitiative.repository.CommentarRepository;
import ch.meineinitiative.service.CommentarService;
import ch.meineinitiative.service.dto.CommentarDTO;
import ch.meineinitiative.service.mapper.CommentarMapper;
import ch.meineinitiative.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static ch.meineinitiative.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommentarResource REST controller.
 *
 * @see CommentarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeineInitiativeApp.class)
public class CommentarResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CommentarRepository commentarRepository;

    @Autowired
    private CommentarMapper commentarMapper;

    @Autowired
    private CommentarService commentarService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommentarMockMvc;

    private Commentar commentar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommentarResource commentarResource = new CommentarResource(commentarService);
        this.restCommentarMockMvc = MockMvcBuilders.standaloneSetup(commentarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentar createEntity(EntityManager em) {
        Commentar commentar = new Commentar()
            .text(DEFAULT_TEXT)
            .creationDate(DEFAULT_CREATION_DATE);
        return commentar;
    }

    @Before
    public void initTest() {
        commentar = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommentar() throws Exception {
        int databaseSizeBeforeCreate = commentarRepository.findAll().size();

        // Create the Commentar
        CommentarDTO commentarDTO = commentarMapper.toDto(commentar);
        restCommentarMockMvc.perform(post("/api/commentars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentarDTO)))
            .andExpect(status().isCreated());

        // Validate the Commentar in the database
        List<Commentar> commentarList = commentarRepository.findAll();
        assertThat(commentarList).hasSize(databaseSizeBeforeCreate + 1);
        Commentar testCommentar = commentarList.get(commentarList.size() - 1);
        assertThat(testCommentar.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testCommentar.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createCommentarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentarRepository.findAll().size();

        // Create the Commentar with an existing ID
        commentar.setId(1L);
        CommentarDTO commentarDTO = commentarMapper.toDto(commentar);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentarMockMvc.perform(post("/api/commentars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commentar in the database
        List<Commentar> commentarList = commentarRepository.findAll();
        assertThat(commentarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCommentars() throws Exception {
        // Initialize the database
        commentarRepository.saveAndFlush(commentar);

        // Get all the commentarList
        restCommentarMockMvc.perform(get("/api/commentars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentar.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))));
    }

    @Test
    @Transactional
    public void getCommentar() throws Exception {
        // Initialize the database
        commentarRepository.saveAndFlush(commentar);

        // Get the commentar
        restCommentarMockMvc.perform(get("/api/commentars/{id}", commentar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commentar.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingCommentar() throws Exception {
        // Get the commentar
        restCommentarMockMvc.perform(get("/api/commentars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentar() throws Exception {
        // Initialize the database
        commentarRepository.saveAndFlush(commentar);
        int databaseSizeBeforeUpdate = commentarRepository.findAll().size();

        // Update the commentar
        Commentar updatedCommentar = commentarRepository.findOne(commentar.getId());
        updatedCommentar
            .text(UPDATED_TEXT)
            .creationDate(UPDATED_CREATION_DATE);
        CommentarDTO commentarDTO = commentarMapper.toDto(updatedCommentar);

        restCommentarMockMvc.perform(put("/api/commentars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentarDTO)))
            .andExpect(status().isOk());

        // Validate the Commentar in the database
        List<Commentar> commentarList = commentarRepository.findAll();
        assertThat(commentarList).hasSize(databaseSizeBeforeUpdate);
        Commentar testCommentar = commentarList.get(commentarList.size() - 1);
        assertThat(testCommentar.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCommentar.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCommentar() throws Exception {
        int databaseSizeBeforeUpdate = commentarRepository.findAll().size();

        // Create the Commentar
        CommentarDTO commentarDTO = commentarMapper.toDto(commentar);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommentarMockMvc.perform(put("/api/commentars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentarDTO)))
            .andExpect(status().isCreated());

        // Validate the Commentar in the database
        List<Commentar> commentarList = commentarRepository.findAll();
        assertThat(commentarList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommentar() throws Exception {
        // Initialize the database
        commentarRepository.saveAndFlush(commentar);
        int databaseSizeBeforeDelete = commentarRepository.findAll().size();

        // Get the commentar
        restCommentarMockMvc.perform(delete("/api/commentars/{id}", commentar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Commentar> commentarList = commentarRepository.findAll();
        assertThat(commentarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commentar.class);
        Commentar commentar1 = new Commentar();
        commentar1.setId(1L);
        Commentar commentar2 = new Commentar();
        commentar2.setId(commentar1.getId());
        assertThat(commentar1).isEqualTo(commentar2);
        commentar2.setId(2L);
        assertThat(commentar1).isNotEqualTo(commentar2);
        commentar1.setId(null);
        assertThat(commentar1).isNotEqualTo(commentar2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentarDTO.class);
        CommentarDTO commentarDTO1 = new CommentarDTO();
        commentarDTO1.setId(1L);
        CommentarDTO commentarDTO2 = new CommentarDTO();
        assertThat(commentarDTO1).isNotEqualTo(commentarDTO2);
        commentarDTO2.setId(commentarDTO1.getId());
        assertThat(commentarDTO1).isEqualTo(commentarDTO2);
        commentarDTO2.setId(2L);
        assertThat(commentarDTO1).isNotEqualTo(commentarDTO2);
        commentarDTO1.setId(null);
        assertThat(commentarDTO1).isNotEqualTo(commentarDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commentarMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commentarMapper.fromId(null)).isNull();
    }
}
