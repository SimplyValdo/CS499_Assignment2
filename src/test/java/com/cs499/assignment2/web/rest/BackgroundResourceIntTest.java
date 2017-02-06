package com.cs499.assignment2.web.rest;

import com.cs499.assignment2.Assignment2App;

import com.cs499.assignment2.domain.Background;
import com.cs499.assignment2.repository.BackgroundRepository;

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
 * Test class for the BackgroundResource REST controller.
 *
 * @see BackgroundResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Assignment2App.class)
public class BackgroundResourceIntTest {

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_HOMETOWN = "AAAAAAAAAA";
    private static final String UPDATED_HOMETOWN = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEARS_IN_CURRENT_POSITION = 1;
    private static final Integer UPDATED_YEARS_IN_CURRENT_POSITION = 2;

    private static final Integer DEFAULT_TOTAL_CHAMPIONSHIPS = 1;
    private static final Integer UPDATED_TOTAL_CHAMPIONSHIPS = 2;

    @Autowired
    private BackgroundRepository backgroundRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restBackgroundMockMvc;

    private Background background;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            BackgroundResource backgroundResource = new BackgroundResource(backgroundRepository);
        this.restBackgroundMockMvc = MockMvcBuilders.standaloneSetup(backgroundResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Background createEntity(EntityManager em) {
        Background background = new Background()
                .language(DEFAULT_LANGUAGE)
                .hometown(DEFAULT_HOMETOWN)
                .yearsInCurrentPosition(DEFAULT_YEARS_IN_CURRENT_POSITION)
                .totalChampionships(DEFAULT_TOTAL_CHAMPIONSHIPS);
        return background;
    }

    @Before
    public void initTest() {
        background = createEntity(em);
    }

    @Test
    @Transactional
    public void createBackground() throws Exception {
        int databaseSizeBeforeCreate = backgroundRepository.findAll().size();

        // Create the Background

        restBackgroundMockMvc.perform(post("/api/backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(background)))
            .andExpect(status().isCreated());

        // Validate the Background in the database
        List<Background> backgroundList = backgroundRepository.findAll();
        assertThat(backgroundList).hasSize(databaseSizeBeforeCreate + 1);
        Background testBackground = backgroundList.get(backgroundList.size() - 1);
        assertThat(testBackground.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testBackground.getHometown()).isEqualTo(DEFAULT_HOMETOWN);
        assertThat(testBackground.getYearsInCurrentPosition()).isEqualTo(DEFAULT_YEARS_IN_CURRENT_POSITION);
        assertThat(testBackground.getTotalChampionships()).isEqualTo(DEFAULT_TOTAL_CHAMPIONSHIPS);
    }

    @Test
    @Transactional
    public void createBackgroundWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = backgroundRepository.findAll().size();

        // Create the Background with an existing ID
        Background existingBackground = new Background();
        existingBackground.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBackgroundMockMvc.perform(post("/api/backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBackground)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Background> backgroundList = backgroundRepository.findAll();
        assertThat(backgroundList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBackgrounds() throws Exception {
        // Initialize the database
        backgroundRepository.saveAndFlush(background);

        // Get all the backgroundList
        restBackgroundMockMvc.perform(get("/api/backgrounds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(background.getId().intValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].hometown").value(hasItem(DEFAULT_HOMETOWN.toString())))
            .andExpect(jsonPath("$.[*].yearsInCurrentPosition").value(hasItem(DEFAULT_YEARS_IN_CURRENT_POSITION)))
            .andExpect(jsonPath("$.[*].totalChampionships").value(hasItem(DEFAULT_TOTAL_CHAMPIONSHIPS)));
    }

    @Test
    @Transactional
    public void getBackground() throws Exception {
        // Initialize the database
        backgroundRepository.saveAndFlush(background);

        // Get the background
        restBackgroundMockMvc.perform(get("/api/backgrounds/{id}", background.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(background.getId().intValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.hometown").value(DEFAULT_HOMETOWN.toString()))
            .andExpect(jsonPath("$.yearsInCurrentPosition").value(DEFAULT_YEARS_IN_CURRENT_POSITION))
            .andExpect(jsonPath("$.totalChampionships").value(DEFAULT_TOTAL_CHAMPIONSHIPS));
    }

    @Test
    @Transactional
    public void getNonExistingBackground() throws Exception {
        // Get the background
        restBackgroundMockMvc.perform(get("/api/backgrounds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBackground() throws Exception {
        // Initialize the database
        backgroundRepository.saveAndFlush(background);
        int databaseSizeBeforeUpdate = backgroundRepository.findAll().size();

        // Update the background
        Background updatedBackground = backgroundRepository.findOne(background.getId());
        updatedBackground
                .language(UPDATED_LANGUAGE)
                .hometown(UPDATED_HOMETOWN)
                .yearsInCurrentPosition(UPDATED_YEARS_IN_CURRENT_POSITION)
                .totalChampionships(UPDATED_TOTAL_CHAMPIONSHIPS);

        restBackgroundMockMvc.perform(put("/api/backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBackground)))
            .andExpect(status().isOk());

        // Validate the Background in the database
        List<Background> backgroundList = backgroundRepository.findAll();
        assertThat(backgroundList).hasSize(databaseSizeBeforeUpdate);
        Background testBackground = backgroundList.get(backgroundList.size() - 1);
        assertThat(testBackground.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testBackground.getHometown()).isEqualTo(UPDATED_HOMETOWN);
        assertThat(testBackground.getYearsInCurrentPosition()).isEqualTo(UPDATED_YEARS_IN_CURRENT_POSITION);
        assertThat(testBackground.getTotalChampionships()).isEqualTo(UPDATED_TOTAL_CHAMPIONSHIPS);
    }

    @Test
    @Transactional
    public void updateNonExistingBackground() throws Exception {
        int databaseSizeBeforeUpdate = backgroundRepository.findAll().size();

        // Create the Background

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBackgroundMockMvc.perform(put("/api/backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(background)))
            .andExpect(status().isCreated());

        // Validate the Background in the database
        List<Background> backgroundList = backgroundRepository.findAll();
        assertThat(backgroundList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBackground() throws Exception {
        // Initialize the database
        backgroundRepository.saveAndFlush(background);
        int databaseSizeBeforeDelete = backgroundRepository.findAll().size();

        // Get the background
        restBackgroundMockMvc.perform(delete("/api/backgrounds/{id}", background.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Background> backgroundList = backgroundRepository.findAll();
        assertThat(backgroundList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Background.class);
    }
}
