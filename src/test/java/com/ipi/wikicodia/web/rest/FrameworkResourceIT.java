package com.ipi.wikicodia.web.rest;

import com.ipi.wikicodia.WikicodiaApp;
import com.ipi.wikicodia.domain.Framework;
import com.ipi.wikicodia.repository.FrameworkRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FrameworkResource} REST controller.
 */
@SpringBootTest(classes = WikicodiaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FrameworkResourceIT {

    private static final String DEFAULT_NAME_FRAMEWORK = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FRAMEWORK = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    @Autowired
    private FrameworkRepository frameworkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFrameworkMockMvc;

    private Framework framework;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Framework createEntity(EntityManager em) {
        Framework framework = new Framework()
            .nameFramework(DEFAULT_NAME_FRAMEWORK)
            .version(DEFAULT_VERSION);
        return framework;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Framework createUpdatedEntity(EntityManager em) {
        Framework framework = new Framework()
            .nameFramework(UPDATED_NAME_FRAMEWORK)
            .version(UPDATED_VERSION);
        return framework;
    }

    @BeforeEach
    public void initTest() {
        framework = createEntity(em);
    }

    @Test
    @Transactional
    public void createFramework() throws Exception {
        int databaseSizeBeforeCreate = frameworkRepository.findAll().size();
        // Create the Framework
        restFrameworkMockMvc.perform(post("/api/frameworks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isCreated());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeCreate + 1);
        Framework testFramework = frameworkList.get(frameworkList.size() - 1);
        assertThat(testFramework.getNameFramework()).isEqualTo(DEFAULT_NAME_FRAMEWORK);
        assertThat(testFramework.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void createFrameworkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frameworkRepository.findAll().size();

        // Create the Framework with an existing ID
        framework.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrameworkMockMvc.perform(post("/api/frameworks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isBadRequest());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameFrameworkIsRequired() throws Exception {
        int databaseSizeBeforeTest = frameworkRepository.findAll().size();
        // set the field null
        framework.setNameFramework(null);

        // Create the Framework, which fails.


        restFrameworkMockMvc.perform(post("/api/frameworks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isBadRequest());

        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFrameworks() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        // Get all the frameworkList
        restFrameworkMockMvc.perform(get("/api/frameworks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(framework.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameFramework").value(hasItem(DEFAULT_NAME_FRAMEWORK)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }
    
    @Test
    @Transactional
    public void getFramework() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        // Get the framework
        restFrameworkMockMvc.perform(get("/api/frameworks/{id}", framework.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(framework.getId().intValue()))
            .andExpect(jsonPath("$.nameFramework").value(DEFAULT_NAME_FRAMEWORK))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }
    @Test
    @Transactional
    public void getNonExistingFramework() throws Exception {
        // Get the framework
        restFrameworkMockMvc.perform(get("/api/frameworks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFramework() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();

        // Update the framework
        Framework updatedFramework = frameworkRepository.findById(framework.getId()).get();
        // Disconnect from session so that the updates on updatedFramework are not directly saved in db
        em.detach(updatedFramework);
        updatedFramework
            .nameFramework(UPDATED_NAME_FRAMEWORK)
            .version(UPDATED_VERSION);

        restFrameworkMockMvc.perform(put("/api/frameworks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFramework)))
            .andExpect(status().isOk());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
        Framework testFramework = frameworkList.get(frameworkList.size() - 1);
        assertThat(testFramework.getNameFramework()).isEqualTo(UPDATED_NAME_FRAMEWORK);
        assertThat(testFramework.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingFramework() throws Exception {
        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrameworkMockMvc.perform(put("/api/frameworks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isBadRequest());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFramework() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        int databaseSizeBeforeDelete = frameworkRepository.findAll().size();

        // Delete the framework
        restFrameworkMockMvc.perform(delete("/api/frameworks/{id}", framework.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
