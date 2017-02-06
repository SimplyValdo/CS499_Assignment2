package com.cs499.assignment2.web.rest;

import com.cs499.assignment2.Assignment2App;

import com.cs499.assignment2.domain.Coach;
import com.cs499.assignment2.repository.CoachRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoachResource REST controller.
 *
 * @see CoachResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Assignment2App.class)
public class CoachResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_TITLE = "BBBBBBBBBB";

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restCoachMockMvc;

    private Coach coach;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            CoachResource coachResource = new CoachResource(coachRepository);
        this.restCoachMockMvc = MockMvcBuilders.standaloneSetup(coachResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coach createEntity(EntityManager em) {
        Coach coach = new Coach()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .positionTitle(DEFAULT_POSITION_TITLE);
        return coach;
    }

    @Before
    public void initTest() {
        coach = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoach() throws Exception {
        int databaseSizeBeforeCreate = coachRepository.findAll().size();

        // Create the Coach

        restCoachMockMvc.perform(post("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isCreated());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeCreate + 1);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCoach.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCoach.getPositionTitle()).isEqualTo(DEFAULT_POSITION_TITLE);
    }

    @Test
    @Transactional
    public void createCoachWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coachRepository.findAll().size();

        // Create the Coach with an existing ID
        Coach existingCoach = new Coach();
        existingCoach.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoachMockMvc.perform(post("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCoach)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoaches() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get all the coachList
        restCoachMockMvc.perform(get("/api/coaches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coach.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].positionTitle").value(hasItem(DEFAULT_POSITION_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get the coach
        restCoachMockMvc.perform(get("/api/coaches/{id}", coach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coach.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.positionTitle").value(DEFAULT_POSITION_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoach() throws Exception {
        // Get the coach
        restCoachMockMvc.perform(get("/api/coaches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Update the coach
        Coach updatedCoach = coachRepository.findOne(coach.getId());
        updatedCoach
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .positionTitle(UPDATED_POSITION_TITLE);

        restCoachMockMvc.perform(put("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoach)))
            .andExpect(status().isOk());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCoach.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCoach.getPositionTitle()).isEqualTo(UPDATED_POSITION_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Create the Coach

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoachMockMvc.perform(put("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isCreated());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);
        int databaseSizeBeforeDelete = coachRepository.findAll().size();

        // Get the coach
        restCoachMockMvc.perform(delete("/api/coaches/{id}", coach.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coach.class);
    }
}
