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
 * A Framework.
 */
@Entity
@Table(name = "framework")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Framework implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name_framework", nullable = false)
    private String nameFramework;

    @Column(name = "version")
    private String version;

    @ManyToMany(mappedBy = "frameworks")
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

    public String getNameFramework() {
        return nameFramework;
    }

    public Framework nameFramework(String nameFramework) {
        this.nameFramework = nameFramework;
        return this;
    }

    public void setNameFramework(String nameFramework) {
        this.nameFramework = nameFramework;
    }

    public String getVersion() {
        return version;
    }

    public Framework version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Article> getArticleIds() {
        return articleIds;
    }

    public Framework articleIds(Set<Article> articles) {
        this.articleIds = articles;
        return this;
    }

    public Framework addArticleId(Article article) {
        this.articleIds.add(article);
        article.getFrameworks().add(this);
        return this;
    }

    public Framework removeArticleId(Article article) {
        this.articleIds.remove(article);
        article.getFrameworks().remove(this);
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
        if (!(o instanceof Framework)) {
            return false;
        }
        return id != null && id.equals(((Framework) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Framework{" +
            "id=" + getId() +
            ", nameFramework='" + getNameFramework() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
