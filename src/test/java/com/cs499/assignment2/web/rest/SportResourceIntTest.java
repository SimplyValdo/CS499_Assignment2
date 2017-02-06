package com.cs499.assignment2.web.rest;

import com.cs499.assignment2.Assignment2App;

import com.cs499.assignment2.domain.Sport;
import com.cs499.assignment2.repository.SportRepository;

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
 * Test class for the SportResource REST controller.
 *
 * @see SportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Assignment2App.class)
public class SportResourceIntTest {

    private static final String DEFAULT_SPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SPORT_NAME = "BBBBBBBBBB";

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restSportMockMvc;

    private Sport sport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SportResource sportResource = new SportResource(sportRepository);
        this.restSportMockMvc = MockMvcBuilders.standaloneSetup(sportResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sport createEntity(EntityManager em) {
        Sport sport = new Sport()
                .sportName(DEFAULT_SPORT_NAME);
        return sport;
    }

    @Before
    public void initTest() {
        sport = createEntity(em);
    }

    @Test
    @Transactional
    public void createSport() throws Exception {
        int databaseSizeBeforeCreate = sportRepository.findAll().size();

        // Create the Sport

        restSportMockMvc.perform(post("/api/sports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sport)))
            .andExpect(status().isCreated());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeCreate + 1);
        Sport testSport = sportList.get(sportList.size() - 1);
        assertThat(testSport.getSportName()).isEqualTo(DEFAULT_SPORT_NAME);
    }

    @Test
    @Transactional
    public void createSportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sportRepository.findAll().size();

        // Create the Sport with an existing ID
        Sport existingSport = new Sport();
        existingSport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSportMockMvc.perform(post("/api/sports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSport)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSports() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        // Get all the sportList
        restSportMockMvc.perform(get("/api/sports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sport.getId().intValue())))
            .andExpect(jsonPath("$.[*].sportName").value(hasItem(DEFAULT_SPORT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSport() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        // Get the sport
        restSportMockMvc.perform(get("/api/sports/{id}", sport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sport.getId().intValue()))
            .andExpect(jsonPath("$.sportName").value(DEFAULT_SPORT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSport() throws Exception {
        // Get the sport
        restSportMockMvc.perform(get("/api/sports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSport() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();

        // Update the sport
        Sport updatedSport = sportRepository.findOne(sport.getId());
        updatedSport
                .sportName(UPDATED_SPORT_NAME);

        restSportMockMvc.perform(put("/api/sports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSport)))
            .andExpect(status().isOk());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
        Sport testSport = sportList.get(sportList.size() - 1);
        assertThat(testSport.getSportName()).isEqualTo(UPDATED_SPORT_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSport() throws Exception {
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();

        // Create the Sport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSportMockMvc.perform(put("/api/sports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sport)))
            .andExpect(status().isCreated());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSport() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);
        int databaseSizeBeforeDelete = sportRepository.findAll().size();

        // Get the sport
        restSportMockMvc.perform(delete("/api/sports/{id}", sport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sport.class);
    }
}
