package com.cs499.assignment2.web.rest;

import com.cs499.assignment2.Assignment2App;

import com.cs499.assignment2.domain.Quote;
import com.cs499.assignment2.repository.QuoteRepository;

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
 * Test class for the QuoteResource REST controller.
 *
 * @see QuoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Assignment2App.class)
public class QuoteResourceIntTest {

    private static final String DEFAULT_PHRASE = "AAAAAAAAAA";
    private static final String UPDATED_PHRASE = "BBBBBBBBBB";

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restQuoteMockMvc;

    private Quote quote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            QuoteResource quoteResource = new QuoteResource(quoteRepository);
        this.restQuoteMockMvc = MockMvcBuilders.standaloneSetup(quoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createEntity(EntityManager em) {
        Quote quote = new Quote()
                .phrase(DEFAULT_PHRASE);
        return quote;
    }

    @Before
    public void initTest() {
        quote = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuote() throws Exception {
        int databaseSizeBeforeCreate = quoteRepository.findAll().size();

        // Create the Quote

        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isCreated());

        // Validate the Quote in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeCreate + 1);
        Quote testQuote = quoteList.get(quoteList.size() - 1);
        assertThat(testQuote.getPhrase()).isEqualTo(DEFAULT_PHRASE);
    }

    @Test
    @Transactional
    public void createQuoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quoteRepository.findAll().size();

        // Create the Quote with an existing ID
        Quote existingQuote = new Quote();
        existingQuote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingQuote)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllQuotes() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        // Get all the quoteList
        restQuoteMockMvc.perform(get("/api/quotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().intValue())))
            .andExpect(jsonPath("$.[*].phrase").value(hasItem(DEFAULT_PHRASE.toString())));
    }

    @Test
    @Transactional
    public void getQuote() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        // Get the quote
        restQuoteMockMvc.perform(get("/api/quotes/{id}", quote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quote.getId().intValue()))
            .andExpect(jsonPath("$.phrase").value(DEFAULT_PHRASE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuote() throws Exception {
        // Get the quote
        restQuoteMockMvc.perform(get("/api/quotes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuote() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);
        int databaseSizeBeforeUpdate = quoteRepository.findAll().size();

        // Update the quote
        Quote updatedQuote = quoteRepository.findOne(quote.getId());
        updatedQuote
                .phrase(UPDATED_PHRASE);

        restQuoteMockMvc.perform(put("/api/quotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuote)))
            .andExpect(status().isOk());

        // Validate the Quote in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeUpdate);
        Quote testQuote = quoteList.get(quoteList.size() - 1);
        assertThat(testQuote.getPhrase()).isEqualTo(UPDATED_PHRASE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuote() throws Exception {
        int databaseSizeBeforeUpdate = quoteRepository.findAll().size();

        // Create the Quote

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuoteMockMvc.perform(put("/api/quotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isCreated());

        // Validate the Quote in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuote() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);
        int databaseSizeBeforeDelete = quoteRepository.findAll().size();

        // Get the quote
        restQuoteMockMvc.perform(delete("/api/quotes/{id}", quote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quote.class);
    }
}
