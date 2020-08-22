package com.ipi.wikicodia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.ipi.wikicodia.domain.enumeration.ArticleType;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "creation_date")
    private LocalDate creationDate = LocalDate.now();

    @Column(name = "last_edit_date")
    private LocalDate lastEditDate = LocalDate.now();

    @NotNull
    @Size(min = 20, max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;
    
    @NotNull
    @Size(min = 100, max = 65535)
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    @NotNull
    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "is_validated")
    private Boolean isValidated;

    @NotNull
    @Column(name = "is_promoted")
    private Boolean isPromoted;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "article_type", nullable = false)
    private ArticleType articleType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "articles", allowSetters = true)
    private Category category;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "articles", allowSetters = true)
    private User author;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "article_ratings",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "ratings_id", referencedColumnName = "id"))
    private Set<Vote> ratings = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotNull
    @JoinTable(name = "article_language",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "language_id", referencedColumnName = "id"))
    private Set<Language> languages = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "article_framework",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "framework_id", referencedColumnName = "id"))
    private Set<Framework> frameworks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Article title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Article creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastEditDate() {
        return lastEditDate;
    }

    public Article lastEditDate(LocalDate lastEditDate) {
        this.lastEditDate = lastEditDate;
        return this;
    }

    public void setLastEditDate(LocalDate lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getDescription() {
        return description;
    }

    public Article description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getContent() {
        return content;
    }

    public Article content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isIsPublished() {
        return isPublished;
    }

    public Article isPublished(Boolean isPublished) {
        this.isPublished = isPublished;
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Boolean isIsValidated() {
        return isValidated;
    }

    public Article isValidated(Boolean isValidated) {
        this.isValidated = isValidated;
        return this;
    }

    public void setIsValidated(Boolean isValidated) {
        this.isValidated = isValidated;
    }

    public Boolean isIsPromoted() {
        return isPromoted;
    }

    public Article isPromoted(Boolean isPromoted) {
        this.isPromoted = isPromoted;
        return this;
    }

    public void setIsPromoted(Boolean isPromoted) {
        this.isPromoted = isPromoted;
    }

    public ArticleType getArticleType() {
        return articleType;
    }

    public Article articleType(ArticleType articleType) {
        this.articleType = articleType;
        return this;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
    }

    public Category getCategory() {
        return category;
    }

    public Article category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public Article author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public Set<Vote> getRatings() {
        return ratings;
    }

    public Article ratings(Set<Vote> votes) {
        this.ratings = votes;
        return this;
    }

    public Article addRatings(Vote vote) {
        this.ratings.add(vote);
        vote.getArticleIds().add(this);
        return this;
    }

    public Article removeRatings(Vote vote) {
        this.ratings.remove(vote);
        vote.getArticleIds().remove(this);
        return this;
    }

    public void setRatings(Set<Vote> votes) {
        this.ratings = votes;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public Article languages(Set<Language> languages) {
        this.languages = languages;
        return this;
    }

    public Article addLanguage(Language language) {
        this.languages.add(language);
        language.getArticleIds().add(this);
        return this;
    }

    public Article removeLanguage(Language language) {
        this.languages.remove(language);
        language.getArticleIds().remove(this);
        return this;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Set<Framework> getFrameworks() {
        return frameworks;
    }

    public Article frameworks(Set<Framework> frameworks) {
        this.frameworks = frameworks;
        return this;
    }

    public Article addFramework(Framework framework) {
        this.frameworks.add(framework);
        framework.getArticleIds().add(this);
        return this;
    }

    public Article removeFramework(Framework framework) {
        this.frameworks.remove(framework);
        framework.getArticleIds().remove(this);
        return this;
    }

    public void setFrameworks(Set<Framework> frameworks) {
        this.frameworks = frameworks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastEditDate='" + getLastEditDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", content='" + getContent() + "'" +
            ", isPublished='" + isIsPublished() + "'" +
            ", isValidated='" + isIsValidated() + "'" +
            ", isPromoted='" + isIsPromoted() + "'" +
            ", articleType='" + getArticleType() + "'" +
            "}";
    }
}
