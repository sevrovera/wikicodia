package com.ipi.wikicodia.web.rest;

import com.ipi.wikicodia.domain.Vote;
import com.ipi.wikicodia.repository.VoteRepository;
import com.ipi.wikicodia.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ipi.wikicodia.domain.Vote}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoteResource {

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    private static final String ENTITY_NAME = "vote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoteRepository voteRepository;

    public VoteResource(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    /**
     * {@code POST  /votes} : Create a new vote.
     *
     * @param vote the vote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vote, or with status {@code 400 (Bad Request)} if the vote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/votes")
    public ResponseEntity<Vote> createVote(@Valid @RequestBody Vote vote) throws URISyntaxException {
        log.debug("REST request to save Vote : {}", vote);
        if (vote.getId() != null) {
            throw new BadRequestAlertException("A new vote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vote result = voteRepository.save(vote);
        return ResponseEntity.created(new URI("/api/votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /votes} : Updates an existing vote.
     *
     * @param vote the vote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vote,
     * or with status {@code 400 (Bad Request)} if the vote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/votes")
    public ResponseEntity<Vote> updateVote(@Valid @RequestBody Vote vote) throws URISyntaxException {
        log.debug("REST request to update Vote : {}", vote);
        if (vote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vote result = voteRepository.save(vote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vote.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /votes} : get all the votes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of votes in body.
     */
    @GetMapping("/votes")
    public ResponseEntity<List<Vote>> getAllVotes(Pageable pageable) {
        log.debug("REST request to get a page of Votes");
        Page<Vote> page = voteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /votes/:id} : get the "id" vote.
     *
     * @param id the id of the vote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/votes/{id}")
    public ResponseEntity<Vote> getVote(@PathVariable Long id) {
        log.debug("REST request to get Vote : {}", id);
        Optional<Vote> vote = voteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vote);
    }

    /**
     * {@code DELETE  /votes/:id} : delete the "id" vote.
     *
     * @param id the id of the vote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/votes/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
        log.debug("REST request to delete Vote : {}", id);
        voteRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
