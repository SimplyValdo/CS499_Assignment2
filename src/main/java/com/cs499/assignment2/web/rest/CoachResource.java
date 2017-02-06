package com.cs499.assignment2.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.assignment2.domain.Coach;

import com.cs499.assignment2.repository.CoachRepository;
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
 * REST controller for managing Coach.
 */
@RestController
@RequestMapping("/api")
public class CoachResource {

    private final Logger log = LoggerFactory.getLogger(CoachResource.class);

    private static final String ENTITY_NAME = "coach";
        
    private final CoachRepository coachRepository;

    public CoachResource(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    /**
     * POST  /coaches : Create a new coach.
     *
     * @param coach the coach to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coach, or with status 400 (Bad Request) if the coach has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coaches")
    @Timed
    public ResponseEntity<Coach> createCoach(@RequestBody Coach coach) throws URISyntaxException {
        log.debug("REST request to save Coach : {}", coach);
        if (coach.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new coach cannot already have an ID")).body(null);
        }
        Coach result = coachRepository.save(coach);
        return ResponseEntity.created(new URI("/api/coaches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coaches : Updates an existing coach.
     *
     * @param coach the coach to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coach,
     * or with status 400 (Bad Request) if the coach is not valid,
     * or with status 500 (Internal Server Error) if the coach couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coaches")
    @Timed
    public ResponseEntity<Coach> updateCoach(@RequestBody Coach coach) throws URISyntaxException {
        log.debug("REST request to update Coach : {}", coach);
        if (coach.getId() == null) {
            return createCoach(coach);
        }
        Coach result = coachRepository.save(coach);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coach.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coaches : get all the coaches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coaches in body
     */
    @GetMapping("/coaches")
    @Timed
    public List<Coach> getAllCoaches() {
        log.debug("REST request to get all Coaches");
        List<Coach> coaches = coachRepository.findAll();
        return coaches;
    }

    /**
     * GET  /coaches/:id : get the "id" coach.
     *
     * @param id the id of the coach to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coach, or with status 404 (Not Found)
     */
    @GetMapping("/coaches/{id}")
    @Timed
    public ResponseEntity<Coach> getCoach(@PathVariable Long id) {
        log.debug("REST request to get Coach : {}", id);
        Coach coach = coachRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coach));
    }

    /**
     * DELETE  /coaches/:id : delete the "id" coach.
     *
     * @param id the id of the coach to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coaches/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoach(@PathVariable Long id) {
        log.debug("REST request to delete Coach : {}", id);
        coachRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
