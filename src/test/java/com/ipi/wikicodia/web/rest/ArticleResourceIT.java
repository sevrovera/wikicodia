package com.ipi.wikicodia.web.rest;

import com.ipi.wikicodia.WikicodiaApp;
import com.ipi.wikicodia.domain.Article;
import com.ipi.wikicodia.domain.Category;
import com.ipi.wikicodia.domain.User;
import com.ipi.wikicodia.domain.Language;
import com.ipi.wikicodia.repository.ArticleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ipi.wikicodia.domain.enumeration.ArticleType;
/**
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@SpringBootTest(classes = WikicodiaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ArticleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_EDIT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_EDIT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PUBLISHED = false;
    private static final Boolean UPDATED_IS_PUBLISHED = true;

    private static final Boolean DEFAULT_IS_VALIDATED = false;
    private static final Boolean UPDATED_IS_VALIDATED = true;

    private static final Boolean DEFAULT_IS_PROMOTED = false;
    private static final Boolean UPDATED_IS_PROMOTED = true;

    private static final ArticleType DEFAULT_ARTICLE_TYPE = ArticleType.TUTORIAL;
    private static final ArticleType UPDATED_ARTICLE_TYPE = ArticleType.TROUBLESHOOTING;

    @Autowired
    private ArticleRepository articleRepository;

    @Mock
    private ArticleRepository articleRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createEntity(EntityManager em) {
        Article article = new Article()
            .title(DEFAULT_TITLE)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastEditDate(DEFAULT_LAST_EDIT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .isPublished(DEFAULT_IS_PUBLISHED)
            .isValidated(DEFAULT_IS_VALIDATED)
            .isPromoted(DEFAULT_IS_PROMOTED)
            .articleType(DEFAULT_ARTICLE_TYPE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        article.setCategory(category);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        article.setAuthor(user);
        // Add required entity
        Language language;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            language = LanguageResourceIT.createEntity(em);
            em.persist(language);
            em.flush();
        } else {
            language = TestUtil.findAll(em, Language.class).get(0);
        }
        article.getLanguages().add(language);
        return article;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity(EntityManager em) {
        Article article = new Article()
            .title(UPDATED_TITLE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastEditDate(UPDATED_LAST_EDIT_DATE)
            .description(UPDATED_DESCRIPTION)
            .isPublished(UPDATED_IS_PUBLISHED)
            .isValidated(UPDATED_IS_VALIDATED)
            .isPromoted(UPDATED_IS_PROMOTED)
            .articleType(UPDATED_ARTICLE_TYPE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        article.setCategory(category);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        article.setAuthor(user);
        // Add required entity
        Language language;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            language = LanguageResourceIT.createUpdatedEntity(em);
            em.persist(language);
            em.flush();
        } else {
            language = TestUtil.findAll(em, Language.class).get(0);
        }
        article.getLanguages().add(language);
        return article;
    }

    @BeforeEach
    public void initTest() {
        article = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();
        // Create the Article
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticle.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testArticle.getLastEditDate()).isEqualTo(DEFAULT_LAST_EDIT_DATE);
        assertThat(testArticle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArticle.isIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
        assertThat(testArticle.isIsValidated()).isEqualTo(DEFAULT_IS_VALIDATED);
        assertThat(testArticle.isIsPromoted()).isEqualTo(DEFAULT_IS_PROMOTED);
        assertThat(testArticle.getArticleType()).isEqualTo(DEFAULT_ARTICLE_TYPE);
    }

    @Test
    @Transactional
    public void createArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article with an existing ID
        article.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setTitle(null);

        // Create the Article, which fails.


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setCreationDate(null);

        // Create the Article, which fails.


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setDescription(null);

        // Create the Article, which fails.


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsPublishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setIsPublished(null);

        // Create the Article, which fails.


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsPromotedIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setIsPromoted(null);

        // Create the Article, which fails.


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArticleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setArticleType(null);

        // Create the Article, which fails.


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastEditDate").value(hasItem(DEFAULT_LAST_EDIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].isValidated").value(hasItem(DEFAULT_IS_VALIDATED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPromoted").value(hasItem(DEFAULT_IS_PROMOTED.booleanValue())))
            .andExpect(jsonPath("$.[*].articleType").value(hasItem(DEFAULT_ARTICLE_TYPE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllArticlesWithEagerRelationshipsIsEnabled() throws Exception {
        when(articleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArticleMockMvc.perform(get("/api/articles?eagerload=true"))
            .andExpect(status().isOk());

        verify(articleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllArticlesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(articleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArticleMockMvc.perform(get("/api/articles?eagerload=true"))
            .andExpect(status().isOk());

        verify(articleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastEditDate").value(DEFAULT_LAST_EDIT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isPublished").value(DEFAULT_IS_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.isValidated").value(DEFAULT_IS_VALIDATED.booleanValue()))
            .andExpect(jsonPath("$.isPromoted").value(DEFAULT_IS_PROMOTED.booleanValue()))
            .andExpect(jsonPath("$.articleType").value(DEFAULT_ARTICLE_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).get();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .title(UPDATED_TITLE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastEditDate(UPDATED_LAST_EDIT_DATE)
            .description(UPDATED_DESCRIPTION)
            .isPublished(UPDATED_IS_PUBLISHED)
            .isValidated(UPDATED_IS_VALIDATED)
            .isPromoted(UPDATED_IS_PROMOTED)
            .articleType(UPDATED_ARTICLE_TYPE);

        restArticleMockMvc.perform(put("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedArticle)))
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticle.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testArticle.getLastEditDate()).isEqualTo(UPDATED_LAST_EDIT_DATE);
        assertThat(testArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArticle.isIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testArticle.isIsValidated()).isEqualTo(UPDATED_IS_VALIDATED);
        assertThat(testArticle.isIsPromoted()).isEqualTo(UPDATED_IS_PROMOTED);
        assertThat(testArticle.getArticleType()).isEqualTo(UPDATED_ARTICLE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc.perform(put("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        // Delete the article
        restArticleMockMvc.perform(delete("/api/articles/{id}", article.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
