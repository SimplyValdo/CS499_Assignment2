package com.cs499.assignment2.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.assignment2.domain.Statistics;

import com.cs499.assignment2.repository.StatisticsRepository;
import com.cs499.assignment2.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Statistics.
 */
@RestController
@RequestMapping("/api")
public class StatisticsResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

    private static final String ENTITY_NAME = "statistics";
        
    private final StatisticsRepository statisticsRepository;

    public StatisticsResource(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * POST  /statistics : Create a new statistics.
     *
     * @param statistics the statistics to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statistics, or with status 400 (Bad Request) if the statistics has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/statistics")
    @Timed
    public ResponseEntity<Statistics> createStatistics(@RequestBody Statistics statistics) throws URISyntaxException {
        log.debug("REST request to save Statistics : {}", statistics);
        if (statistics.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new statistics cannot already have an ID")).body(null);
        }
        Statistics result = statisticsRepository.save(statistics);
        return ResponseEntity.created(new URI("/api/statistics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /statistics : Updates an existing statistics.
     *
     * @param statistics the statistics to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statistics,
     * or with status 400 (Bad Request) if the statistics is not valid,
     * or with status 500 (Internal Server Error) if the statistics couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/statistics")
    @Timed
    public ResponseEntity<Statistics> updateStatistics(@RequestBody Statistics statistics) throws URISyntaxException {
        log.debug("REST request to update Statistics : {}", statistics);
        if (statistics.getId() == null) {
            return createStatistics(statistics);
        }
        Statistics result = statisticsRepository.save(statistics);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, statistics.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statistics : get all the statistics.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of statistics in body
     */
    @GetMapping("/statistics")
    @Timed
    public List<Statistics> getAllStatistics() {
        log.debug("REST request to get all Statistics");
        List<Statistics> statistics = statisticsRepository.findAll();
        return statistics;
    }

    /**
     * GET  /statistics/:id : get the "id" statistics.
     *
     * @param id the id of the statistics to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statistics, or with status 404 (Not Found)
     */
    @GetMapping("/statistics/{id}")
    @Timed
    public ResponseEntity<Statistics> getStatistics(@PathVariable Long id) {
        log.debug("REST request to get Statistics : {}", id);
        Statistics statistics = statisticsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(statistics));
    }

    /**
     * DELETE  /statistics/:id : delete the "id" statistics.
     *
     * @param id the id of the statistics to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/statistics/{id}")
    @Timed
    public ResponseEntity<Void> deleteStatistics(@PathVariable Long id) {
        log.debug("REST request to delete Statistics : {}", id);
        statisticsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
