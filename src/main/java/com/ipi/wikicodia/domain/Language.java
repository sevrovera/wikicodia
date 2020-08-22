package com.ipi.wikicodia.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "name_language", nullable = false, unique = true)
    private String nameLanguage;

    @Column(name = "version")
    private String version;

    @ManyToMany(mappedBy = "languages")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Article> articleIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameLanguage() {
        return nameLanguage;
    }

    public Language nameLanguage(String nameLanguage) {
        this.nameLanguage = nameLanguage;
        return this;
    }

    public void setNameLanguage(String nameLanguage) {
        this.nameLanguage = nameLanguage;
    }

    public String getVersion() {
        return version;
    }

    public Language version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Article> getArticleIds() {
        return articleIds;
    }

    public Language articleIds(Set<Article> articles) {
        this.articleIds = articles;
        return this;
    }

    public Language addArticleId(Article article) {
        this.articleIds.add(article);
        article.getLanguages().add(this);
        return this;
    }

    public Language removeArticleId(Article article) {
        this.articleIds.remove(article);
        article.getLanguages().remove(this);
        return this;
    }

    public void setArticleIds(Set<Article> articles) {
        this.articleIds = articles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Language)) {
            return false;
        }
        return id != null && id.equals(((Language) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", nameLanguage='" + getNameLanguage() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
