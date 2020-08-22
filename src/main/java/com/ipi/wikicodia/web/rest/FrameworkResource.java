package com.ipi.wikicodia.web.rest;

import com.ipi.wikicodia.domain.Framework;
import com.ipi.wikicodia.repository.FrameworkRepository;
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
 * REST controller for managing {@link com.ipi.wikicodia.domain.Framework}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FrameworkResource {

    private final Logger log = LoggerFactory.getLogger(FrameworkResource.class);

    private static final String ENTITY_NAME = "framework";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrameworkRepository frameworkRepository;

    public FrameworkResource(FrameworkRepository frameworkRepository) {
        this.frameworkRepository = frameworkRepository;
    }

    /**
     * {@code POST  /frameworks} : Create a new framework.
     *
     * @param framework the framework to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new framework, or with status {@code 400 (Bad Request)} if the framework has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frameworks")
    public ResponseEntity<Framework> createFramework(@Valid @RequestBody Framework framework) throws URISyntaxException {
        log.debug("REST request to save Framework : {}", framework);
        if (framework.getId() != null) {
            throw new BadRequestAlertException("A new framework cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Framework result = frameworkRepository.save(framework);
        return ResponseEntity.created(new URI("/api/frameworks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frameworks} : Updates an existing framework.
     *
     * @param framework the framework to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated framework,
     * or with status {@code 400 (Bad Request)} if the framework is not valid,
     * or with status {@code 500 (Internal Server Error)} if the framework couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frameworks")
    public ResponseEntity<Framework> updateFramework(@Valid @RequestBody Framework framework) throws URISyntaxException {
        log.debug("REST request to update Framework : {}", framework);
        if (framework.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Framework result = frameworkRepository.save(framework);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, framework.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /frameworks} : get all the frameworks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frameworks in body.
     */
    @GetMapping("/frameworks")
    public ResponseEntity<List<Framework>> getAllFrameworks(Pageable pageable) {
        log.debug("REST request to get a page of Frameworks");
        Page<Framework> page = frameworkRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frameworks/:id} : get the "id" framework.
     *
     * @param id the id of the framework to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the framework, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frameworks/{id}")
    public ResponseEntity<Framework> getFramework(@PathVariable Long id) {
        log.debug("REST request to get Framework : {}", id);
        Optional<Framework> framework = frameworkRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(framework);
    }

    /**
     * {@code DELETE  /frameworks/:id} : delete the "id" framework.
     *
     * @param id the id of the framework to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frameworks/{id}")
    public ResponseEntity<Void> deleteFramework(@PathVariable Long id) {
        log.debug("REST request to delete Framework : {}", id);
        frameworkRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
