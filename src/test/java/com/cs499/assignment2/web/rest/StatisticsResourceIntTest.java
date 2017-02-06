package com.cs499.assignment2.web.rest;

import com.cs499.assignment2.Assignment2App;

import com.cs499.assignment2.domain.Statistics;
import com.cs499.assignment2.repository.StatisticsRepository;

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
 * Test class for the StatisticsResource REST controller.
 *
 * @see StatisticsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Assignment2App.class)
public class StatisticsResourceIntTest {

    private static final Integer DEFAULT_WINS = 1;
    private static final Integer UPDATED_WINS = 2;

    private static final Integer DEFAULT_LOSSES = 1;
    private static final Integer UPDATED_LOSSES = 2;

    private static final Integer DEFAULT_DRAWS = 1;
    private static final Integer UPDATED_DRAWS = 2;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restStatisticsMockMvc;

    private Statistics statistics;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            StatisticsResource statisticsResource = new StatisticsResource(statisticsRepository);
        this.restStatisticsMockMvc = MockMvcBuilders.standaloneSetup(statisticsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Statistics createEntity(EntityManager em) {
        Statistics statistics = new Statistics()
                .wins(DEFAULT_WINS)
                .losses(DEFAULT_LOSSES)
                .draws(DEFAULT_DRAWS);
        return statistics;
    }

    @Before
    public void initTest() {
        statistics = createEntity(em);
    }

    @Test
    @Transactional
    public void createStatistics() throws Exception {
        int databaseSizeBeforeCreate = statisticsRepository.findAll().size();

        // Create the Statistics

        restStatisticsMockMvc.perform(post("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statistics)))
            .andExpect(status().isCreated());

        // Validate the Statistics in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeCreate + 1);
        Statistics testStatistics = statisticsList.get(statisticsList.size() - 1);
        assertThat(testStatistics.getWins()).isEqualTo(DEFAULT_WINS);
        assertThat(testStatistics.getLosses()).isEqualTo(DEFAULT_LOSSES);
        assertThat(testStatistics.getDraws()).isEqualTo(DEFAULT_DRAWS);
    }

    @Test
    @Transactional
    public void createStatisticsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = statisticsRepository.findAll().size();

        // Create the Statistics with an existing ID
        Statistics existingStatistics = new Statistics();
        existingStatistics.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatisticsMockMvc.perform(post("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStatistics)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStatistics() throws Exception {
        // Initialize the database
        statisticsRepository.saveAndFlush(statistics);

        // Get all the statisticsList
        restStatisticsMockMvc.perform(get("/api/statistics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].wins").value(hasItem(DEFAULT_WINS)))
            .andExpect(jsonPath("$.[*].losses").value(hasItem(DEFAULT_LOSSES)))
            .andExpect(jsonPath("$.[*].draws").value(hasItem(DEFAULT_DRAWS)));
    }

    @Test
    @Transactional
    public void getStatistics() throws Exception {
        // Initialize the database
        statisticsRepository.saveAndFlush(statistics);

        // Get the statistics
        restStatisticsMockMvc.perform(get("/api/statistics/{id}", statistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(statistics.getId().intValue()))
            .andExpect(jsonPath("$.wins").value(DEFAULT_WINS))
            .andExpect(jsonPath("$.losses").value(DEFAULT_LOSSES))
            .andExpect(jsonPath("$.draws").value(DEFAULT_DRAWS));
    }

    @Test
    @Transactional
    public void getNonExistingStatistics() throws Exception {
        // Get the statistics
        restStatisticsMockMvc.perform(get("/api/statistics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatistics() throws Exception {
        // Initialize the database
        statisticsRepository.saveAndFlush(statistics);
        int databaseSizeBeforeUpdate = statisticsRepository.findAll().size();

        // Update the statistics
        Statistics updatedStatistics = statisticsRepository.findOne(statistics.getId());
        updatedStatistics
                .wins(UPDATED_WINS)
                .losses(UPDATED_LOSSES)
                .draws(UPDATED_DRAWS);

        restStatisticsMockMvc.perform(put("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStatistics)))
            .andExpect(status().isOk());

        // Validate the Statistics in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeUpdate);
        Statistics testStatistics = statisticsList.get(statisticsList.size() - 1);
        assertThat(testStatistics.getWins()).isEqualTo(UPDATED_WINS);
        assertThat(testStatistics.getLosses()).isEqualTo(UPDATED_LOSSES);
        assertThat(testStatistics.getDraws()).isEqualTo(UPDATED_DRAWS);
    }

    @Test
    @Transactional
    public void updateNonExistingStatistics() throws Exception {
        int databaseSizeBeforeUpdate = statisticsRepository.findAll().size();

        // Create the Statistics

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStatisticsMockMvc.perform(put("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statistics)))
            .andExpect(status().isCreated());

        // Validate the Statistics in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStatistics() throws Exception {
        // Initialize the database
        statisticsRepository.saveAndFlush(statistics);
        int databaseSizeBeforeDelete = statisticsRepository.findAll().size();

        // Get the statistics
        restStatisticsMockMvc.perform(delete("/api/statistics/{id}", statistics.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Statistics.class);
    }
}
