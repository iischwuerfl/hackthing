package ch.meineinitiative.web.rest;

import ch.meineinitiative.MeineInitiativeApp;

import ch.meineinitiative.domain.Initiative;
import ch.meineinitiative.repository.InitiativeRepository;
import ch.meineinitiative.service.InitiativeService;
import ch.meineinitiative.service.dto.InitiativeDTO;
import ch.meineinitiative.service.mapper.InitiativeMapper;
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

import ch.meineinitiative.domain.enumeration.Status;
/**
 * Test class for the InitiativeResource REST controller.
 *
 * @see InitiativeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeineInitiativeApp.class)
public class InitiativeResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PROPOSAL;
    private static final Status UPDATED_STATUS = Status.COLLECTING;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private InitiativeRepository initiativeRepository;

    @Autowired
    private InitiativeMapper initiativeMapper;

    @Autowired
    private InitiativeService initiativeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInitiativeMockMvc;

    private Initiative initiative;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InitiativeResource initiativeResource = new InitiativeResource(initiativeService);
        this.restInitiativeMockMvc = MockMvcBuilders.standaloneSetup(initiativeResource)
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
    public static Initiative createEntity(EntityManager em) {
        Initiative initiative = new Initiative()
            .title(DEFAULT_TITLE)
            .text(DEFAULT_TEXT)
            .status(DEFAULT_STATUS)
            .creationDate(DEFAULT_CREATION_DATE);
        return initiative;
    }

    @Before
    public void initTest() {
        initiative = createEntity(em);
    }

    @Test
    @Transactional
    public void createInitiative() throws Exception {
        int databaseSizeBeforeCreate = initiativeRepository.findAll().size();

        // Create the Initiative
        InitiativeDTO initiativeDTO = initiativeMapper.toDto(initiative);
        restInitiativeMockMvc.perform(post("/api/initiatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(initiativeDTO)))
            .andExpect(status().isCreated());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeCreate + 1);
        Initiative testInitiative = initiativeList.get(initiativeList.size() - 1);
        assertThat(testInitiative.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInitiative.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testInitiative.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInitiative.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createInitiativeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = initiativeRepository.findAll().size();

        // Create the Initiative with an existing ID
        initiative.setId(1L);
        InitiativeDTO initiativeDTO = initiativeMapper.toDto(initiative);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInitiativeMockMvc.perform(post("/api/initiatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(initiativeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInitiatives() throws Exception {
        // Initialize the database
        initiativeRepository.saveAndFlush(initiative);

        // Get all the initiativeList
        restInitiativeMockMvc.perform(get("/api/initiatives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(initiative.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))));
    }

    @Test
    @Transactional
    public void getInitiative() throws Exception {
        // Initialize the database
        initiativeRepository.saveAndFlush(initiative);

        // Get the initiative
        restInitiativeMockMvc.perform(get("/api/initiatives/{id}", initiative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(initiative.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingInitiative() throws Exception {
        // Get the initiative
        restInitiativeMockMvc.perform(get("/api/initiatives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInitiative() throws Exception {
        // Initialize the database
        initiativeRepository.saveAndFlush(initiative);
        int databaseSizeBeforeUpdate = initiativeRepository.findAll().size();

        // Update the initiative
        Initiative updatedInitiative = initiativeRepository.findOne(initiative.getId());
        updatedInitiative
            .title(UPDATED_TITLE)
            .text(UPDATED_TEXT)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);
        InitiativeDTO initiativeDTO = initiativeMapper.toDto(updatedInitiative);

        restInitiativeMockMvc.perform(put("/api/initiatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(initiativeDTO)))
            .andExpect(status().isOk());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeUpdate);
        Initiative testInitiative = initiativeList.get(initiativeList.size() - 1);
        assertThat(testInitiative.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInitiative.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testInitiative.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInitiative.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingInitiative() throws Exception {
        int databaseSizeBeforeUpdate = initiativeRepository.findAll().size();

        // Create the Initiative
        InitiativeDTO initiativeDTO = initiativeMapper.toDto(initiative);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInitiativeMockMvc.perform(put("/api/initiatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(initiativeDTO)))
            .andExpect(status().isCreated());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInitiative() throws Exception {
        // Initialize the database
        initiativeRepository.saveAndFlush(initiative);
        int databaseSizeBeforeDelete = initiativeRepository.findAll().size();

        // Get the initiative
        restInitiativeMockMvc.perform(delete("/api/initiatives/{id}", initiative.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Initiative.class);
        Initiative initiative1 = new Initiative();
        initiative1.setId(1L);
        Initiative initiative2 = new Initiative();
        initiative2.setId(initiative1.getId());
        assertThat(initiative1).isEqualTo(initiative2);
        initiative2.setId(2L);
        assertThat(initiative1).isNotEqualTo(initiative2);
        initiative1.setId(null);
        assertThat(initiative1).isNotEqualTo(initiative2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InitiativeDTO.class);
        InitiativeDTO initiativeDTO1 = new InitiativeDTO();
        initiativeDTO1.setId(1L);
        InitiativeDTO initiativeDTO2 = new InitiativeDTO();
        assertThat(initiativeDTO1).isNotEqualTo(initiativeDTO2);
        initiativeDTO2.setId(initiativeDTO1.getId());
        assertThat(initiativeDTO1).isEqualTo(initiativeDTO2);
        initiativeDTO2.setId(2L);
        assertThat(initiativeDTO1).isNotEqualTo(initiativeDTO2);
        initiativeDTO1.setId(null);
        assertThat(initiativeDTO1).isNotEqualTo(initiativeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(initiativeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(initiativeMapper.fromId(null)).isNull();
    }
}
